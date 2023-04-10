package github.zimoyin.seeker.reference.vs.interfaces;

public interface GeneralMethodParameter{
    public String getName();
    public String getType();
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
}
