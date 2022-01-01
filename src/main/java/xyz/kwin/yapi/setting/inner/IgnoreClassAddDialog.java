package xyz.kwin.yapi.setting.inner;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import xyz.kwin.yapi.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author kwin
 * @since 2021/12/27 5:50 下午
 */
public class IgnoreClassAddDialog extends DialogWrapper {
    private JPanel mainPanel;
    private JTextField classRefName;

    public IgnoreClassAddDialog() {
        super(false);
        init();
        setTitle("请输入类全限定名");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        if (StringUtils.isEmpty(classRefName.getText())) {
            return new ValidationInfo(CommonConstant.Notice.CLASS_NAME_CANNOT_EMPTY, classRefName);
        }
        return super.doValidate();
    }

    public String getClassRefName() {
        String text = classRefName.getText();
        return StringUtils.isEmpty(text) ? null : text;
    }
}
