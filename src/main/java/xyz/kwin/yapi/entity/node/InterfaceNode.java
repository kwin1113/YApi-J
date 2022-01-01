package xyz.kwin.yapi.entity.node;

import java.util.List;

/**
 * 接口节点
 *
 * @author kwin
 * @since 2021/12/28 2:35 下午
 */
public class InterfaceNode {
    /**
     * 控制器基本路径
     */
    private String basePath;
    /**
     * 接口路径
     */
    private String path;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 接口描述
     */
    private String desc;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 返回值
     */
    private ClassNode retValue;
    /**
     * 参数列表
     */
    private List<ClassNode> params;

    public InterfaceNode() {
    }

    public InterfaceNode(String basePath, String path, String method, String name, String desc, String methodName, ClassNode retValue, List<ClassNode> params) {
        this.basePath = basePath;
        this.path = path;
        this.method = method;
        this.name = name;
        this.desc = desc;
        this.methodName = methodName;
        this.retValue = retValue;
        this.params = params;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ClassNode getRetValue() {
        return retValue;
    }

    public void setRetValue(ClassNode retValue) {
        this.retValue = retValue;
    }

    public List<ClassNode> getParams() {
        return params;
    }

    public void setParams(List<ClassNode> params) {
        this.params = params;
    }
}
