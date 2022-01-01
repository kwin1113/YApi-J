package xyz.kwin.yapi.util;

import com.google.common.collect.Sets;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTypesUtil;
import xyz.kwin.yapi.constant.CommonConstant;
import xyz.kwin.yapi.entity.node.ClassNode;
import xyz.kwin.yapi.setting.SettingAdaptor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 解析Psi对象工具类
 *
 * @author kwin
 * @since 2021/12/26 6:54 下午
 */
public class PsiParseUtil {

    private static final Logger log = Logger.getInstance(PsiParseUtil.class);

    private static final Set<String> COLLECTION_PREFIX = Sets.newHashSet("java.util.Collection",
            "java.util.List", "java.util.Map", "java.util.Deque", "java.util.Queue", "java.util.Set");
    private static final Set<String> UNIFIED_TYPE_SET = Sets.newHashSet("short", "Short", "byte", "Byte",
            "char", "Character", "boolean", "Boolean", "int", "Integer", "BigInteger",
            "float", "Float", "double", "Double", "long", "Long", "BigDecimal", "String");

    /**
     * 将指定的对象解析为Java类对象（外部入口）
     *
     * @param psiType 类型
     * @return 对应类对象
     */
    public static ClassNode parse2Java(PsiType psiType) {
        return parse2Java(psiType, new HashMap<>());
    }

