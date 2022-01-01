package xyz.kwin.yapi.entity;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import xyz.kwin.yapi.constant.LocateTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 属性定位
 *
 * @author kwin
 * @since 2021/12/28 3:19 下午
 */
public class PropertyLocator implements Serializable {
    private static final String SEPARATOR = "/";
    /**
     * 定位类型
     */
    private LocateTypeEnum locateType;
    /**
     * 定位标签
     * 当locateType为0时，为注释tag
     * 当locateType为1时，为注解属性
     */
    private String location;
    /**
     * 对应注解
     */
    private String annotation;
    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 定位器获取字段
     *
     * @param docComment  注释
     * @param annotations 注解
     * @return 对应字段
     */
    public String getValue(PsiDocComment docComment, PsiAnnotation[] annotations) {
        if (StringUtils.isEmpty(location)) {
            return defaultValue;
        }
        List<String> valueList = new ArrayList<>();
        if (getLocateType() == LocateTypeEnum.FROM_COMMENT && docComment != null) {
            // get from docComment
            String[] tagArr = getLocation().split(SEPARATOR);
            for (String tagName : tagArr) {
                PsiDocTag tag = docComment.findTagByName(tagName);
                if (tag != null && tag.getDataElements().length > 0) {
                    String val = Arrays.stream(tag.getDataElements()).map(PsiElement::getText).collect(Collectors.joining()).trim();
                    valueList.add(val);
                }
            }
        } else if (getLocateType() == LocateTypeEnum.FROM_ANNOTATION && annotations.length > 0) {
            // get from annotations
            String annotation = getAnnotation();
            String[] properties = getLocation().split(SEPARATOR);
            for (String property : properties) {
                PsiAnnotation psiAnnotation = Arrays.stream(annotations).filter(a -> a.getQualifiedName() != null && a.getQualifiedName().equals(annotation)).findFirst().orElse(null);
                if (psiAnnotation != null) {
                    for (PsiNameValuePair attribute : psiAnnotation.getParameterList().getAttributes()) {
                        if (Objects.requireNonNull(attribute.getAttributeName()).equals(property)) {
                            String val = Objects.requireNonNull(attribute.getValue()).getText();
                            valueList.add(val);
                            break;
                        }
                    }
                }
            }
        }
        return CollectionUtils.isEmpty(valueList) ? null : String.join("_", valueList);
    }

    public PropertyLocator() {
    }

    public PropertyLocator(LocateTypeEnum locateType, String location, String annotation) {
        this.locateType = locateType;
        this.location = location;
        this.annotation = annotation;
    }

    public LocateTypeEnum getLocateType() {
        return locateType;
    }

    public void setLocateType(LocateTypeEnum locateType) {
        this.locateType = locateType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
