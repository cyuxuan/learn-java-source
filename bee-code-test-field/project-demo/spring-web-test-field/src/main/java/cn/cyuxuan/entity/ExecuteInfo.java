package cn.cyuxuan.entity;

/**
 * 执行信息
 *
 * @author 陈玉轩
 * @since 23.04.05
 */
public class ExecuteInfo {
    /**
     * 当前这次执行的id, 唯一标记
     */
    public String executeId;

    /**
     * 当前执行信息签名
     */
    public String executeSignature;

    /**
     * 当前执行的函数名称
     */
    public String methodName;

    /**
     * 当前执行的函数层级深度
     */
    public int level;

    /**
     * 当前函数执行的时间
     */
    public long executeTime;

    /**
     * 当前线程方法执行前的字节数量
     */
    public long beforeByteNum;

    /**
     * 当前线程方法执行后的字节数量
     */
    public long afterByteNum;

    /**
     * 方法执行前的gc次数
     */
    public long beforegGcNum;

    /**
     * 方法执行后的gc次数
     */
    public long afterGcNum;

    public ExecuteInfo() {}

    public ExecuteInfo(String methodName) {
        this.methodName = methodName;
    }

    public void setExecuteSignature(String methodName, String inParams, String outParams) {
        this.executeSignature = methodName + inParams + outParams;
    }
}