    /**
     * 将指定的对象解析为Java类对象
     * <p>
     * 1. 基础对象直接返回
     * 2. 数组对象需要解析内部元素
     * 3. 集合对象（自定义泛型集合）需要借助解析缓存获取使用时的具体类
     * 4. 普通类直接解析（同时会带上类注释作为描述）
     *
     * @param psiType   类型
     * @param nodeCache 解析缓存
     *                  1. 快速返回已解析过的类
     *                  2. 自定义泛型集合根据T/E/K/V等获取集合内部当前使用的具体类型
     * @return 对应类对象
     */
    private static ClassNode parse2Java(PsiType psiType, @NotNull Map<String, ClassNode> nodeCache) {
        if (nodeCache.containsKey(psiType.getCanonicalText())) {
            return nodeCache.get(psiType.getCanonicalText());
        }

        String rootTypeName = psiType.getCanonicalText();
        // 新建类节点
        ClassNode classNode = new ClassNode(psiType.getPresentableText(), psiType.getPresentableText(), psiType.getCanonicalText());
        if (!isSupported(psiType)) {
            return classNode;
        }

        boolean isCollection, isGeneric;
        if (isPrimaryType(psiType)) {
            // 基础类型
            return classNode;
        } else if (isArray(psiType)) {
            classNode.setArray(true);
            PsiType deepComponentType = psiType.getDeepComponentType();
            classNode.setType(deepComponentType.getPresentableText());
            PsiClass psiClass = PsiTypesUtil.getPsiClass(deepComponentType);
            if (psiClass == null) {
                log.warn("parse class error, cannot get corresponding psi class. psi type: " + deepComponentType.getCanonicalText());
                return classNode;
            }
            List<ClassNode> fields = getFields(rootTypeName, psiClass, nodeCache);
            fields.forEach(e -> nodeCache.put(e.getQualifiedType(), e));
            classNode.setFields(fields);
        } else if ((isCollection = isCollection(psiType)) | (isGeneric = isGeneric(psiType))) {
            classNode.setCollection(isCollection);
            classNode.setCustomGeneric(isGeneric && !isCollection);

            // 解析集合的元素类型
            PsiJavaCodeReferenceElement psiJavaCodeReferenceElement = (PsiJavaCodeReferenceElement) ((PsiClassReferenceType) psiType).getPsiContext();
            if (psiJavaCodeReferenceElement == null) {
                return classNode;
            }

            // 集合元素会有多个，例如Map
            List<ClassNode> collectionElements = new ArrayList<>();

            // 获取到包含泛型的自定义集合类类对象，获取泛型类的标识符，例如T、E等
            // 顺序对应下面的elementTypes集合
            PsiClass containingClass = PsiTypesUtil.getPsiClass(psiType);
            List<String> genericIdentifications = containingClass == null ? null :
                    Arrays.stream(containingClass.getTypeParameters()).map(PsiTypeParameter::getText).collect(Collectors.toList());
            PsiType[] elementTypes = psiJavaCodeReferenceElement.getTypeParameters();
            for (int i = 0; i < elementTypes.length; i++) {
                PsiType elementType = elementTypes[i];
                PsiClass elementClass = PsiTypesUtil.getPsiClass(elementType);
                if (elementClass == null) {
                    log.warn("parse class error, cannot get corresponding psi class. psi type: " + elementType.getCanonicalText());
                    continue;
                }
                ClassNode elementNode;
                if (rootTypeName.equals(elementType.getCanonicalText())) {
                    elementNode = new ClassNode(elementType.getPresentableText(), elementType.getPresentableText(), elementType.getCanonicalText());
                } else {
                    elementNode = parse2Java(elementType);
                }
                // 往解析缓存中放入当前类的缓存，同时还要将泛型标识符做为Key加入缓存
                // 即此时加入两个相同的value，key1：类全限定名，key2：泛型标识符（T/E/K/V等）
                nodeCache.put(elementNode.getQualifiedType(), elementNode);
                if (genericIdentifications != null) {
                    nodeCache.put(genericIdentifications.get(i), elementNode);
                }
                collectionElements.add(elementNode);
            }
            classNode.setCollectionElements(collectionElements);

            // 如果是自定义泛型集合，需要将集合的其他字段解析
            if (isGeneric && !isCollection) {
                PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
                if (psiClass == null) {
                    log.warn("parse class error, cannot get corresponding psi class. psi type: " + psiType.getCanonicalText());
                } else {
                    List<ClassNode> fields = getFields(rootTypeName, psiClass, nodeCache);
                    fields.forEach(e -> nodeCache.put(e.getQualifiedType(), e));
                    classNode.setFields(fields);
                    classNode.setCollectionElements(null);
                }
            }

            // 获取集合类真正的类型，例如List/Map等
            PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
            if (psiClass == null) {
                log.warn("parse class error, cannot get corresponding psi class. psi type: " + rootTypeName);
                return classNode;
            }
            PsiClassType classType = PsiTypesUtil.getClassType(psiClass);
            classNode.setType(classType.getPresentableText());
        } else {
            // 解析类
            // 获取类注释 + required
            PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
            if (psiClass == null) {
                log.warn("parse class error, cannot get corresponding psi class. psi type: " + rootTypeName);
                return classNode;
            }
            PsiDocComment classDocComment = psiClass.getDocComment();
            if (classDocComment != null) {
                String commentText = TextUtil.getCommentFromCommonJavaDoc(classDocComment.getText());
                if (commentText != null && (commentText.endsWith(" true") || commentText.endsWith(" false"))) {
                    String[] arr = commentText.split(" ");
                    if (arr.length > 1) {
                        commentText = String.join("", Arrays.copyOfRange(arr, 0, arr.length - 1));
                        boolean required = Boolean.parseBoolean(arr[arr.length - 1]);
                        classNode.setRequired(required);
                    }
                }
                classNode.setDesc(commentText);
            }
            List<ClassNode> fields = getFields(rootTypeName, psiClass, nodeCache);
            classNode.setFields(fields);
        }

        nodeCache.put(classNode.getQualifiedType(), classNode);
        return classNode;
    }

