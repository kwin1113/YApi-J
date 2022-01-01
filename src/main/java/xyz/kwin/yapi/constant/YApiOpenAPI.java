package xyz.kwin.yapi.constant;

/**
 * YApi开放接口
 * https://hellosean1025.github.io/yapi/openapi.html
 *
 * @author kwin
 * @since 2021/12/13 7:47 下午
 */
public enum YApiOpenAPI {
    // 获取项目基本信息
    PROJECT_BASIC_INFO("/api/project/get", "GET"),
    // 新增接口分类
    ADD_CAT("/api/interface/add_cat", "POST"),
    // 获取菜单列表
    GET_CAT_MENU("/api/interface/getCatMenu", "GET"),
    // 服务端数据导入
    IMPORT_DATA("/api/open/import_data", "POST"),
    // 获取接口数据
    INTERFACE_GET("/api/interface/get", "GET"),
    // 获取某个分类下接口列表
    INTERFACE_LIST_CAT("/api/interface/list_cat", "GET"),
    // 新增接口
    INTERFACE_ADD("/api/interface/add", "POST"),
    // 新增或更新接口
    INTERFACE_SAVE("/api/interface/save", "POST"),
    // 获取接口列表数据
    INTERFACE_LIST("/api/interface/list", "GET"),
    // 更新接口
    INTERFACE_UP("/api/interface/up", "POST"),
    // 获取接口菜单列表
    INTERFACE_LIST_MENU("/api/interface/list_menu", "GET"),
    ;

    private final String path;
    private final String method;

    YApiOpenAPI(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public String with(String url) {
        return url + path;
    }

}
