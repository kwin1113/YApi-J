package xyz.kwin.yapi.setting;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.kwin.yapi.constant.CommonConstant;

import javax.swing.*;

/**
 * 配置
 *
 * @author kwin
 * @since 2021/12/27 11:25 上午
 */
public class SettingConfigurable implements SearchableConfigurable {

    private SettingUI settingUI;

    @Override
    public @NotNull @NonNls String getId() {
        return CommonConstant.PLUGIN_ID;
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return CommonConstant.PLUGIN_NAME;
    }

    @Override
    public @Nullable JComponent createComponent() {
        settingUI = new SettingUI();
        return settingUI.getMainPanel();
    }

    @Override
    public boolean isModified() {
        return settingUI != null && settingUI.isModified();
    }

    @Override
    public void apply() {
        settingUI.apply();
    }
}
