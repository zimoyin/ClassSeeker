package github.zimoyin.seeker.vs;

public interface GeneralMethod extends General{
    /**
     * 获取方法返回值
     */
    public String getReturnType();

    /**
     * 获取方法中的参数
     */
    public GeneralMethodParameter[] getParameters();

    /**
     * 获取方法中定义的变量
     */
    public GeneralMethodParameter[] getLocalParameters();
}
