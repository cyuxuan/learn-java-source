package cn.cyuxuan.agent.sourceobject;

import java.util.List;

/**
 * 方法通用描述信息
 *
 * @author 陈玉轩
 */
public class MethodCommonDescription {
    private int id;
    private String sourceClassName;
    private String className;
    private String methodName;
    /**
     * 入参名称集合
     */
    private List<String> parameterNameList;
    /**
     * 入参类型集合
     */
    private List<String> parameterTypeList;

    /**
     * 返回类型
     */
    private String returnType;

    public MethodCommonDescription() {
    }

    public MethodCommonDescription(int id, String className, String sourceClassName, String methodName, List<String> parameterNameList,
                                   List<String> parameterTypeList, String returnType) {
        this.id = id;
        this.className = className;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
        this.parameterNameList = parameterNameList;
        this.parameterTypeList = parameterTypeList;
        this.returnType = returnType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParameterNameList() {
        return parameterNameList;
    }

    public void setParameterNameList(List<String> parameterNameList) {
        this.parameterNameList = parameterNameList;
    }

    public List<String> getParameterTypeList() {
        return parameterTypeList;
    }

    public void setParameterTypeList(List<String> parameterTypeList) {
        this.parameterTypeList = parameterTypeList;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }
}
