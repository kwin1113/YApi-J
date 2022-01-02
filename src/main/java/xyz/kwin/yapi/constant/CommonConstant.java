package xyz.kwin.yapi.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 常量
 *
 * @author kwin
 * @since 2021/12/27 3:20 下午
 */
public interface CommonConstant {
    String PLUGIN_NAME = "YApi-J";
    String PLUGIN_ID = "YApi-J.id";

    /**
     * 支持的Java类型（递归解析的终止条件）
     * 如果java包下不在此集合中，则返回空节点，终止解析
     * 什么Date之类的当参数全是邪教 @^@ (YApi不支持)
     */
    Set<String> SUPPORTED_JAVA_TYPE = Sets.newHashSet("java.util.ArrayDeque",
            "java.util.ArrayList", "java.util.Collection", "java.util.Deque", "java.util.HashMap",
            "java.util.HashSet", "java.util.Hashtable", "java.util.LinkedHashMap", "java.util.LinkedHashSet",
            "java.util.LinkedList", "java.util.List", "java.util.Map", "java.util.PriorityQueue", "java.util.Queue",
            "java.util.Set", "java.util.SortedMap", "java.util.SortedSet", "java.util.Stack", "java.util.TreeMap", "java.util.TreeSet");

    interface Notice {
        String IGNORE_CLASSES_EMPTY_NOTICE = "配置类全限定名列表进行过滤，列表中的类不做解析";
        String PROJECT_LIST_EMPTY_NOTICE = "请配置项目token";
        String PATTERN_LIST_EMPTY_NOTICE = "请配置解析规则模板";

        String NOTICE_TITLE = "提示信息";
        String ERROR_TITLE = "错误信息";
        String UNKNOWN_TITLE = "未知异常！";

        String ERROR_TARGET = "请在目标类或目标方法上执行";
        String CONFIG_MISSING = "请先进行配置！";
        String NO_VALID_METHOD = "没有可以解析的方法！";

        String UPLOAD_CONFIRM = "确认上传信息";
        String SELECTED_UPLOAD_CONFIG = "解析规则：%s\n上传项目：%s\n";
        String SELECTED_CLASS = "类名：%s";
        String SELECTED_METHOD = "方法名：%s";

        String UPLOAD_SUCCESS = "接口上传成功";
        String UPLOAD_COMPLETE = "接口上传完毕，失败列表：";
        String FAILED_METHOD_REASON = "%s[%s]";

        String CLASS_NAME_CANNOT_EMPTY = "类名不能为空";

        String PATTERN_CANNOT_EMPTY = "规则名称不能为空";
        String LOCATION_CANNOT_EMPTY = "定位字段不能为空";

        String TOKEN_CANNOT_EMPTY = "token不能为空";

        String YAPI_CAT_MENU_EMPTY = "项目接口分类列表为空，请创建至少一个分类";

        String ARRAY_OBJECT_CANNOT_NULL = "数组/集合元素不能为空，请检查集合泛型等";
    }
}
