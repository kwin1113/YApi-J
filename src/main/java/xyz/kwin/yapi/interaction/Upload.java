package xyz.kwin.yapi.interaction;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import xyz.kwin.yapi.constant.CommonConstant;
import xyz.kwin.yapi.constant.LocateTypeEnum;
import xyz.kwin.yapi.entity.PropertyLocator;
import xyz.kwin.yapi.entity.node.InterfaceNode;
import xyz.kwin.yapi.entity.yapi.Response;
import xyz.kwin.yapi.exceptions.BaseException;
import xyz.kwin.yapi.interaction.ui.PatternSelector;
import xyz.kwin.yapi.interaction.ui.ProjectSelector;
import xyz.kwin.yapi.parsers.DefaultInterfaceParser;
import xyz.kwin.yapi.service.InterfaceService;
import xyz.kwin.yapi.service.ProjectService;
import xyz.kwin.yapi.setting.SettingAdaptor;
import xyz.kwin.yapi.setting.vo.Pattern;
import xyz.kwin.yapi.setting.vo.ProjectVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 上传Action
 *
 * @author kwin
 * @since 2021/12/13 4:42 下午
 */
public class Upload extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            // 获取配置
            SettingAdaptor settingAdaptor = SettingAdaptor.getInstance();
            if (!settingAdaptor.valid() ||
                    settingAdaptor.getProjects() == null || settingAdaptor.getProjects().isEmpty() ||
                    settingAdaptor.getPatterns() == null || settingAdaptor.getPatterns().isEmpty()) {
                throw new BaseException(CommonConstant.Notice.CONFIG_MISSING);
            }

            // 用户选择解析规则和上传项目
            PatternSelector patternSelector = new PatternSelector();
            if (!patternSelector.showAndGet()) {
                return;
            }
            String pattern = patternSelector.getPatternName();

            ProjectSelector projectSelector = new ProjectSelector();
            if (!projectSelector.showAndGet()) {
                return;
            }
            ProjectVO selectedProject = projectSelector.getProject();
            String projectToken = selectedProject.getToken();

            // 获取当前操作项目
            Project project = e.getProject();
            if (project == null) {
                return;
            }
            // 获取当前的编辑的位置对应的PSI类或方法
            PsiElement curPsiElement = e.getData(CommonDataKeys.PSI_ELEMENT);

            // 获取类对象和方法，从注释中获取信息
            PsiClass curPsiClass;
            PsiMethod[] psiMethods;
            // 根据选中的不同对象进行操作
            // 只支持在类或方法上进行选中
            String selectedMsg;
            if (curPsiElement instanceof PsiClass) {
                curPsiClass = (PsiClass) curPsiElement;
                psiMethods = getValidMethods(pattern, curPsiClass, null);
                selectedMsg = String.format(CommonConstant.Notice.SELECTED_CLASS, curPsiClass.getName());
            } else if (curPsiElement instanceof PsiMethod) {
                PsiMethod psiMethod = (PsiMethod) curPsiElement;
                curPsiClass = psiMethod.getContainingClass();
                psiMethods = getValidMethods(pattern, curPsiClass, new PsiMethod[]{psiMethod});
                String classPrefix = curPsiClass == null ? "" : curPsiClass.getName() + "#";
                selectedMsg = String.format(CommonConstant.Notice.SELECTED_METHOD, classPrefix + psiMethod.getName());
            } else {
                // error target
                throw new BaseException(CommonConstant.Notice.ERROR_TARGET);
            }

            if (curPsiClass == null || psiMethods.length == 0) {
                Messages.showInfoMessage(project, CommonConstant.Notice.NO_VALID_METHOD, CommonConstant.Notice.NOTICE_TITLE);
                return;
            }

            // 提示选择信息
            String uploadConfig = String.format(CommonConstant.Notice.SELECTED_UPLOAD_CONFIG, pattern, selectedProject.getName());
            int optionReturn = Messages.showOkCancelDialog(project, uploadConfig + "\n" + selectedMsg, CommonConstant.Notice.UPLOAD_CONFIRM, "OK", "Cancel", null);
            if (optionReturn != 0) {
                return;
            }

            // 解析接口
            DefaultInterfaceParser defaultInterfaceParser = new DefaultInterfaceParser();
            List<InterfaceNode> interfaces = defaultInterfaceParser.getInterfaces(pattern, curPsiClass, psiMethods);
            if (CollectionUtils.isNotEmpty(interfaces)) {
                ProjectService projectService = ProjectService.getInstance();
                InterfaceService interfaceService = InterfaceService.getInstance();

                // 获取项目的第一个分类，一般为公共分类
                Integer catid = projectService.getCatid(projectToken);
                // 失败方法警告
                List<String> failedMethods = new ArrayList<>();
                for (InterfaceNode interfaceNode : interfaces) {
                    Response<Object> response = interfaceService.upload(projectToken, catid, interfaceNode);
                    if (!response.isSuccess()) {
                        String failedDetail = String.format(CommonConstant.Notice.FAILED_METHOD_REASON, interfaceNode.getMethodName(), response.getErrmsg());
                        failedMethods.add(failedDetail);
                    }
                }
                if (failedMethods.isEmpty()) {
                    Messages.showInfoMessage(project, CommonConstant.Notice.UPLOAD_SUCCESS, CommonConstant.Notice.NOTICE_TITLE);
                } else {
                    String failedMethodStr = String.join(",\n ", failedMethods);
                    Messages.showInfoMessage(project, CommonConstant.Notice.UPLOAD_COMPLETE + "\n" + failedMethodStr, CommonConstant.Notice.NOTICE_TITLE);
                }
            }
        } catch (BaseException ex) {
            ex.printStackTrace();
            Messages.showErrorDialog(ex.getMessage(), CommonConstant.Notice.ERROR_TITLE);
        } catch (Exception ex) {
            ex.printStackTrace();
            Messages.showErrorDialog(CommonConstant.Notice.UNKNOWN_TITLE, CommonConstant.Notice.ERROR_TITLE);
        }
    }

    /**
     * 获取可以解析的方法
     *
     * @param psiClass 目标类
     * @param methods  目标方法
     * @return 可以解析的方法列表
     */
    private PsiMethod[] getValidMethods(@NotNull String patternName, PsiClass psiClass, PsiMethod[] methods) {
        List<PsiMethod> methodList;
        if (methods == null) {
            PsiMethod[] classMethods = psiClass.getMethods();
            methodList = Arrays.stream(classMethods).filter(e -> {
                PsiModifierList modifierList = e.getModifierList();
                // 公共方法
                return modifierList.hasModifierProperty(PsiModifier.PUBLIC) &&
                        // 且没有其他修饰（奇奇怪怪的应该不会有）
                        !modifierList.hasModifierProperty(PsiModifier.STATIC) && !modifierList.hasModifierProperty(PsiModifier.FINAL);
            }).collect(Collectors.toList());
        } else {
            methodList = Arrays.stream(methods).collect(Collectors.toList());
        }

        SettingAdaptor setting = SettingAdaptor.getInstance();
        Pattern pattern = setting.getPatterns().get(patternName);
        PropertyLocator pathLocator = pattern.getPathLocator();
        // 删除没有注释的方法
        Iterator<PsiMethod> iterator = methodList.iterator();
        if (pathLocator.getLocateType() == LocateTypeEnum.FROM_ANNOTATION) {
            while (iterator.hasNext()) {
                PsiMethod method = iterator.next();
                PsiAnnotation[] annotations = method.getAnnotations();
                if (annotations.length == 0) {
                    iterator.remove();
                    continue;
                }
                boolean valid = Arrays.stream(annotations).anyMatch(e -> e.getQualifiedName() != null && e.getQualifiedName().equals(pathLocator.getAnnotation()));
                if (!valid) {
                    iterator.remove();
                }
            }
        }
        // 删除没有注释的方法
        else {
            while (iterator.hasNext()) {
                PsiMethod method = iterator.next();
                PsiDocComment docComment = method.getDocComment();
                if (docComment == null || docComment.getTags().length == 0) {
                    iterator.remove();
                    continue;
                }
                boolean valid = Arrays.stream(docComment.getTags()).anyMatch(e -> e.getName().equals(pathLocator.getLocation()));
                if (!valid) {
                    iterator.remove();
                }
            }
        }
        return methodList.toArray(new PsiMethod[0]);
    }

}
