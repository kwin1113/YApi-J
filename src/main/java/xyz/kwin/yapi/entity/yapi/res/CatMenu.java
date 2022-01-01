package xyz.kwin.yapi.entity.yapi.res;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 接口分类信息
 *
 * @author kwin
 * @since 2021/12/29 9:57 上午
 */
public class CatMenu {
    private Integer index;
    @JSONField(name = "_id")
    private Integer id;
    private String name;
    @JSONField(name = "project_id")
    private Integer projectId;
    private String desc;
    private Integer uid;
    private List<InterfaceInfo> list;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public List<InterfaceInfo> getList() {
        return list;
    }

    public void setList(List<InterfaceInfo> list) {
        this.list = list;
    }
}
