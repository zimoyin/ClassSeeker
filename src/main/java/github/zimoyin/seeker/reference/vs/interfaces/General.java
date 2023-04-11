package github.zimoyin.seeker.reference.vs.interfaces;

import java.util.ArrayList;

public interface General {
    /**
     * 获取名称
     */
    public String getName();

    /**
     * 获取简单名称
     */
    public String getSimpleName();

    /**
     * 获取公开程度修饰符
     */
    public String getModifier();

    /**
     * 是否是静态
     */
    public boolean isStatic();

    /**
     * 是否有Final 修饰符
     */
    public boolean isFinal();

    /**
     * 获取所有注解
     */
    public String[] getAnnotations();

    /**
     * 是否存在该注解
     *
     * @param annotation 注解全限定名
     */
    public boolean isAnnotation(String annotation);

    /**
     * 获取在当前域中的引用对象
     */
    public ArrayList<String> getReferences();
}