    /**
     * 解析Java类的属性
     * （过滤static/final）
     *
     * @param parentTypeName 父级节点全限定名
     * @param psiClass       psi类对象
     * @param nodeCache      解析节点缓存
     * @return 指定类对象下的所有属性节点
     */
    private static List<ClassNode> getFields(String parentTypeName, PsiClass psiClass, @NotNull Map<String, ClassNode> nodeCache) {
        List<ClassNode> fields = new ArrayList<>();
        List<PsiField> psiFields = Arrays.stream(psiClass.getFields()).collect(Collectors.toList());
        PsiClass superClass = psiClass.getSuperClass();
        if (superClass != null && superClass.getQualifiedName() != null && !superClass.getQualifiedName().startsWith("java.")) {
            PsiField[] superFields = superClass.getFields();
            psiFields.addAll(Arrays.stream(superFields).collect(Collectors.toList()));
        }
        for (PsiField psiField : psiFields) {
            // 跳过static/final修饰的字段
            PsiModifierList modifierList = psiField.getModifierList();
            if (modifierList != null && (modifierList.hasModifierProperty(PsiModifier.STATIC) || modifierList.hasModifierProperty(PsiModifier.FINAL))) {
                continue;
            }
            PsiType psiType = psiField.getType();

            ClassNode fieldNode;
            // 根据泛型获取
            if (nodeCache.containsKey(psiType.getPresentableText())) {
                fieldNode = nodeCache.get(psiType.getPresentableText());
            }
            // 从缓存中获取
            else if (nodeCache.containsKey(psiType.getCanonicalText())) {
                fieldNode = nodeCache.get(psiType.getCanonicalText());
                PsiDocComment docComment = psiField.getDocComment();
                if (docComment != null && docComment.getText() != null) {
                    // 解析field上的注释 + required
                    String commentText = TextUtil.getCommentFromCommonJavaDoc(docComment.getText());
                    if (commentText != null && (commentText.endsWith("true") || commentText.endsWith("false"))) {
                        String[] arr = commentText.split(" ");
                        if (arr.length > 1) {
                            commentText = String.join(" ", Arrays.copyOfRange(arr, 0, arr.length - 1));
                            boolean required = Boolean.parseBoolean(arr[arr.length - 1]);
                            fieldNode.setRequired(required);
                        }
                    }
                    fieldNode.setDesc(commentText);
                }
            }
            // 解析Java对象
            else {
                fieldNode = resolveRecursion(parentTypeName, psiType, nodeCache);
                PsiDocComment docComment = psiField.getDocComment();
                if (docComment != null && docComment.getText() != null) {
                    // 解析field上的注释 + required
                    String commentText = TextUtil.getCommentFromCommonJavaDoc(docComment.getText());
                    if (commentText != null && (commentText.endsWith("true") || commentText.endsWith("false"))) {
                        String[] arr = commentText.split(" ");
                        if (arr.length > 1) {
                            commentText = String.join(" ", Arrays.copyOfRange(arr, 0, arr.length - 1));
                            boolean required = Boolean.parseBoolean(arr[arr.length - 1]);
                            fieldNode.setRequired(required);
                        }
                    }
                    fieldNode.setDesc(commentText);
                }
            }
            String fieldName = psiField.getName();
            fieldNode.setName(fieldName);
            fields.add(fieldNode);
        }

        return fields;
    }

    /**
     * 处理子节点递归（例如链表/树），用在解析类属性上
     * 流程与{@link PsiParseUtil#parse2Java(com.intellij.psi.PsiType, java.util.Map)}方法类似，但该方法不操作nodeCache
     *
     * @param parentTypeName 父级节点全限定名
     * @param psiType        当前节点type
     * @param nodeCache      解析节点缓存
     * @return 解析节点
     */
    private static @NotNull ClassNode resolveRecursion(String parentTypeName, PsiType psiType, Map<String, ClassNode> nodeCache) {
        boolean isCollection, isGeneric;
        if (isPrimaryType(psiType)) {
            // 基础类型
            return parse2Java(psiType, nodeCache);
        } else if (isArray(psiType)) {
            PsiType deepComponentType = psiType.getDeepComponentType();
            if (parentTypeName.equals(deepComponentType.getCanonicalText())) {
                ClassNode classNode = new ClassNode(psiType.getPresentableText(), psiType.getPresentableText(), psiType.getCanonicalText());
                classNode.setArray(true);
                classNode.setListOrTree(true);
                return classNode;
            }
            return parse2Java(psiType, nodeCache);
        } else if ((isCollection = isCollection(psiType)) | (isGeneric = isGeneric(psiType))) {
            ClassNode classNode = new ClassNode(psiType.getPresentableText(), psiType.getPresentableText(), psiType.getCanonicalText());
            classNode.setCollection(isCollection);
            classNode.setCustomGeneric(isGeneric && !isCollection);

            // 解析集合的元素类型
            PsiJavaCodeReferenceElement psiJavaCodeReferenceElement = (PsiJavaCodeReferenceElement) ((PsiClassReferenceType) psiType).getPsiContext();
            if (psiJavaCodeReferenceElement == null) {
                return parse2Java(psiType, nodeCache);
            }

            // 集合元素会有多个，例如Map
            List<ClassNode> collectionElements = new ArrayList<>();
            PsiType[] elementTypes = psiJavaCodeReferenceElement.getTypeParameters();
            for (PsiType elementType : elementTypes) {
                PsiClass elementClass = PsiTypesUtil.getPsiClass(elementType);
                if (elementClass == null) {
                    log.warn("parse class error, cannot get corresponding psi class. psi type: " + elementType.getCanonicalText());
                    continue;
                }
                ClassNode elementNode;
                if (parentTypeName.equals(elementType.getCanonicalText())) {
                    elementNode = new ClassNode(elementType.getPresentableText(), elementType.getPresentableText(), psiType.getCanonicalText());
                    elementNode.setListOrTree(true);
                } else {
                    // 获取泛型记号
                    if (nodeCache.containsKey(elementType.getPresentableText())) {
                        elementNode = nodeCache.get(elementType.getPresentableText());
                    } else if (nodeCache.containsKey(elementType.getCanonicalText())) {
                        elementNode = nodeCache.get(elementType.getCanonicalText());
                    } else {
                        elementNode = parse2Java(elementType, nodeCache);
                    }
                }
                collectionElements.add(elementNode);
            }
            classNode.setCollectionElements(collectionElements);

            // 如果是自定义泛型集合，需要将集合的其他字段解析
            if (isGeneric && !isCollection) {
                PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
                if (psiClass == null) {
                    log.warn("parse class error, cannot get corresponding psi class. psi type: " + psiType.getCanonicalText());
                } else {
                    List<ClassNode> fields = getFields(parentTypeName, psiClass, nodeCache);
                    classNode.setFields(fields);
                    classNode.setCollectionElements(null);
                }
            }

            // 获取集合类真正的类型，例如List/Map等
            PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
            if (psiClass == null) {
                log.warn("parse class error, cannot get corresponding psi class. psi type: " + psiType);
                return classNode;
            }
            PsiClassType classType = PsiTypesUtil.getClassType(psiClass);
            classNode.setType(classType.getPresentableText());
            return classNode;
        } else {
            if (parentTypeName.equals(psiType.getCanonicalText())) {
                ClassNode classNode = new ClassNode(psiType.getPresentableText(), psiType.getPresentableText(), psiType.getCanonicalText());
                classNode.setListOrTree(true);
                return classNode;
            } else {
                // 直接解析
                return parse2Java(psiType, nodeCache);
            }
        }
    }

