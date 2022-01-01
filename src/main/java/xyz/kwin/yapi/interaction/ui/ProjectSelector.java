package xyz.kwin.yapi.interaction.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.SimpleListCellRenderer;
import xyz.kwin.yapi.setting.Setting;
import xyz.kwin.yapi.setting.SettingAdaptor;
import xyz.kwin.yapi.setting.Settings;
import xyz.kwin.yapi.setting.vo.ProjectVO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 项目选择器
 *
 * @author kwin
 * @since 2021/12/29 4:23 下午
 */
public class ProjectSelector extends DialogWrapper {
    private JPanel mainPanel;
    private JComboBox<ProjectVO> projectComboBox;
    private CollectionComboBoxModel<ProjectVO> projectVOCollectionComboBoxModel;

    public ProjectSelector() {
        super(false);
        init();
        setTitle("请选择项目");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        Setting settings = Settings.getInstance().getState();
        SettingAdaptor settingAdaptor = new SettingAdaptor(settings);
        Collection<ProjectVO> projects = settingAdaptor.getProjects().values();
        projectVOCollectionComboBoxModel = new CollectionComboBoxModel<>(new ArrayList<>(projects));
        projectComboBox = new ComboBox<>(projectVOCollectionComboBoxModel);
        projectComboBox.setRenderer(new SimpleListCellRenderer<>() {
            @Override
            public void customize(@NotNull JList<? extends ProjectVO> list, ProjectVO value, int index, boolean selected, boolean hasFocus) {
                String text = value.getValid() ? value.getName() : "「无效」" + value.getToken();
                setText(text);
            }
        });
    }

    public ProjectVO getProject() {
        return projectVOCollectionComboBoxModel.getElementAt(projectComboBox.getSelectedIndex());
    }
}
