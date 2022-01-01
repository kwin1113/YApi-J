package xyz.kwin.yapi.entity.yapi.req;

import com.alibaba.fastjson.annotation.JSONField;

import javax.annotation.Nullable;

/**
 * 添加分类请求
 *
 * @author kwin
 * @since 2021/12/28 10:45 下午
 */
public class CategoryAddRequest {
    private String name;
    @JSONField(name = "project_id")
    private Integer projectId;
    private String token;
    @Nullable
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Nullable
    public String getDesc() {
        return desc;
    }

    public void setDesc(@Nullable String desc) {
        this.desc = desc;
    }
}
