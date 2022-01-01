package xyz.kwin.yapi.entity.yapi.req;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * YApi保存类
 *
 * @author kwin
 * @since 2021/12/13 7:18 下午
 */
public class InterfaceRequest {
    /**
     * 项目token 唯一标识
     */
    private String token;
    /**
     * 标题
     */
    private String title;
    /**
     * 分类id
     */
    @JSONField(name = "catid")
    private Integer catId;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 接口路径
     */
    private String path;
    /**
     * 请求参数
     */
    @JSONField(name = "req_query")
    private List<ReqQuery> reqQuery;
    /**
     * 请求头
     */
    @JSONField(name = "req_headers")
    private List<Object> reqHeaders;
    /**
     * 返回参数
     */
    @JSONField(name = "res_body")
    private String resBody;
    /**
     * 请求体表单数据
     */
    @JSONField(name = "req_body_form")
    private List<ReqQuery> reqBodyForm;
    /**
     * 接口描述
     */
    private String desc;
    /**
     * 请求数据body
     */
    @JSONField(name = "req_body_other")
    private String reqBodyOther;

    @SuppressWarnings("FieldCanBeLocal")
    @JSONField(name = "req_body_is_json_schema")
    private final boolean reqBodyIsJsonSchema = true;
    @SuppressWarnings("FieldCanBeLocal")
    @JSONField(name = "req_body_type")
    private final String reqBodyType = "json";
    @SuppressWarnings("FieldCanBeLocal")
    @JSONField(name = "res_body_type")
    private final String resBodyType = "json";
    @SuppressWarnings("FieldCanBeLocal")
    @JSONField(name = "res_body_is_json_schema")
    private final boolean resBodyIsJsonSchema = true;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ReqQuery> getReqQuery() {
        return reqQuery;
    }

    public void setReqQuery(List<ReqQuery> reqQuery) {
        this.reqQuery = reqQuery;
    }

    public List<Object> getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(List<Object> reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }

    public List<ReqQuery> getReqBodyForm() {
        return reqBodyForm;
    }

    public void setReqBodyForm(List<ReqQuery> reqBodyForm) {
        this.reqBodyForm = reqBodyForm;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReqBodyOther() {
        return reqBodyOther;
    }

    public void setReqBodyOther(String reqBodyOther) {
        this.reqBodyOther = reqBodyOther;
    }

    public boolean isReqBodyIsJsonSchema() {
        return reqBodyIsJsonSchema;
    }

    public String getReqBodyType() {
        return reqBodyType;
    }

    public String getResBodyType() {
        return resBodyType;
    }

    public boolean isResBodyIsJsonSchema() {
        return resBodyIsJsonSchema;
    }

    public static class ReqQuery {
        /**
         * 参数名称
         */
        private String name;
        /**
         * 是否必须 0-非必需/1-必须
         */
        private String required;
        /**
         * 示例
         */
        private String example;
        /**
         * 描述
         */
        private String desc;
        /**
         * 类型 text/file
         */
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRequired() {
            return required;
        }

        public void setRequired(String required) {
            this.required = required;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class ReqHeader {
        /**
         * 参数名称
         */
        private String name;
        /**
         * 参数值
         */
        private String value;
        /**
         * 示例
         */
        private String example;
        /**
         * 描述
         */
        private String desc;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
