package xyz.kwin.yapi.entity.yapi.res;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author kwin
 * @since 2021/12/29 10:54 上午
 */
public class InterfaceInfo {
    @JSONField(name = "edit_uid")
    private Integer editUid;
    private String status;
    @JSONField(name = "api_opened")
    private boolean apiOpened;
    @JSONField(name = "_id")
    private Integer id;
    private String method;
    @JSONField(name = "catid")
    private Integer catId;
    private String title;
    private String path;
    @JSONField(name = "project_id")
    private Integer projectId;
    private Integer uid;

    public Integer getEditUid() {
        return editUid;
    }

    public void setEditUid(Integer editUid) {
        this.editUid = editUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isApiOpened() {
        return apiOpened;
    }

    public void setApiOpened(boolean apiOpened) {
        this.apiOpened = apiOpened;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
