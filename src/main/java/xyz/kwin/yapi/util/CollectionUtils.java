package xyz.kwin.yapi.util;

import java.util.Collection;

/**
 * 集合工具类
 *
 * @author kwin
 * @since 2021/12/27 7:59 下午
 */
public class CollectionUtils {

    @SuppressWarnings({"rawtypes", "BooleanMethodIsAlwaysInverted"})
    public static boolean equals(Collection a, Collection b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null && b.size() == 0) {
            return true;
        }
        if (b == null && a.size() == 0) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return org.apache.commons.collections.CollectionUtils.isEqualCollection(a, b);
    }
}
