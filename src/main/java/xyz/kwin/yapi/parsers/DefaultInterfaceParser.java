package xyz.kwin.yapi.parsers;

import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import xyz.kwin.yapi.constant.CommonConstant;
import xyz.kwin.yapi.constant.RequestMethod;
import xyz.kwin.yapi.entity.PropertyLocator;
import xyz.kwin.yapi.entity.node.ClassNode;
import xyz.kwin.yapi.entity.node.InterfaceNode;
import xyz.kwin.yapi.setting.SettingAdaptor;
import xyz.kwin.yapi.setting.vo.Pattern;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认接口解析
 * 通过配置文件配置，根据{@link PropertyLocator}定位
 *
 * @author kwin
 * @since 2021/12/28 3:38 下午
 */
public class DefaultInterfaceParser implements InterfaceParser {

    private final MethodParser interfaceParser = new MethodParser();

    @Override
    public List<InterfaceNode> getInterfaces(String pattern, PsiClass psiClass, PsiMethod[] psiMethods) {
        return Arrays.stream(psiMethods).map(method -> getInterface(pattern, psiClass, method)).collect(Collectors.toList());
    }

    @Override
    public InterfaceNode getInterface(String pattern, PsiClass psiClass, PsiMethod psiMethod) {
        SettingAdaptor settingAdaptor = SettingAdaptor.getInstance();
        if (!settingAdaptor.valid()) {
            Messages.showErrorDialog(CommonConstant.Notice.CONFIG_MISSING, CommonConstant.Notice.ERROR_TITLE);
            return null;
        }
        Pattern parsePattern = settingAdaptor.getPatterns().get(pattern);
        if (parsePattern == null) {
            return null;
        }
        // 从这两个地方获取配置中配置的字段
        // 类注释 + 注解
        PsiDocComment classDocComment = psiClass.getDocComment();
        PsiAnnotation[] classAnnotations = psiClass.getAnnotations();
        // 方法注释 + 注解
        PsiDocComment docComment = psiMethod.getDocComment();
        PsiAnnotation[] annotations = psiMethod.getAnnotations();

        // 接口基本路径
        String basePath = parsePattern.getBasePathLocator() == null ? "" :
                parsePattern.getBasePathLocator().getValue(classDocComment, classAnnotations);
        basePath = basePath.replace("\"", "");
        // 接口地址
        String path = parsePattern.getPathLocator().getValue(docComment, annotations);
        path = path.replace("\"", "");
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        // 请求方法
        String method = parsePattern.getMethodLocator().getValue(docComment, annotations);
        RequestMethod requestMethod = handleRequestMethod(method);
        method = requestMethod.getMethod();
        // 接口名称
        String name = interfaceParser.getMethodTitle(psiMethod);
        // 接口描述
        String desc = interfaceParser.getMethodDesc(psiMethod);
        // 方法名称
        String methodName = psiMethod.getName();
        // 返回值
        ClassNode retValue = interfaceParser.getReturnValue(psiMethod);
        // 参数列表
        List<ClassNode> params = interfaceParser.getParameters(psiMethod);

        // YApi的GET请求不支持请求体，需要将body平铺（兼容GET携带请求体的奇怪注册方式）
        if (requestMethod == RequestMethod.GET) {
            params = flattenParamList(params);
        }

        return new InterfaceNode(basePath, path, method, name, desc, methodName, retValue, params);
    }

    /**
     * 将定位的method字段转换成YApi支持的RequestMethod
     * 定位的method字段需要包含GET/POST等字眼
     * 如果同时出现多种method字段，则取第一个
     *
     * @param method 定位的method字段
     * @return RequestMethod
     */
    private RequestMethod handleRequestMethod(String method) {
        for (RequestMethod requestMethodEnum : RequestMethod.values()) {
            if (StringUtils.containsIgnoreCase(method, requestMethodEnum.getMethod())) {
                return requestMethodEnum;
            }
        }
        return RequestMethod.GET;
    }

    /**
     * 将参数列表展平
     * 其中碰到集合类不做处理，直接上传
     * 就不应该出现集合类，甚至不应该出现需要展平的情况，属于不合规的GET请求
     *
     * @param paramList 参数列表
     * @return 展平后的基础参数列表
     */
    private List<ClassNode> flattenParamList(List<ClassNode> paramList) {
        List<ClassNode> params = new ArrayList<>();
        for (ClassNode param : paramList) {
            // 不判断集合类collectionElements
            if (param.getFields() == null) {
                String desc = param.getDesc() == null ? "" : param.getDesc();
                param.setDesc("(" + param.getType() + ")" + desc);
                params.add(param);
            } else {
                params.addAll(flattenParamList(param.getFields()));
            }
        }
        return params;
    }
}
