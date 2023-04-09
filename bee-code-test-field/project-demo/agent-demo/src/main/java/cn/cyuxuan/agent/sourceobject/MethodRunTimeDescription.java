package cn.cyuxuan.agent.sourceobject;

/**
 * 方法运行时描述信息
 *
 * @author 陈玉轩
 */
public class MethodRunTimeDescription {
    /**
     * 当前执行id
     */
    private String id;

    /**
     * 方法通用描述信息
     */
    private int methodDescriptionId;

    /**
     * 入参值集合
     */
    private Object[] parameterValues;

    /**
     * 返回参数
     */
    private Object returnValues;

    /**
     * 当前线程方法执行前的字节数量
     */
    private long beforeByteNum;

    /**
     * 当前线程方法执行后的字节数量
     */
    private long afterByteNum;

    /**
     * 方法执行前的gc次数
     */
    private long beforegGcNum;

    /**
     * 方法执行后的gc次数
     */
    private long afterGcNum;

    /**
     * 当前执行的函数层级深度
     */
    private int level;

    /**
     * 执行时间统计
     */
    private long exeTime;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long stopTime;

    /**
     * 开始执行监控
     */
    public void startWatch() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 停止执行监控
     */
    public void stopWatch() {
        this.stopTime = System.currentTimeMillis();
        this.exeTime = stopTime - startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMethodDescriptionId() {
        return methodDescriptionId;
    }

    public void setMethodDescriptionId(int methodDescriptionId) {
        this.methodDescriptionId = methodDescriptionId;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    public Object getReturnValues() {
        return returnValues;
    }

    public void setReturnValues(Object returnValues) {
        this.returnValues = returnValues;
    }

    public long getBeforeByteNum() {
        return beforeByteNum;
    }

    public void setBeforeByteNum(long beforeByteNum) {
        this.beforeByteNum = beforeByteNum;
    }

    public long getAfterByteNum() {
        return afterByteNum;
    }

    public void setAfterByteNum(long afterByteNum) {
        this.afterByteNum = afterByteNum;
    }

    public long getBeforegGcNum() {
        return beforegGcNum;
    }

    public void setBeforegGcNum(long beforegGcNum) {
        this.beforegGcNum = beforegGcNum;
    }

    public long getAfterGcNum() {
        return afterGcNum;
    }

    public void setAfterGcNum(long afterGcNum) {
        this.afterGcNum = afterGcNum;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExeTime() {
        return exeTime;
    }

    public void setExeTime(long exeTime) {
        this.exeTime = exeTime;
    }
}
