package xyz.kwin.yapi.service;

import xyz.kwin.yapi.constant.RequestMethod;
import xyz.kwin.yapi.constant.YApiResEnum;
import xyz.kwin.yapi.entity.node.ClassNode;
import xyz.kwin.yapi.entity.node.InterfaceNode;
import xyz.kwin.yapi.entity.yapi.Converter;
import xyz.kwin.yapi.entity.yapi.req.InterfaceRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 将接口节点转换成请求
 *
 * @author kwin
 * @since 2021/12/28 7:56 下午
 */
public class RequestConverter {

    /**
     * 生成一个可直接发送的接口保存请求
     *
     * @param projectToken  项目token
     * @param catid         分类id
     * @param interfaceNode 接口节点
     * @return 接口请求
     */
    public static InterfaceRequest toInterfaceRequest(String projectToken, Integer catid, InterfaceNode interfaceNode) {
        InterfaceRequest interfaceRequest = new InterfaceRequest();
        interfaceRequest.setToken(projectToken);
        interfaceRequest.setCatId(catid);

        interfaceRequest.setDesc(interfaceNode.getDesc());
        String basePath = interfaceNode.getBasePath() == null ? "" : interfaceNode.getBasePath();
        String path = interfaceNode.getPath();
        interfaceRequest.setPath(basePath + path);
        interfaceRequest.setMethod(interfaceNode.getMethod());

        List<ClassNode> params = interfaceNode.getParams();
        if (RequestMethod.GET == RequestMethod.byMethod(interfaceNode.getMethod())) {
            List<InterfaceRequest.ReqQuery> req_query = new ArrayList<>();
            for (ClassNode param : params) {
                InterfaceRequest.ReqQuery query = new InterfaceRequest.ReqQuery();
                query.setName(param.getName());
                query.setRequired(param.isRequired() ? "1" : "0");
                query.setDesc(param.getDesc());
                query.setType(YApiResEnum.convert(param.getType()).getType());
                req_query.add(query);
            }
            interfaceRequest.setReqQuery(req_query);
        } else {
            interfaceRequest.setReqBodyOther(Converter.toReqBodyOther(params).toJSON());
        }
        interfaceRequest.setResBody(Converter.toPropertyItem(interfaceNode.getRetValue()).toJSON());
        interfaceRequest.setTitle(interfaceNode.getName());
        return interfaceRequest;
    }
}
