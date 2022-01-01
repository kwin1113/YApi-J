package xyz.kwin.yapi.entity.yapi.req.inner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import xyz.kwin.yapi.constant.YApiResEnum;
import xyz.kwin.yapi.entity.JSONAware;
import xyz.kwin.yapi.entity.yapi.req.InterfaceRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * YApi请求JSON
 * 对应{@link InterfaceRequest#getReqBodyOther()}字段
 *
 * @author kwin
 * @since 2021/12/28 7:16 下午
 */
public class ReqBodyOther implements JSONAware {
    /**
     * 必填字段列表
     */
    private List<String> required;
    /**
     * 请求参数
     */
    private Map<String, ResBody> reqValueMap;

    public void addRequired(String paramName) {
        if (required == null) {
            required = new ArrayList<>();
        }
        required.add(paramName);
    }

    public void addReqValue(String name, ResBody resBody) {
        if (reqValueMap == null) {
            reqValueMap = new HashMap<>();
        }
        reqValueMap.put(name, resBody);
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public Map<String, ResBody> getReqValueMap() {
        return reqValueMap;
    }

    public void setReqValueMap(Map<String, ResBody> reqValueMap) {
        this.reqValueMap = reqValueMap;
    }

    @Override
    public String toJSON() {
        ResBody reqJSONValue = new ResBody();
        reqJSONValue.setType(YApiResEnum.OBJECT.getType());

        reqJSONValue.setProperties(reqValueMap);
        reqJSONValue.setRequired(required);
        return JSON.toJSONString(reqJSONValue, SerializerFeature.DisableCircularReferenceDetect);
    }
}
