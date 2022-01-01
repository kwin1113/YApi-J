package xyz.kwin.yapi.parsers;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import xyz.kwin.yapi.entity.node.InterfaceNode;

import java.util.List;

/**
 * 接口解析
 *
 * @author kwin
 * @since 2021/12/28 2:58 下午
 */
public interface InterfaceParser {

    /**
     * 获取类下所有接口节点
     *
     * @param pattern    解析规则
     * @param psiClass   对应控制器类
     * @param psiMethods 对应方法列表
     * @return 控制器下所有接口节点
     */
    List<InterfaceNode> getInterfaces(String pattern, PsiClass psiClass, PsiMethod[] psiMethods);

    /**
     * 获取指定接口节点
     *
     * @param pattern   解析规则
     * @param psiClass  对应控制器类
     * @param psiMethod 对应接口方法
     * @return 对应接口节点
     */
    InterfaceNode getInterface(String pattern, PsiClass psiClass, PsiMethod psiMethod);
}
