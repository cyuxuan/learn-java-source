package cn.cyuxuan.agent.sourceobject;

import java.util.List;

public class MethodDescription {
    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    private String sourceClassName;
    private String className;
    private String methodName;
    private List<String> parameterNameList;
    private List<String> parameterTypeList;
    private String returnType;

    public MethodDescription() {}

    public MethodDescription(String className,String sourceClassName, String methodName, List<String> parameterNameList,
                             List<String> parameterTypeList, String returnType) {
        this.className = className;
        this.sourceClassName = sourceClassName;
        this.methodName = methodName;
        this.parameterNameList = parameterNameList;
        this.parameterTypeList = parameterTypeList;
        this.returnType = returnType;
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
}
