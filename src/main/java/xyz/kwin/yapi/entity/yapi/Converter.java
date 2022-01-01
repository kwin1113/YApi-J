package xyz.kwin.yapi.entity.yapi;

import xyz.kwin.yapi.constant.YApiResEnum;
import xyz.kwin.yapi.entity.node.ClassNode;
import xyz.kwin.yapi.entity.yapi.req.inner.ReqBodyOther;
import xyz.kwin.yapi.entity.yapi.req.inner.ResBody;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将类节点转换为YApi请求JSON
 *
 * @author kwin
 * @since 2021/12/28 6:29 下午
 */
public class Converter {

    private static final String LIST_OR_TREE_NOTICE = "(与父节点相同)";

    /**
     * 将类节点转换为YApi字段格式
     *
     * @param node 解析出来的类节点（参数中的单个对象/方法返回值）
     * @return YApi字段格式对象
     */
    public static ResBody toPropertyItem(ClassNode node) {
        ResBody resBody = new ResBody();

        // yapi接口返回值type
        YApiResEnum typeEnum = YApiResEnum.convert(node.getType());
        resBody.setType(typeEnum.getType());
        resBody.setDescription(node.getDesc());
        switch (typeEnum) {
            case NULL:
                break;
            // 四种类型是基础类，无法创建下级节点
            case STRING:
            case NUMBER:
            case BOOLEAN:
            case INTEGER:
                resBody.setDescription(node.getDesc());
                break;
            case OBJECT:
            case ARRAY:
                Map<String, ResBody> properties = new HashMap<>();
                List<String> required = new ArrayList<>();
                // Map集合当作object处理，否则是普通对象
                List<ClassNode> fields = new ArrayList<>();
                if (node.isCustomGeneric()) {
                    fields.addAll(node.getFields());
                } else if (node.isCollection()) {
                    fields.addAll(node.getCollectionElements());
                } else if (node.getFields() != null) {
                    fields.addAll(node.getFields());
                }
                if (CollectionUtils.isNotEmpty(fields)) {
                    for (ClassNode field : fields) {
                        String fieldKey = field.getName();
                        ResBody resField = toPropertyItem(field);
                        if (field.isRequired()) {
                            required.add(fieldKey);
                        }
                        // 如果是链表或者树在注释里标记一下
                        if (field.isListOrTree()) {
                            String description = resField.getDescription() == null ? "" : resField.getDescription();
                            resField.setDescription(LIST_OR_TREE_NOTICE + description);
                        }
                        properties.put(fieldKey, resField);
                    }
                }
                required = CollectionUtils.isEmpty(required) ? null : required;
                properties = MapUtils.isEmpty(properties) ? null : properties;
                resBody.setRequired(required);
                resBody.setProperties(properties);
                if (typeEnum == YApiResEnum.ARRAY) {
                    resBody.setItems(ResBody.copyArrayItems(resBody));
                    resBody.setProperties(null);
                }
                break;
            default:
        }
        return resBody;
    }

    /**
     * 将请求参数封装成YApi请求体格式
     *
     * @param params 接口请求参数
     * @return 请求对象体
     */
    public static ReqBodyOther toReqBodyOther(List<ClassNode> params) {
        ReqBodyOther reqJSONValue = new ReqBodyOther();

        for (ClassNode param : params) {
            reqJSONValue.addReqValue(param.getName(), toPropertyItem(param));
            if (param.isRequired()) {
                reqJSONValue.addRequired(param.getName());
            }
        }

        return reqJSONValue;
    }
}
