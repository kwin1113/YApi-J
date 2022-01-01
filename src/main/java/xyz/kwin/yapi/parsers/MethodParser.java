package xyz.kwin.yapi.parsers;

import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import xyz.kwin.yapi.entity.node.ClassNode;
import xyz.kwin.yapi.setting.SettingAdaptor;
import xyz.kwin.yapi.util.PsiParseUtil;
import xyz.kwin.yapi.util.TextUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 方法解析
 *
 * @author kwin
 * @since 2021/12/25 9:09 下午
 */
public class MethodParser {

    private static final String UNKNOWN_METHOD = "UNKNOWN_METHOD";
    private static final String PARAM_TAG = "param";
    private static final String RETURN_TAG = "return";

    private final SettingAdaptor settingAdaptor = SettingAdaptor.getInstance();

    /**
     * 获取类注释做描述
     *
     * @param psiClass 类对象
     * @return 类注释
     */
    public String getDesc(PsiClass psiClass) {
        PsiDocComment docComment = psiClass.getDocComment();
        return docComment == null ? null : TextUtil.getCommentFromCommonJavaDoc(docComment.getText());
    }

    /**
     * 获取方法名称（方法注释第一行）
     *
     * @param psiMethod 方法对象
     * @return 方法名称
     */
    public String getMethodTitle(PsiMethod psiMethod) {
        String desc = getMethodDesc(psiMethod);
        return StringUtils.isEmpty(desc) ? UNKNOWN_METHOD : desc.split("\n")[0];
    }

    /**
     * 获取方法描述（方法注释体）
     *
     * @param psiMethod 方法对象
     * @return 方法描述
     */
    public String getMethodDesc(PsiMethod psiMethod) {
        PsiDocComment docComment = psiMethod.getDocComment();
        return docComment == null ? null : TextUtil.getCommentFromCommonJavaDoc(docComment.getText());
    }

    /**
     * 获取方法参数解析成类节点列表
     *
     * @param psiMethod 方法对象
     * @return 类节点列表
     */
    public List<ClassNode> getParameters(PsiMethod psiMethod) {
        // 获取注释
        PsiDocComment docComment = psiMethod.getDocComment();
        Map<String, String> paramDesc = new HashMap<>();
        if (docComment != null) {
            PsiDocTag[] paramTags = docComment.findTagsByName(PARAM_TAG);
            if (paramTags.length != 0) {
                Arrays.stream(paramTags).forEach(t -> {
                    PsiElement[] tagElements = t.getDataElements();
                    paramDesc.put(tagElements[0].getText(), tagElements.length > 1 ? tagElements[1].getText() : null);
                });
            }
        }

        PsiParameterList parameterList = psiMethod.getParameterList();
        PsiParameter[] parameters = parameterList.getParameters();

        List<ClassNode> params = new ArrayList<>();
        for (PsiParameter parameter : parameters) {
            PsiType paramType = parameter.getType();
            if (!PsiParseUtil.isAllowedInConfig(paramType, settingAdaptor)) {
                continue;
            }
            String paramName = parameter.getName();

            // 解析参数类型
            ClassNode classNode = PsiParseUtil.parse2Java(paramType);
            // 获取参数注释（覆盖类注释）
            String desc = paramDesc.get(paramName);
            if (desc != null && (desc.endsWith("true") || desc.endsWith("false"))) {
                String[] arr = desc.split(" ");
                desc = String.join(" ", Arrays.copyOfRange(arr, 0, arr.length - 1));
                boolean required = Boolean.parseBoolean(arr[arr.length - 1]);
                classNode.setRequired(required);
            }
            classNode.setDesc(desc);
            classNode.setName(paramName);
            params.add(classNode);
        }

        return params;
    }

    /**
     * 获取方法返回值对象解析成类节点
     *
     * @param psiMethod 方法对象
     * @return 返回值类节点
     */
    public ClassNode getReturnValue(PsiMethod psiMethod) {
        // 获取注释
        PsiDocComment docComment = psiMethod.getDocComment();
        String returnDesc = null;
        if (docComment != null) {
            PsiDocTag returnTag = docComment.findTagByName(RETURN_TAG);
            if (returnTag != null && returnTag.getDataElements().length > 0) {
                returnDesc = Arrays.stream(returnTag.getDataElements()).map(PsiElement::getText).collect(Collectors.joining()).trim();
            }
        }

        PsiType returnType = psiMethod.getReturnType();
        if (returnType == null) {
            return null;
        }
        // 解析参数类型
        ClassNode classNode = PsiParseUtil.parse2Java(returnType);
        // 获取参数注释（覆盖类注释）
        if (returnDesc != null) {
            classNode.setDesc(returnDesc);
        }
        classNode.setName(returnType.getPresentableText());

        return classNode;
    }
}
