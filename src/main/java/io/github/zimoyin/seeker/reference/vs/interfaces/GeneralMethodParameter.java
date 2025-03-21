package io.github.zimoyin.seeker.reference.vs.interfaces;

import io.github.zimoyin.seeker.reference.vs.visitor.AnnotationVs;

public interface GeneralMethodParameter{
    public String getName();
    public String getType();
    /**
     * 获取所有注解
     */
    public AnnotationVs[] getAnnotations();
}
