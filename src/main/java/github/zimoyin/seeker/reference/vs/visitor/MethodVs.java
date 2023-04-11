package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethod;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Getter
public class MethodVs extends GeneralImpl implements GeneralMethod {
    private final String MethodName;
    private final String MethodDescription;
    private String ReturnTypeNameSource;
    private final ArrayList<String> ParameterTypeNameSource = new ArrayList<String>();
    private final HashMap<String, String> ParameterNameSourceMap = new HashMap<>();
    private final ArrayList<String> ThrowExceptionNameSource = new ArrayList<>();
    private final ArrayList<String> AnnotationNameSource = new ArrayList<>();
    private final ArrayList<String> ParameterAnnotationNameSource = new ArrayList<>();
    private final HashMap<String, String> LocalVariableNameSource = new HashMap<>();
    private Modifier ModifierValue;
    private boolean isStatic;
    private boolean isFinal;
    private String className;
    private boolean isSynthetic;
    private boolean isNative;
    private final ArrayList<String> TryCatchBlockExceptionNameSource = new ArrayList<>();
    private ClassVs thisClass;

    public MethodVs(String methodName, String methodDescription) {
        MethodName = methodName;
        MethodDescription = methodDescription;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getModifier()).append(" ");
        if (isStatic) buffer.append("static").append(" ");
        if (isFinal) buffer.append("final").append(" ");
        if (isSynthetic) buffer.append("synthetic").append(" ");
        if (isNative) buffer.append("native").append(" ");
        buffer.append(getReturnType()).append(" ");
        buffer.append(getThisClassName()).append(".");
        buffer.append(getName());
        buffer.append("(");
        if (ParameterTypeNameSource.size() > 0)
            buffer.append(Arrays.toString(getParameterTypes()), 1, Arrays.toString(getParameterTypes()).length() - 1);
        buffer.append(")").append(" ");
        if (ThrowExceptionNameSource.size() > 0) buffer
                .append("throws")
                .append(" ")
                .append(Arrays.toString(getThrowExceptions()), 1, Arrays.toString(getThrowExceptions()).length() - 1);
        return buffer.toString();
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
    public String[] getParameterTypes() {
        return ParameterTypeNameSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }

    @Override
    public GeneralMethodParameter[] getParameters() {
        ArrayList<GeneralMethodParameter> parameters = new ArrayList<>();
        ParameterNameSourceMap.forEach((s, s2) -> parameters.add(new MethodLocalVariableVs(s, s2,ParameterAnnotationNameSource)));
        return parameters.toArray(new GeneralMethodParameter[0]);
    }

    @Override
    public String[] getThrowExceptions() {
        return ThrowExceptionNameSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }

    @Override
    public String[] getTryExceptions() {
        return TryCatchBlockExceptionNameSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }

    @Override
    public GeneralMethodParameter[] getLocalVariable() {
        ArrayList<GeneralMethodParameter> variables = new ArrayList<>();
        LocalVariableNameSource.forEach((s, s2) -> variables.add(new MethodLocalVariableVs(s, s2)));
        return variables.toArray(new GeneralMethodParameter[0]);
    }

    @Override
    public String getThisClassName() {
        return className;
    }

    @Override
    public ClassVs getThisClass() {
        return thisClass;
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

    protected void setSynthetic(boolean isSynthetic) {
        this.isSynthetic = isSynthetic;
    }

    protected void setNative(boolean isNative) {
        this.isNative = isNative;
    }

    protected void setTryCatchBlockException(String type) {
        this.TryCatchBlockExceptionNameSource.add(type);
    }

    protected void setClassName(String value) {
        this.className = value;
    }

    protected void setThisClass(ClassVs classVsInstance) {
        this.thisClass = classVsInstance;
    }
}
