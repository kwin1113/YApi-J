package xyz.kwin.yapi.setting.vo;

import java.io.Serializable;

/**
 * 项目UI对象
 *
 * @author kwin
 * @since 2021/12/29 11:37 上午
 */
public class ProjectVO implements Serializable {
    /**
     * 项目token，需手动配置
     */
    private String token;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 是否有效
     */
    private Boolean valid = Boolean.FALSE;

    public ProjectVO() {
    }

    public ProjectVO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
