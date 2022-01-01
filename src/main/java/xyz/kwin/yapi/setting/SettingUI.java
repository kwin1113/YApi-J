package xyz.kwin.yapi.setting;

import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;
import xyz.kwin.yapi.constant.CommonConstant;
import xyz.kwin.yapi.service.ProjectService;
import xyz.kwin.yapi.setting.inner.IgnoreClassAddDialog;
import xyz.kwin.yapi.setting.inner.PatternConfigDialog;
import xyz.kwin.yapi.setting.inner.ProjectAddDialog;
import xyz.kwin.yapi.setting.vo.Pattern;
import xyz.kwin.yapi.setting.vo.ProjectVO;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kwin
 * @since 2021/12/27 12:04 下午
 */
@SuppressWarnings("unused")
public class SettingUI {

    private final SettingAdaptor config = SettingAdaptor.getInstance();
    private final ProjectService projectService = ProjectService.getInstance();

    private JPanel mainPanel;
    private JTextField yApiUrlInput;
    private JPanel ignoreClassesListPanel;
    private JPanel yapiUrlPanel;
    private JPanel ignoreClassesPanel;
    private JBList<String> ignoreClassesList;
    private CollectionListModel<String> ignoreClassesModel;
    private JPanel projectPanel;
    private JPanel projectListPanel;
    private JBList<ProjectVO> projectList;
    private CollectionListModel<ProjectVO> projectListModel;
    private JPanel patternPanel;
    private JPanel patternListPanel;
    private JBList<Pattern> patternList;
    private CollectionListModel<Pattern> patternListModel;

    public SettingUI() {
        initUI();
    }

    private void initUI() {
        loadSettings();
    }

    private void loadSettings() {
        if (!config.valid()) {
            return;
        }

        // YApiUrl
        String yapiUrl = config.getYapiUrl();
        yApiUrlInput.setText(yapiUrl);

        // ignoreClasses
        List<String> ignoreClasses = config.getIgnoreClasses();
        ignoreClassesModel.removeAll();
        ignoreClassesModel.addAll(ignoreClassesModel.getSize(), new ArrayList<>(ignoreClasses));
        // projectList
        refreshProjects();
        Map<String, ProjectVO> projects = config.getProjects();
        projectListModel.removeAll();
        projectListModel.addAll(projectListModel.getSize(), new ArrayList<>(projects.values()));
        // pattern
        Map<String, Pattern> patternMap = config.getPatterns();
        patternListModel.removeAll();
        patternListModel.addAll(patternListModel.getSize(), new ArrayList<>(patternMap.values()));
    }

    private void refreshProjects() {
        try {
            Map<String, ProjectVO> projects = config.getProjects();
            ArrayList<String> projectTokens = new ArrayList<>(projects.keySet());
            List<ProjectVO> freshProjects = projectService.getProjectListNoEx(projectTokens);
            config.setProjects(freshProjects);
        } catch (Exception ignored) {
            Messages.showInfoMessage("YApi连接失败", CommonConstant.Notice.NOTICE_TITLE);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public boolean isModified() {
        Setting config = Settings.getInstance().getState();
        if (config == null) {
            return true;
        }
        Setting curConfig = processConfig();
        return config.isModified(curConfig);
    }

    public void apply() {
        config.setYapiUrl(yApiUrlInput.getText());
        config.setIgnoreClasses(new ArrayList<>(ignoreClassesModel.getItems()));
        config.setProjects(new ArrayList<>(projectListModel.getItems()));
        config.setPatterns(new ArrayList<>(patternListModel.getItems()));
    }

    private Setting processConfig() {
        SettingAdaptor settingAdaptor = SettingAdaptor.empty();
        settingAdaptor.setYapiUrl(yApiUrlInput.getText());
        settingAdaptor.setIgnoreClasses(ignoreClassesModel.getItems());
        settingAdaptor.setProjects(projectListModel.getItems());
        settingAdaptor.setPatterns(patternListModel.getItems());
        return settingAdaptor.getSetting();
    }

    private void createUIComponents() {
        // ignoreClasses
        ignoreClassesModel = new CollectionListModel<>();
        ignoreClassesList = new JBList<>(ignoreClassesModel);
        ignoreClassesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ignoreClassesList.setEmptyText(CommonConstant.Notice.IGNORE_CLASSES_EMPTY_NOTICE);
        ToolbarDecorator ignoreDecorator = ToolbarDecorator.createDecorator(ignoreClassesList);
        ignoreDecorator.setAddAction(r -> {
            IgnoreClassAddDialog ignoreClassAddDialog = new IgnoreClassAddDialog();
            if (ignoreClassAddDialog.showAndGet()) {
                String classRefName = ignoreClassAddDialog.getClassRefName();
                ignoreClassesModel.add(classRefName);
            }
        });
        ignoreDecorator.setRemoveAction(r -> {
            int selectedIndex = ignoreClassesList.getSelectedIndex();
            ignoreClassesModel.remove(selectedIndex);
        });

        ignoreClassesListPanel = ignoreDecorator.createPanel();

        // projectList
        projectListModel = new CollectionListModel<>();
        projectList = new JBList<>(projectListModel);
        projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectList.setEmptyText(CommonConstant.Notice.PROJECT_LIST_EMPTY_NOTICE);
        projectList.setCellRenderer(new SimpleListCellRenderer<>() {
            @Override
            public void customize(@NotNull JList<? extends ProjectVO> list, ProjectVO value, int index, boolean selected, boolean hasFocus) {
                String text = value.getValid() ? value.getName() : "「无效」" + value.getToken();
                setText(text);
            }
        });
        ToolbarDecorator projectDecorator = ToolbarDecorator.createDecorator(projectList);
        projectDecorator.setAddAction(r -> {
            ProjectAddDialog projectAddDialog = new ProjectAddDialog();
            if (projectAddDialog.showAndGet()) {
                String token = projectAddDialog.getToken();
                ProjectVO project = projectService.getProject(token);
                projectListModel.add(project);
            }
        });
        projectDecorator.setRemoveAction(r -> {
            int selectedIndex = projectList.getSelectedIndex();
            projectListModel.remove(selectedIndex);
        });
        projectListPanel = projectDecorator.createPanel();

        // pattern
        patternListModel = new CollectionListModel<>();
        patternList = new JBList<>(patternListModel);
        patternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patternList.setEmptyText(CommonConstant.Notice.PATTERN_LIST_EMPTY_NOTICE);
        patternList.setCellRenderer(new SimpleListCellRenderer<>() {
            @Override
            public void customize(@NotNull JList<? extends Pattern> list, Pattern value, int index, boolean selected, boolean hasFocus) {
                setText(value.getPattern());
            }
        });
        ToolbarDecorator patternDecorator = ToolbarDecorator.createDecorator(patternList);
        patternDecorator.setAddAction(r -> {
            PatternConfigDialog patternConfigDialog = new PatternConfigDialog();
            if (patternConfigDialog.showAndGet()) {
                Pattern pattern = patternConfigDialog.getPattern();
                patternListModel.add(pattern);
            }
        });
//        patternDecorator.setEditAction();
        patternDecorator.setRemoveAction(r -> {
            int selectedIndex = patternList.getSelectedIndex();
            patternListModel.remove(selectedIndex);
        });
        patternListPanel = patternDecorator.createPanel();
    }
}
