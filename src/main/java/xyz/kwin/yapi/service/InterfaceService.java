package xyz.kwin.yapi.service;

import com.intellij.openapi.application.ApplicationManager;
import xyz.kwin.yapi.entity.node.InterfaceNode;
import xyz.kwin.yapi.entity.yapi.Response;
import xyz.kwin.yapi.entity.yapi.req.InterfaceRequest;
import xyz.kwin.yapi.entity.yapi.res.InterfaceSave;
import xyz.kwin.yapi.util.YApiClient;

import java.util.List;

/**
 * 接口服务
 *
 * @author kwin
 * @since 2021/12/29 5:08 下午
 */
public class InterfaceService {

    public static InterfaceService getInstance() {
        return ApplicationManager.getApplication().getService(InterfaceService.class);
    }

    /**
     * 上传接口到指定项目
     *
     * @param projectToken  项目token
     * @param catid         分类id
     * @param interfaceNode 接口节点
     * @return 是否成功
     */
    public Response<Object> upload(String projectToken, Integer catid, InterfaceNode interfaceNode) {
        InterfaceRequest interfaceRequest = RequestConverter.toInterfaceRequest(projectToken, catid, interfaceNode);
        Response<List<InterfaceSave>> response = YApiClient.interfaceSave(interfaceRequest);
        return new Response<>(response.getErrcode(), response.getErrmsg());
    }
}
