package xyz.kwin.yapi.setting.vo;

import xyz.kwin.yapi.entity.PropertyLocator;

import java.io.Serializable;

/**
 * 接口属性配置
 *
 * @author kwin
 * @since 2021/12/28 3:29 下午
 */
public class Pattern implements Serializable {
    private String pattern;
    private PropertyLocator basePathLocator;
    private PropertyLocator pathLocator;
    private PropertyLocator methodLocator;

    public Pattern() {
    }

    public Pattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public PropertyLocator getBasePathLocator() {
        return basePathLocator;
    }

    public void setBasePathLocator(PropertyLocator basePathLocator) {
        this.basePathLocator = basePathLocator;
    }

    public PropertyLocator getPathLocator() {
        return pathLocator;
    }

    public void setPathLocator(PropertyLocator pathLocator) {
        this.pathLocator = pathLocator;
    }

    public PropertyLocator getMethodLocator() {
        return methodLocator;
    }

    public void setMethodLocator(PropertyLocator methodLocator) {
        this.methodLocator = methodLocator;
    }
}
