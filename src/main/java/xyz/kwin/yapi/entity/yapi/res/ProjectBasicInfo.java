package xyz.kwin.yapi.entity.yapi.res;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 项目基础信息
 *
 * @author kwin
 * @since 2021/12/28 11:00 下午
 */
public class ProjectBasicInfo {
    /**
     * 项目id
     */
    @JSONField(name = "_id")
    private Integer id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目基本路径
     */
    @JSONField(name = "basepath")
    private String basePath;
    /**
     * 项目类型
     */
    @JSONField(name = "project_type")
    private String projectType;
    /**
     * 用户id
     */
    private Integer uid;
    /**
     * 组id
     */
    @JSONField(name = "group_id")
    private Integer groupId;

    @JSONField(name = "switch_notice")
    private boolean switchNotice;
    @JSONField(name = "is_mock_open")
    private boolean isMockOpen;
    private boolean strice;
    @JSONField(name = "is_json5")
    private boolean isJson5;
    private String icon;
    private String color;

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

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public boolean isSwitchNotice() {
        return switchNotice;
    }

    public void setSwitchNotice(boolean switchNotice) {
        this.switchNotice = switchNotice;
    }

    public boolean isMockOpen() {
        return isMockOpen;
    }

    public void setMockOpen(boolean mockOpen) {
        isMockOpen = mockOpen;
    }

    public boolean isStrice() {
        return strice;
    }

    public void setStrice(boolean strice) {
        this.strice = strice;
    }

    public boolean isJson5() {
        return isJson5;
    }

    public void setJson5(boolean json5) {
        isJson5 = json5;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
