package xyz.kwin.yapi.setting.inner;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import xyz.kwin.yapi.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author kwin
 * @since 2021/12/29 11:57 上午
 */
public class ProjectAddDialog extends DialogWrapper {
    private JPanel mainPanel;
    private JTextField tokenField;

    public ProjectAddDialog() {
        super(false);
        init();
        setTitle("新增项目");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        if (StringUtils.isEmpty(tokenField.getText())) {
            return new ValidationInfo(CommonConstant.Notice.TOKEN_CANNOT_EMPTY, tokenField);
        }
        return super.doValidate();
    }

    public String getToken() {
        String text = tokenField.getText();
        return StringUtils.isEmpty(text) ? null : text;
    }
}
