package xyz.kwin.yapi.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * YApi配置持久化配置
 *
 * @author kwin
 * @since 2021/12/25 9:57 下午
 */
@State(name = "Settings", storages = {@Storage(value = "YApi_J_Settings.xml")})
public class Settings implements PersistentStateComponent<Setting> {

    public static Settings getInstance() {
        return ApplicationManager.getApplication().getService(Settings.class);
    }

    private Setting setting;

    @Override
    public @Nullable Setting getState() {
        if (this.setting == null) {
            this.setting = new Setting();
        }
        return this.setting;
    }

    @Override
    public void loadState(@NotNull Setting state) {
        this.setting = state;
    }
}
