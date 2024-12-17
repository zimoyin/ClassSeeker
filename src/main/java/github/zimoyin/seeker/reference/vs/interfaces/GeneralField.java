package github.zimoyin.seeker.reference.vs.interfaces;


import github.zimoyin.seeker.reference.vs.visitor.AnnotationVs;
import github.zimoyin.seeker.reference.vs.visitor.GenericType;

public interface GeneralField extends General{
    public Object getValue();
    public String getType();
    public boolean isVolatile();
    public AnnotationVs[] getGenericAnnotations();
    public AnnotationVs getGenericAnnotation(String clsName);
    public AnnotationVs getGenericAnnotation(AnnotationVs av);
    public AnnotationVs getGenericAnnotation(int index);
    public GenericType getGenericType(String clsName);
    public GenericType getGenericType(int index);
    public GenericType[] getGenericTypes();

}
