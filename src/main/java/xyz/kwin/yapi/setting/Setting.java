package xyz.kwin.yapi.setting;

import org.apache.commons.lang3.StringUtils;
import xyz.kwin.yapi.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置类
 *
 * @author kwin
 * @since 2021/12/29 8:53 下午
 */
public class Setting implements Serializable {
    private String yapiUrl;
    private List<String> ignoreClasses = new ArrayList<>();
    private String patternJson;
    private String projectJson;

    public String getYapiUrl() {
        return yapiUrl;
    }

    public void setYapiUrl(String yapiUrl) {
        this.yapiUrl = yapiUrl;
    }

    public List<String> getIgnoreClasses() {
        return ignoreClasses;
    }

    public void setIgnoreClasses(List<String> ignoreClasses) {
        this.ignoreClasses = ignoreClasses;
    }

    public String getPatternJson() {
        return patternJson;
    }

    public void setPatternJson(String patternJson) {
        this.patternJson = patternJson;
    }

    public String getProjectJson() {
        return projectJson;
    }

    public void setProjectJson(String projectJson) {
        this.projectJson = projectJson;
    }

    public boolean isModified(Setting curConfig) {
        if (curConfig == null) {
            return false;
        } else {
            SettingAdaptor settingAdaptor = new SettingAdaptor(this);
            SettingAdaptor curSettingAdaptor = new SettingAdaptor(curConfig);
            return !StringUtils.equals(this.yapiUrl, curConfig.getYapiUrl()) ||
                    !CollectionUtils.equals(this.ignoreClasses, curConfig.getIgnoreClasses()) ||
                    !CollectionUtils.equals(settingAdaptor.getPatterns().keySet(), curSettingAdaptor.getPatterns().keySet()) ||
                    !CollectionUtils.equals(settingAdaptor.getProjects().keySet(), curSettingAdaptor.getProjects().keySet());
        }
    }
}
