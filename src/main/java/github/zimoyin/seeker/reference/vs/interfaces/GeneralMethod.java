package github.zimoyin.seeker.reference.vs.interfaces;

import github.zimoyin.seeker.reference.vs.visitor.ClassVs;

public interface GeneralMethod extends General {
    /**
     * 获取方法返回值
     */
    public String getReturnType();

    /**
     * 获取方法中的参数列表中的参数类型
     */
    public String[] getParameterTypes();

    /**
     * 获取方法中的参数列表中的参数
     */
    public GeneralMethodParameter[] getParameters();

    /**
     * 获取 this 关键字指向的对象的名称
     */
    public String getThisClassName();

    /**
     * 获取 this 关键字指向的对象
     */
    public ClassVs getThisClass();

    /**
     * 获取方法 throw 的异常列表
     */
    public String[] getThrowExceptions();

    /**
     * 获取方法 try 的异常列表
     */
    public String[] getTryExceptions();

    /**
     * 获取在方法里面定义的变量。注意如果在里面new了一个对象，但对象没有用变量存储是不会被记录的。
     * 变量的作用域必须是在整个方法内，如果是在方法内的一个域变量则不能被获取，如if/for/while...内定义的变量
     */
    public GeneralMethodParameter[] getLocalVariable();
}
