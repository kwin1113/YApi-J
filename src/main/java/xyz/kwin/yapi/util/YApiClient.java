package xyz.kwin.yapi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import xyz.kwin.yapi.constant.YApiOpenAPI;
import xyz.kwin.yapi.entity.yapi.Response;
import xyz.kwin.yapi.exceptions.YApiException;
import xyz.kwin.yapi.setting.Setting;
import xyz.kwin.yapi.setting.Settings;
import xyz.kwin.yapi.entity.yapi.req.CategoryAddRequest;
import xyz.kwin.yapi.entity.yapi.req.InterfaceRequest;
import xyz.kwin.yapi.entity.yapi.res.CatMenu;
import xyz.kwin.yapi.entity.yapi.res.InterfaceList;
import xyz.kwin.yapi.entity.yapi.res.InterfaceSave;
import xyz.kwin.yapi.entity.yapi.res.ProjectBasicInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * YApi操作类
 *
 * @author kwin
 * @since 2021/12/13 7:13 下午
 */
@SuppressWarnings("unused")
public class YApiClient {

    /**
     * 获取项目基本信息
     *
     * @param token 项目token
     * @return 项目信息响应
     */
    public static Response<ProjectBasicInfo> getProjectBasicInfo(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        HttpGet httpGet = HttpUtil.httpGet(YApiOpenAPI.PROJECT_BASIC_INFO.with(getUrl()), params);
        String res = HttpUtil.execute(httpGet);

        JSONObject resObject = JSON.parseObject(res);
        if (resObject.getInteger("errcode") != 0) {
            return new Response<>(resObject.getInteger("errcode"), resObject.getString("errmsg"));
        }
        return JSON.parseObject(res, new TypeReference<>() {
        });
    }

    /**
     * 新增接口分类 TODO
     *
     * @param request 新增请求
     * @return 响应
     */
    public static String addCategory(CategoryAddRequest request) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HttpPost httpPost = HttpUtil.httpPost(YApiOpenAPI.ADD_CAT.with(getUrl()), JSON.toJSONString(request), header);
        return HttpUtil.execute(httpPost);
    }

    /**
     * 获取菜单列表
     *
     * @param projectId 项目id
     * @param token     项目token
     * @return 菜单列表
     */
    public static Response<List<CatMenu>> getCatMenu(Integer projectId, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("project_id", projectId);
        params.put("token", token);
        HttpGet httpGet = HttpUtil.httpGet(YApiOpenAPI.GET_CAT_MENU.with(getUrl()), params);
        String res = HttpUtil.execute(httpGet);

        JSONObject resObject = JSON.parseObject(res);
        if (resObject.getInteger("errcode") != 0) {
            return new Response<>(resObject.getInteger("errcode"), resObject.getString("errmsg"));
        }
        return JSON.parseObject(res, new TypeReference<>() {
        });
    }

    /**
     * 获取接口数据 TODO
     *
     * @param id    接口id
     * @param token 项目token
     * @return 接口数据
     */
    public static String getInterface(Integer id, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("token", token);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        HttpGet httpGet = HttpUtil.httpGet(YApiOpenAPI.INTERFACE_GET.with(getUrl()), params, headers);
        return HttpUtil.execute(httpGet);
    }

    /**
     * 获取某个分类下接口列表
     *
     * @param token 项目token
     * @param catid 分类id
     * @param page  当前页
     * @param limit 每页数量
     * @return 接口列表
     */
    public static String listCatInterface(String token, String catid, Integer page, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("catid", catid);
        if (page != null && limit != null) {
            params.put("page", page);
            params.put("limit", limit);
        }

        HttpGet httpGet = HttpUtil.httpGet(YApiOpenAPI.INTERFACE_LIST_CAT.with(getUrl()), params);
        return HttpUtil.execute(httpGet);
    }

    /**
     * 新增或更新接口 TODO
     *
     * @param request 接口请求
     * @return 是否成功
     */
    public static String interfaceAdd(InterfaceRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        HttpPost httpRequest = HttpUtil.httpPost(YApiOpenAPI.INTERFACE_ADD.with(getUrl()), JSON.toJSONString(request), headers);
        return HttpUtil.execute(httpRequest);
    }

    /**
     * 更新接口
     *
     * @param request 接口请求
     * @return 是否成功
     */
    public static String interfaceUpdate(InterfaceRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        HttpPost httpRequest = HttpUtil.httpPost(YApiOpenAPI.INTERFACE_UP.with(getUrl()), JSON.toJSONString(request), headers);
        return HttpUtil.execute(httpRequest);
    }

    /**
     * 新增或更新接口
     *
     * @param request 接口请求
     * @return 是否成功
     */
    public static Response<List<InterfaceSave>> interfaceSave(InterfaceRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        HttpPost httpRequest = HttpUtil.httpPost(YApiOpenAPI.INTERFACE_SAVE.with(getUrl()), JSON.toJSONString(request), headers);
        String res = HttpUtil.execute(httpRequest);
        JSONObject resObject = JSON.parseObject(res);
        if (resObject.getInteger("errcode") != 0) {
            return new Response<>(resObject.getInteger("errcode"), resObject.getString("errmsg"));
        }
        return JSON.parseObject(res, new TypeReference<>() {
        });
    }

    /**
     * 获取接口列表数据
     *
     * @param projectId 项目id
     * @param token     项目token
     * @param page      当前页
     * @param limit     每页数量
     * @return 接口列表数据
     */
    public static Response<InterfaceList> interfaceList(Integer projectId, String token, Integer page, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("project_id", projectId);
        params.put("token", token);
        params.put("page", page);
        params.put("limit", limit);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpGet httpGet = HttpUtil.httpGet(YApiOpenAPI.INTERFACE_LIST.with(getUrl()), params, headers);
        String res = HttpUtil.execute(httpGet);

        JSONObject resObject = JSON.parseObject(res);
        if (resObject.getInteger("errcode") != 0) {
            return new Response<>(resObject.getInteger("errcode"), resObject.getString("errmsg"));
        }
        return JSON.parseObject(res, new TypeReference<>() {
        });
    }

    /**
     * 获取接口菜单列表
     *
     * @param projectId 项目id
     * @param token     项目token
     * @return 接口菜单列表
     */
    public static Response<List<CatMenu>> interfaceListMenu(Integer projectId, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("project_id", projectId);
        params.put("token", token);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        HttpGet httpGet = HttpUtil.httpGet(YApiOpenAPI.INTERFACE_LIST_MENU.with(getUrl()), params, headers);
        String res = HttpUtil.execute(httpGet);

        JSONObject resObject = JSON.parseObject(res);
        if (resObject.getInteger("errcode") != 0) {
            return new Response<>(resObject.getInteger("errcode"), resObject.getString("errmsg"));
        }
        return JSON.parseObject(res, new TypeReference<>() {
        });
    }

    /**
     * 从配置中获取YApi地址
     *
     * @return 配置的YApi地址
     * @throws YApiException 无法获取YApi地址
     */
    private static String getUrl() {
        Setting settings = Settings.getInstance().getState();
        if (settings != null && StringUtils.isNotEmpty(settings.getYapiUrl())) {
            return settings.getYapiUrl();
        }
        throw new YApiException();
    }
}
