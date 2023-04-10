package github.zimoyin.seeker.vs;

public interface GeneralClass extends General {
    /**
     * 获取Class实例
     */
    public String getInsClass();

    /**
     * 获取加载路径。如果改类是从谋个jar中加载则是那个jar的路径。
     */
    public String getLoadPath();

    /**
     * 获取包名
     */
    public String getPackage();


    /**
     * 获取类的全限定名
     */
    public String getTypeName();

    /**
     * 获取本类的父类
     */
    public GeneralClass getSuperclass();

    /**
     * 获取构造方法
     */
    public GeneralMethod[] getConstructors();

    /**
     * 获取所有方法
     */
    public GeneralMethod[] getMethods();

    /**
     * 获取所有字段
     */
    public GeneralField[] getFields();

    /**
     * 类是否为抽象类
     */
    public boolean isAbstract();

    /**
     * 类是否为接口
     */
    public boolean isInterface();

    /**
     * 类是否为注解
     */
    public boolean isAnnotation();

    /**
     * 类是否为枚举
     */
    public boolean isEnum();


}
