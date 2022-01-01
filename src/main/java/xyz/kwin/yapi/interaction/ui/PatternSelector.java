package xyz.kwin.yapi.interaction.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.CollectionComboBoxModel;
import xyz.kwin.yapi.setting.Setting;
import xyz.kwin.yapi.setting.SettingAdaptor;
import xyz.kwin.yapi.setting.Settings;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * 解析规则选择器
 *
 * @author kwin
 * @since 2021/12/29 4:10 下午
 */
public class PatternSelector extends DialogWrapper {
    private JPanel mainPanel;
    private JComboBox<String> patternComboBox;
    private CollectionComboBoxModel<String> patternComboBoxModel;

    public PatternSelector() {
        super(false);
        init();
        setTitle("请选择解析规则");
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        Setting settings = Settings.getInstance().getState();
        SettingAdaptor settingAdaptor = new SettingAdaptor(settings);
        Set<String> patternNameSet = settingAdaptor.getPatterns().keySet();
        patternComboBoxModel = new CollectionComboBoxModel<>(new ArrayList<>(patternNameSet));
        patternComboBox = new ComboBox<>(patternComboBoxModel);
    }

    public String getPatternName() {
        return patternComboBoxModel.getElementAt(patternComboBox.getSelectedIndex());
    }
}
