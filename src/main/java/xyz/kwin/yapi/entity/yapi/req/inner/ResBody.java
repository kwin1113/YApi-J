package xyz.kwin.yapi.entity.yapi.req.inner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import xyz.kwin.yapi.constant.CommonConstant;
import xyz.kwin.yapi.constant.YApiResEnum;
import xyz.kwin.yapi.entity.JSONAware;
import xyz.kwin.yapi.entity.yapi.req.InterfaceRequest;
import xyz.kwin.yapi.exceptions.YApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * YApi返回JSON
 * 对应{@link InterfaceRequest#getResBody()}字段
 *
 * @author kwin
 * @since 2021/12/28 6:32 下午
 */
public class ResBody implements JSONAware {
    /**
     * YApi字段类型
     */
    private String type;
    /**
     * 字段描述
     */
    private String description;
    /**
     * 子节点字段
     */
    private Map<String, ResBody> properties;
    /**
     * 当前对象下必填字段列表
     */
    private List<String> required;
    /**
     * 数组元素具体对象
     */
    private ResBody items;

    /**
     * 上传数组时，增加了items字段，用来指定数组元素对象的类型
     *
     * @param value 当前已解析的对象
     * @return 充当items字段
     */
    public static ResBody copyArrayItems(ResBody value) {
        if (value.getProperties() == null) {
            throw new YApiException(CommonConstant.Notice.ARRAY_OBJECT_CANNOT_NULL);
        }
        if (!value.getProperties().isEmpty()) {
            ResBody resBody = new ResBody();
            ArrayList<ResBody> valueList = new ArrayList<>(value.getProperties().values());
            // 数组对象有且只有一个property
            ResBody item = valueList.get(0);
            resBody.setType(item.getType());
            resBody.setDescription(item.getDescription());
            resBody.setProperties(item.getProperties());
            resBody.setRequired(item.getRequired());
            return resBody;
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, ResBody> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, ResBody> properties) {
        this.properties = properties;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public ResBody getItems() {
        return items;
    }

    public void setItems(ResBody items) {
        this.items = items;
    }

    @Override
    public String toJSON() {
        if (YApiResEnum.byType(this.type) == YApiResEnum.NULL) {
            return null;
        }
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
