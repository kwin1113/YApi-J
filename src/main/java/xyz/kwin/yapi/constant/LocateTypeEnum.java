package xyz.kwin.yapi.constant;

import xyz.kwin.yapi.entity.PropertyLocator;

/**
 * 属性定位类型
 * {@link PropertyLocator#getLocateType()}
 *
 * @author kwin
 * @since 2021/12/29 3:17 下午
 */
public enum LocateTypeEnum {
    FROM_ANNOTATION("注解"),
    FROM_COMMENT("注释"),
    ;

    /**
     * 定位类型描述
     */
    private final String type;

    LocateTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
