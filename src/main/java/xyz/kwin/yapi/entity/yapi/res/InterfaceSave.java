package xyz.kwin.yapi.entity.yapi.res;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author kwin
 * @since 2021/12/29 5:16 下午
 */
public class InterfaceSave {
    @JSONField(name = "_id")
    private Integer id;
    @JSONField(name = "res_body")
    private String resBody;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }
}
