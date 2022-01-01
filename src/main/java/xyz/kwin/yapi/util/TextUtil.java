package xyz.kwin.yapi.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 文本工具
 *
 * @author kwin
 * @since 2021/12/25 9:01 下午
 */
public class TextUtil {

    /**
     * 从psi注释对象中获取注释文本
     * @param text 文本
     * @return 注释描述对象
     */
    public static String getCommentFromCommonJavaDoc(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        text = text.replace("/*", "").replace("*/", "").replace("*", "");
        return text.split("@")[0].trim();
    }

}
