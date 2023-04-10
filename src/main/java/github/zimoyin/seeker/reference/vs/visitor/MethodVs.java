package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethod;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;
import lombok.Getter;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Getter
public class MethodVs extends GeneralImpl implements GeneralMethod {
    private final String MethodName;
    private final String MethodDescription;
    private String ReturnTypeNameSource;
    private final ArrayList<String> ParameterTypeNameSource = new ArrayList<String>();
    private final ArrayList<String> ThrowExceptionNameSource = new ArrayList<>();
    private final ArrayList<String> AnnotationNameSource = new ArrayList<>();
    private final ArrayList<String> ParameterAnnotationNameSource = new ArrayList<>();
    private final HashMap<String, String> LocalVariableNameSource = new HashMap<>();
    private Modifier ModifierValue;
    private boolean isStatic;
    private boolean isFinal;

    public MethodVs(String methodName, String methodDescription) {
        MethodName = methodName;
        MethodDescription = methodDescription;
    }

    @Override
    public String toString() {
        return "MethodVs{" +
                "MethodName='" + MethodName + '\'' +
                ", MethodDescription='" + MethodDescription + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return MethodName;
    }

    @Override
    public String getSimpleName() {
        return MethodName;
    }

    @Override
    public String getModifier() {
        return ModifierValue.name().toLowerCase();
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public String[] getAnnotations() {
        return AnnotationNameSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }

    @Override
    public boolean isAnnotation(String annotation) {
        return Arrays.asList(getAnnotations()).contains(annotation);
    }

    @Override
    public String getReturnType() {
        return getTypeClassNameGI(ReturnTypeNameSource);
    }

    @Override
    public String[] getParameters() {
        return ParameterTypeNameSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }


    @Override
    public String[] getThrowExceptions() {
        return ThrowExceptionNameSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }

    @Override
    public GeneralMethodParameter[] getLocalVariable() {
        ArrayList<GeneralMethodParameter> variables = new ArrayList<>();
        LocalVariableNameSource.forEach((s, s2) -> variables.add(new ParameterVs(s, getTypeClassNameGI(s2))));
        return variables.toArray(new GeneralMethodParameter[0]);
    }

    protected void setReturn(String returnType) {
        this.ReturnTypeNameSource = returnType;
    }

    protected void setParameterType(String argName) {
        this.ParameterTypeNameSource.add(argName);
    }

    protected void setThrowException(String exceptionName) {
        this.ThrowExceptionNameSource.add(exceptionName);
    }

    protected void setAnnotation(String annName) {
        this.AnnotationNameSource.add(annName);
    }

    protected void setParameterAnnotation(String para) {
        this.ParameterAnnotationNameSource.add(para);
    }

    protected void setLocalVariable(String name, String value) {
        this.LocalVariableNameSource.put(name, value);
    }

    protected void setModifier(Modifier modifier) {
        this.ModifierValue = modifier;
    }

    protected void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    protected void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
}
