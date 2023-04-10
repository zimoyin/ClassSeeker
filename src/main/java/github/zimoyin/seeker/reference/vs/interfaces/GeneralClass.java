package github.zimoyin.seeker.reference.vs.interfaces;

import java.io.IOException;

public interface GeneralClass extends General {
    /**
     * 获取Class实例
     */
    public Class<?> newInstance() throws ClassNotFoundException;

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
    public GeneralClass getSuperClassVs() throws IOException;

    /**
     * 是否存在指定的注解
     * @param annotation 注解全限定名
     */
    public boolean isAnnotation(String annotation);
    /**
     * 父类的名称
     */
    public String getSuperClassName();

    /**
     * 获取构造方法
     */
    public GeneralMethod[] getConstructors();

    /**
     * 获取某个构造方法
     *
     * @param paramsCls 方法参数的全限定名.注意如果是基础数据类型则是类型本身的名称
     * @return 方法
     */
    public GeneralMethod getConstructor(String... paramsCls);

    /**
     * 获取某个构造方法
     *
     * @param params 方法参数的类对象.注意如果是基础数据类型则是类型本身 如 ： int.class
     * @return 方法
     */
    public GeneralMethod getConstructor(Class<?>... params);

    /**
     * 获取某个方法
     *
     * @param name      方法名称
     * @param paramsCls 方法参数的全限定名.注意如果是基础数据类型则是类型本身的名称
     * @return 方法
     */
    public GeneralMethod getMethod(String name, String... paramsCls);

    /**
     * 获取某个方法
     *
     * @param name   方法名称
     * @param params 方法参数的类对象.注意如果是基础数据类型则是类型本身 如 ： int.class
     * @return 方法
     */
    public GeneralMethod getMethod(String name, Class<?>... params);

    /**
     * 获取所有方法
     */
    public GeneralMethod[] getMethods();

    /**
     * 获取所有字段
     */
    public GeneralField[] getFields();

    /**
     * 获取所有字段
     * @param name 字段的名称
     */
    public GeneralField getFieldByName(String name);
    /**
     * 获取所有字段
     * @param name 字段的的类型。这是个全限定名
     */
    public GeneralField[] getFieldByType(String name);
    /**
     * 获取所有字段
     * @param name 字段的的类型。这是个全限定名
     */
    public GeneralField[] getFieldByType(Class<?> name);

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
