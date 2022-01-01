package xyz.kwin.yapi.entity.node;

import java.util.List;

/**
 * 类节点
 *
 * @author kwin
 * @since 2021/12/26 6:54 下午
 */
public class ClassNode {
    /**
     * 类名/参数名
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 全限定名
     */
    private String qualifiedType;
    /**
     * 参数/类注释
     */
    private String desc;
    /**
     * 子属性
     */
    private List<ClassNode> fields;
    /**
     * 是否为数组
     */
    private boolean isArray;
    /**
     * 是否是集合类型（不支持自定义集合）
     */
    private boolean isCollection;
    /**
     * 集合元素类型
     */
    private List<ClassNode> collectionElements;
    /**
     * 是否是自定义泛型
     */
    private boolean isCustomGeneric;
    /**
     * 是否必须
     */
    private boolean isRequired;
    /**
     * 是否和父节点相同，即链表/树结构
     */
    private boolean listOrTree;

    public ClassNode() {
    }

    public ClassNode(String name, String type, String qualifiedType) {
        this.name = name;
        this.type = type;
        this.qualifiedType = qualifiedType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQualifiedType() {
        return qualifiedType;
    }

    public void setQualifiedType(String qualifiedType) {
        this.qualifiedType = qualifiedType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ClassNode> getFields() {
        return fields;
    }

    public void setFields(List<ClassNode> fields) {
        this.fields = fields;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public List<ClassNode> getCollectionElements() {
        return collectionElements;
    }

    public void setCollectionElements(List<ClassNode> collectionElements) {
        this.collectionElements = collectionElements;
    }

    public boolean isCustomGeneric() {
        return isCustomGeneric;
    }

    public void setCustomGeneric(boolean customGeneric) {
        isCustomGeneric = customGeneric;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isListOrTree() {
        return listOrTree;
    }

    public void setListOrTree(boolean listOrTree) {
        this.listOrTree = listOrTree;
    }
}