    /**
     * 是否为基础类型
     */
    private static boolean isPrimaryType(PsiType psiType) {
        String type = psiType.getPresentableText();
        return psiType instanceof PsiPrimitiveType || UNIFIED_TYPE_SET.contains(type);
    }

    /**
     * 是否为数组类型
     */
    private static boolean isArray(PsiType psiType) {
        return psiType instanceof PsiArrayType;
    }

    /**
     * 是否为集合类型
     */
    private static boolean isCollection(PsiType psiType) {
        String text = psiType.getCanonicalText();
        for (String collectionPrefix : COLLECTION_PREFIX) {
            if (text.startsWith(collectionPrefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为自定义泛型容器
     */
    private static boolean isGeneric(PsiType psiType) {
        PsiJavaCodeReferenceElement psiJavaCodeReferenceElement = (PsiJavaCodeReferenceElement) ((PsiClassReferenceType) psiType).getPsiContext();
        if (psiJavaCodeReferenceElement == null) {
            return false;
        }
        PsiType[] elementTypes = psiJavaCodeReferenceElement.getTypeParameters();
        return elementTypes.length != 0;
    }

    /**
     * 是否支持解析该类
     * 用于终止解析
     */
    private static boolean isSupported(PsiType psiType) {
        PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
        if (psiClass == null) {
            log.warn("parse class error, cannot get corresponding psi class. psi type: " + psiType.getCanonicalText());
            return true;
        }
        PsiClassType classType = PsiTypesUtil.getClassType(psiClass);
        String qualifiedName = classType.getCanonicalText();
        if (qualifiedName.startsWith("java.")) {
            return CommonConstant.SUPPORTED_JAVA_TYPE.contains(qualifiedName);
        }
        return true;
    }

    /**
     * 是否被限制解析
     *
     * @param psiType 类型
     * @param config  配置
     * @return 从用户配置中判断
     */
    public static boolean isAllowedInConfig(PsiType psiType, SettingAdaptor config) {
        String className = psiType.getCanonicalText();
        // 如果是数组元素类型被限制，则不解析，但是集合中的必须解析，因为Map中会有多种元素
        if (isArray(psiType)) {
            PsiType deepComponentType = psiType.getDeepComponentType();
            className = deepComponentType.getCanonicalText();
        }
        return config == null || !config.getIgnoreClasses().contains(className);
    }
}
