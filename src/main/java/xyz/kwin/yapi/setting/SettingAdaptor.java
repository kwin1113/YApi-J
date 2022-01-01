package xyz.kwin.yapi.setting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import xyz.kwin.yapi.setting.vo.Pattern;
import xyz.kwin.yapi.setting.vo.ProjectVO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置插头
 * 两个Map类型的数据持久化有问题，不知道问题在哪
 * 只能暂时通过String保存，再转换成Map使用
 *
 * @author kwin
 * @since 2021/12/29 11:07 下午
 */
public class SettingAdaptor {

    private Setting setting;

    private SettingAdaptor() {
        this.setting = Settings.getInstance().getState();
    }

    public SettingAdaptor(Setting setting) {
        this.setting = setting;
    }

    public static SettingAdaptor getInstance() {
        return new SettingAdaptor();
    }

    public static SettingAdaptor empty() {
        Setting setting = new Setting();
        return new SettingAdaptor(setting);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean valid() {
        return this.setting != null;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public Setting getSetting() {
        return this.setting;
    }

    public String getYapiUrl() {
        return this.setting.getYapiUrl();
    }

    public void setYapiUrl(String yapiUrl) {
        this.setting.setYapiUrl(yapiUrl);
    }

    public List<String> getIgnoreClasses() {
        return this.setting.getIgnoreClasses();
    }

    public void addIgnoreClass(String ignoreClass) {
        if (this.setting.getIgnoreClasses() == null) {
            setIgnoreClasses(new ArrayList<>());
        }
        this.setting.getIgnoreClasses().add(ignoreClass);
    }

    public void setIgnoreClasses(List<String> ignoreClasses) {
        this.setting.setIgnoreClasses(ignoreClasses);
    }

    public void addProject(ProjectVO project) {
        Map<String, ProjectVO> projects = getProjects();
        projects.put(project.getToken(), project);
        setProjects(projects);
    }

    public void setProjects(List<ProjectVO> projects) {
        Map<String, ProjectVO> map = projects.stream().collect(Collectors.toMap(ProjectVO::getToken, e -> e, (a, b) -> a));
        setProjects(map);
    }

    public void setProjects(Map<String, ProjectVO> projects) {
        this.setting.setProjectJson(JSON.toJSONString(projects));
    }

    public Map<String, ProjectVO> getProjects() {
        String projectJson = this.setting.getProjectJson();
        if (StringUtils.isEmpty(projectJson)) {
            String initJson = JSON.toJSONString(new HashMap<>());
            this.setting.setProjectJson(initJson);
            projectJson = initJson;
        }
        return JSON.parseObject(projectJson, new TypeReference<>() {
        });
    }

    public void addPattern(Pattern pattern) {
        Map<String, Pattern> patterns = getPatterns();
        patterns.put(pattern.getPattern(), pattern);
        setPatterns(patterns);
    }

    public void setPatterns(List<Pattern> patterns) {
        Map<String, Pattern> map = patterns.stream().collect(Collectors.toMap(Pattern::getPattern, e -> e, (a, b) -> a));
        setPatterns(map);
    }

    public void setPatterns(Map<String, Pattern> patterns) {
        this.setting.setPatternJson(JSON.toJSONString(patterns));
    }

    public Map<String, Pattern> getPatterns() {
        String patternJson = this.setting.getPatternJson();
        if (StringUtils.isEmpty(patternJson)) {
            String initJson = JSON.toJSONString(new HashMap<>());
            this.setting.setPatternJson(initJson);
            patternJson = initJson;
        }
        return JSON.parseObject(patternJson, new TypeReference<>() {
        });
    }
}
