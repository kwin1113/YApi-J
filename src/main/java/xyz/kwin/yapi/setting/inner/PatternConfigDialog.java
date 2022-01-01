package xyz.kwin.yapi.setting.inner;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.SimpleListCellRenderer;
import xyz.kwin.yapi.constant.CommonConstant;
import xyz.kwin.yapi.constant.LocateTypeEnum;
import xyz.kwin.yapi.constant.RequestMethod;
import xyz.kwin.yapi.entity.PropertyLocator;
import xyz.kwin.yapi.setting.vo.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kwin
 * @since 2021/12/29 2:56 下午
 */
public class PatternConfigDialog extends DialogWrapper {
    private JPanel mainPanel;
    private JTextField patternName;
    private JComboBox<LocateTypeEnum> pathLocateType;
    private JTextField pathAnnotation;
    private JTextField pathLocation;
    private JComboBox<LocateTypeEnum> methodLocateType;
    private JTextField methodLocation;
    private JTextField methodAnnotation;
    private JPanel pathPanel;
    private JPanel methodPanel;
    private JPanel basePathPanel;
    private JComboBox<LocateTypeEnum> basePathLocateType;
    private JTextField basePathLocation;
    private JTextField basePathAnnotation;
    private CollectionComboBoxModel<LocateTypeEnum> basePathLocateTypeModel;
    private CollectionComboBoxModel<LocateTypeEnum> pathLocateTypeModel;
    private CollectionComboBoxModel<LocateTypeEnum> methodLocateTypeModel;

    public PatternConfigDialog() {
        super(false);
        init();
        setTitle("配置解析规则");
        patternName.setToolTipText("解析规则唯一标识符");
        basePathLocation.setToolTipText("定位字段：注解模式为注解属性，注释模式则为注释tag");
        pathLocation.setToolTipText("定位字段：注解模式为注解属性，注释模式则为注释tag");
        methodLocation.setToolTipText("定位字段：注解模式为注解属性，注释模式则为注释tag");
        basePathAnnotation.setToolTipText("请填入注解类的全限定名");
        pathAnnotation.setToolTipText("请填入注解类的全限定名");
        methodAnnotation.setToolTipText("请填入注解类的全限定名");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    protected @NotNull List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> validationInfoList = new ArrayList<>();
        if (StringUtils.isEmpty(patternName.getText())) {
            validationInfoList.add(new ValidationInfo(CommonConstant.Notice.PATTERN_CANNOT_EMPTY, patternName));
        }
        if (StringUtils.isEmpty(pathLocation.getText())) {
            validationInfoList.add(new ValidationInfo(CommonConstant.Notice.LOCATION_CANNOT_EMPTY, pathLocation));
        }
        validationInfoList.addAll(super.doValidateAll());
        return validationInfoList;
    }

    public Pattern getPattern() {
        Pattern pattern = new Pattern(patternName.getText());
        pattern.setBasePathLocator(getBasePathLocator());
        pattern.setPathLocator(getPathLocator());
        pattern.setMethodLocator(getMethodLocator());
        return pattern;
    }

    private PropertyLocator getBasePathLocator() {
        PropertyLocator basePathLocator = new PropertyLocator(basePathLocateTypeModel.getElementAt(basePathLocateType.getSelectedIndex()), basePathLocation.getText(), basePathAnnotation.getText());
        basePathLocator.setDefaultValue("");
        return basePathLocator;
    }

    private PropertyLocator getPathLocator() {
        return new PropertyLocator(pathLocateTypeModel.getElementAt(pathLocateType.getSelectedIndex()), pathLocation.getText(), pathAnnotation.getText());
    }

    private PropertyLocator getMethodLocator() {
        PropertyLocator methodLocator = new PropertyLocator(methodLocateTypeModel.getElementAt(methodLocateType.getSelectedIndex()), methodLocation.getText(), methodAnnotation.getText());
        methodLocator.setDefaultValue(RequestMethod.POST.getMethod());
        return methodLocator;
    }

    private void adjustTextFieldsInLine(JTextField alwaysShow, JTextField mayHide, boolean hide) {
        int width = alwaysShow.getWidth() + mayHide.getWidth();
        if (hide) {
            alwaysShow.setSize(width, alwaysShow.getHeight());
            mayHide.setSize(0, mayHide.getHeight());
            mayHide.setVisible(false);
        } else {
            width /= 2;
            alwaysShow.setSize(width, alwaysShow.getHeight());
            mayHide.setSize(width, mayHide.getHeight());
            mayHide.setVisible(true);
        }
    }

    private void createUIComponents() {
        basePathLocateTypeModel = new CollectionComboBoxModel<>(new ArrayList<>(Arrays.asList(LocateTypeEnum.values())));
        pathLocateTypeModel = new CollectionComboBoxModel<>(new ArrayList<>(Arrays.asList(LocateTypeEnum.values())));
        methodLocateTypeModel = new CollectionComboBoxModel<>(new ArrayList<>(Arrays.asList(LocateTypeEnum.values())));
        basePathLocateType = new ComboBox<>(basePathLocateTypeModel);
        pathLocateType = new ComboBox<>(pathLocateTypeModel);
        methodLocateType = new ComboBox<>(methodLocateTypeModel);
        basePathLocateType.setRenderer(new SimpleListCellRenderer<>() {
            @Override
            public void customize(@NotNull JList<? extends LocateTypeEnum> list, LocateTypeEnum value, int index, boolean selected, boolean hasFocus) {
                setText(value.getType());
            }
        });
        basePathLocateType.addActionListener(event -> {
            LocateTypeEnum selected = basePathLocateTypeModel.getElementAt(basePathLocateType.getSelectedIndex());
            adjustTextFieldsInLine(basePathLocation, basePathAnnotation, selected != LocateTypeEnum.FROM_ANNOTATION);
        });
        pathLocateType.setRenderer(new SimpleListCellRenderer<>() {
            @Override
            public void customize(@NotNull JList<? extends LocateTypeEnum> list, LocateTypeEnum value, int index, boolean selected, boolean hasFocus) {
                setText(value.getType());
            }
        });
        pathLocateType.addActionListener(event -> {
            LocateTypeEnum selected = pathLocateTypeModel.getElementAt(pathLocateType.getSelectedIndex());
            adjustTextFieldsInLine(pathLocation, pathAnnotation, selected != LocateTypeEnum.FROM_ANNOTATION);
        });
        methodLocateType.setRenderer(new SimpleListCellRenderer<>() {
            @Override
            public void customize(@NotNull JList<? extends LocateTypeEnum> list, LocateTypeEnum value, int index, boolean selected, boolean hasFocus) {
                setText(value.getType());
            }
        });
        methodLocateType.addActionListener(event -> {
            LocateTypeEnum selected = methodLocateTypeModel.getElementAt(methodLocateType.getSelectedIndex());
            adjustTextFieldsInLine(methodLocation, methodAnnotation, selected != LocateTypeEnum.FROM_ANNOTATION);
        });
    }
}
