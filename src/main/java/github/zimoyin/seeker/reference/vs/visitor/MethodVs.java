package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethod;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MethodVs extends GeneralImpl implements GeneralMethod {
    private final String MethodName;
    private final String MethodDescription;
    private String ReturnTypeNameSource;
    /**
     * 方法参数列表源类型
     */
    private final ArrayList<String> ParameterTypeNameSource = new ArrayList<String>();
    /**
     * 方法参数列表源名称
     */
    private final HashMap<String, String> ParameterNameSourceMap = new HashMap<>();
    /**
     * 方法参数列表源注解
     * 参数name，索引
     */
    private final HashMap<String, Integer> ParameterNameSourceIndexMap = new HashMap<>();
    /**
     * 方法参数列表源注解
     * 索引，注解
     */
    private final HashMap<Integer, ArrayList<AnnotationVs>> ParameterAnnotationVsMap = new HashMap<>();
    private final ArrayList<String> ThrowExceptionNameSource = new ArrayList<>();
    private final ArrayList<AnnotationVs> AnnotationsSource = new ArrayList<>();
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
        if (!ParameterTypeNameSource.isEmpty())
            buffer.append(Arrays.toString(getParameterTypes()), 1, Arrays.toString(getParameterTypes()).length() - 1);
        buffer.append(")").append(" ");
        if (!ThrowExceptionNameSource.isEmpty()) buffer
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
    public AnnotationVs[] getAnnotations() {
        return AnnotationsSource.toArray(AnnotationVs[]::new);
    }

    @Override
    public AnnotationVs getAnnotations(String name) {
        return AnnotationsSource.stream().filter(s -> s.getName().equals(name)).distinct().findFirst().orElse(null);
    }

    @Override
    public AnnotationVs getAnnotations(AnnotationVs av) {
        return AnnotationsSource.stream().filter(s -> s.equals(av)).distinct().findFirst().orElse(null);
    }

    @Override
    public AnnotationVs getAnnotations(Class<?> av) {
        String name = av.getName();
        return AnnotationsSource.stream().filter(s -> s.getName().equals(name)).distinct().findFirst().orElse(null);
    }

    @Override
    public boolean isContainAnnotation(String annotation) {
        return Arrays.stream(getAnnotations()).map(AnnotationVs::getName).collect(Collectors.toList()).contains(annotation);
    }

    @Override
    public ArrayList<String> getReferences() {
        ArrayList<String> refs = new ArrayList<String>();
        refs.add(getRef(getReturnType()));
        for (String type : getParameterTypes()) {
            refs.add(getRef(type));
        }
//        System.err.println(getMethodName());
        for (GeneralMethodParameter localVar : getLocalVariable()) {
            refs.add(getRef(localVar.getType()));
//            System.err.println(localVar.getName()+": "+localVar.getType());
        }

        refs.addAll(List.of(Arrays.stream(getAnnotations()).map(AnnotationVs::getName).toArray(String[]::new)));
        refs.addAll(List.of(getTryExceptions()));
        refs.addAll(List.of(getThrowExceptions()));
        return refs;
    }

    private String getRef(String type) {
        String ref = type.replaceAll("[\\[|\\]]", "");
        ref = ref.replaceAll("[\\[|\\]]", "");
        return ref;
    }

    @Override
    public String getReturnType() {
        return getTypeClassNameGI(ReturnTypeNameSource);
    }

    @Override
    public String[] getParameterTypes() {
//        parameterSafe();
        return ParameterTypeNameSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }

    @Override
    public GeneralMethodParameter[] getParameters() {
//        parameterSafe();
        ArrayList<GeneralMethodParameter> parameters = new ArrayList<>();
        ParameterNameSourceMap.forEach((s, s2) -> {
            Integer index = ParameterNameSourceIndexMap.get(s);
            ArrayList<AnnotationVs> vsArrayList = ParameterAnnotationVsMap.get(index);
            parameters.add(new MethodLocalVariableVs(s, s2, vsArrayList));
        });
        return parameters.toArray(new GeneralMethodParameter[0]);
    }

    protected void addParameterAnnotation(int index, AnnotationVs annotationVs){
        ParameterAnnotationVsMap.computeIfAbsent(index, k -> new ArrayList<>()).add(annotationVs);
    }

    /**
     * 检测参数列表参数是否对应
     * TODO: 此方法用于开发时对信息进行检测。上传时请删掉此方法
     * TODO：如果方法未被检测到方法局部变量导致无法执行 visitLocalVariable 方法，导致无法获取到方法的参数列表具体信息。准备寻找新的方式来获取
     */
    protected void parameterSafe() {
        if (ParameterNameSourceMap.size() != ParameterTypeNameSource.size()) {
            System.err.println("在VisitorClass 与 VisitorMethod 中解析出来的变量列表数量不一致");
            System.err.println("JarPath: " + getThisClass().getPath());
            System.err.println("ClassDesc: " + getThisClass());
            System.err.println("MethodName: " + getName());
            System.err.println("Method: " + MethodDescription);
            System.err.println("Method  isStatic: " + isStatic);
            System.err.println("Method  isFinal: " + isFinal);
            System.err.println("Method  isNative: " + isNative);
            System.err.println("Method  isSynthetic: " + isSynthetic);
            System.err.println("ParameterNameSourceMap" + ParameterNameSourceMap);
            System.err.println("ParameterTypeNameSource" + ParameterTypeNameSource);
            System.err.println("可能原因：没有调用 github.zimoyin.seeker.reference.vs.visitor.VisitorMethod.visitLocalVariable 方法，导致该方法未解析");
            System.err.println("可能原因：该方法为静态方法或者存在其他修饰符");
            System.err.println("可能原因：该方法未被检测到方法局部变量导致无法执行 visitLocalVariable 方法");
            System.err.println();
            throw new IllegalArgumentException("对ClassSeeker开发者留言：在解析方法时，发现解析的方法参数个数不一致,请检查 github.zimoyin.seeker.reference.vs.visitor.VisitorMethod.visitLocalVariable 方法是否存在BUG");
        }
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
        return className == null ? thisClass.getName() : className;
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

    protected void setAnnotation(AnnotationVs annName) {
        this.AnnotationsSource.add(annName);
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
