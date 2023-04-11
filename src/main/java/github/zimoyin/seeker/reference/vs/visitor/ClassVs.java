package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralClass;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralField;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethod;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


@Getter
public final class ClassVs extends GeneralImpl implements GeneralClass {
    private final String ClassNameSource;
    private final String SuperClassNameSource;
    private final String[] InterfacesSource;
    private final ArrayList<String> AnnotationNameSource = new ArrayList<String>();
    private final ArrayList<FieldVs> Fields = new ArrayList<>();
    private final ArrayList<MethodVs> Methods = new ArrayList<>();
    private Modifier ModifierValue;
    private boolean isStatic;
    private boolean isInterface;
    private boolean isAbstract;
    private boolean isEnum;
    private boolean isAnnotation;
    private boolean isFinal;
    private boolean isModule;
    private boolean isInnerClass;
    @Setter
    private ClassLoader classLoader = null;
    private String path = "";

    public ClassVs(String className, String superClassName, String[] interfaces) {
        ClassNameSource = className;
        SuperClassNameSource = superClassName;
        InterfacesSource = interfaces;
    }

    public String getClassType() {
        if (isAbstract) return "class";
        if (isInterface) return "interface";
        if (isEnum) return "enum";
        return "class";
    }

    @Override
    public String toString() {
        return getClassType() + " " + getTypeName();
    }

    @Override
    public String getName() {
        return getTypeClassNameGI(ClassNameSource);
    }

    @Override
    public String getModifier() {
        return ModifierValue.getModifierName().toLowerCase();
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
        return getAnnotation(annotation) != null;
    }

    public String getAnnotation(String annotation) {
        return Arrays.stream(getAnnotations()).filter(s -> s.equals(annotation)).distinct().findFirst().orElse(null);
    }

    @Override
    public String getSimpleName() {
        return getTypeName().substring(getTypeName().lastIndexOf(".") + 1);
    }

    @Override
    public Class<?> newInstance() throws ClassNotFoundException {
        return getClassLoader().loadClass(getTypeName());
    }

    private ClassLoader getClassLoader() {
        if (classLoader == null) this.classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader;
    }

    @Override
    public String getLoadPath() {
        return path;
    }

    @Override
    public String getPackage() {
        return getTypeName().substring(0, getTypeName().lastIndexOf("." + getSimpleName()));
    }

    @Override
    public String getTypeName() {
        return getTypeClassNameGI(ClassNameSource);
    }

    @Override
    public GeneralClass getSuperClassVs() throws IOException {
        if (SuperClassNameSource == null) return null;
        return VisitorClass.getClassReference(SuperClassNameSource, getLoadPath()).getClassVsInstance();
    }

    /**
     * 获取父类的名称
     */
    @Override
    public String getSuperClassName() {
        return getTypeClassNameGI(SuperClassNameSource);
    }

    @Override
    public GeneralMethod[] getConstructors() {
        return Methods.stream().filter(methodVs -> methodVs.getMethodName().contains(">")).toArray(GeneralMethod[]::new);
    }

    @Override
    public GeneralMethod getConstructor() {
        return getConstructor(new String[0]);
    }

    @Override
    public GeneralMethod getConstructor(String... paramsCls) {
        if (Arrays.stream(paramsCls).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("方法参数类型列表中禁止传入 'null' 值");
        }
        return Arrays.stream(getConstructors())
                .filter(generalMethod -> generalMethod.getParameterTypes().length == paramsCls.length)
                .filter(generalMethod -> {
                    String[] parameters = generalMethod.getParameterTypes();
                    for (int i = 0; i < parameters.length; i++)
                        if (!parameters[i].equals(paramsCls[i])) return false;
                    return true;
                }).findFirst().orElse(null);
    }

    @Override
    public GeneralMethod getConstructor(Class<?>... params) {
        if (Arrays.stream(params).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("方法参数类型列表中禁止传入 'null' 值");
        }
        return Arrays.stream(getConstructors())
                .filter(generalMethod -> generalMethod.getParameterTypes().length == params.length)
                .filter(generalMethod -> {
                    String[] parameters = generalMethod.getParameterTypes();
                    for (int i = 0; i < parameters.length; i++)
                        if (!parameters[i].equals(params[i].getTypeName())) return false;
                    return true;
                }).findFirst().orElse(null);
    }

    @Override
    public GeneralMethod getMethod(String name) {
        return getMethod(name,new String[0]);
    }
    @Override
    public GeneralMethod getMethod(String name, String... paramsCls) {
        if (Arrays.stream(paramsCls).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("方法参数类型列表中禁止传入 'null' 值");
        }
        return Arrays.stream(getMethods())
                .filter(generalMethod -> generalMethod.getName().equals(name))
                .filter(generalMethod -> generalMethod.getParameterTypes().length == paramsCls.length)
                .filter(generalMethod -> {
                    String[] parameters = generalMethod.getParameterTypes();
                    for (int i = 0; i < parameters.length; i++)
                        if (!parameters[i].equals(paramsCls[i])) return false;
                    return true;
                }).findFirst().orElse(null);
    }
    @Override
    public GeneralMethod getMethod(String name, Class<?>... paramsCls) {
        if (Arrays.stream(paramsCls).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("方法参数类型列表中禁止传入 'null' 值");
        }
        return Arrays.stream(getMethods())
                .filter(generalMethod -> generalMethod.getName().equals(name))
                .filter(generalMethod -> generalMethod.getParameterTypes().length == paramsCls.length)
                .filter(generalMethod -> {
                    String[] parameters = generalMethod.getParameterTypes();
                    for (int i = 0; i < parameters.length; i++)
                        if (!parameters[i].equals(paramsCls[i].getTypeName())) return false;
                    return true;
                }).findFirst().orElse(null);
    }

    @Override
    public GeneralMethod[] getMethods() {
        return Methods.stream().filter(methodVs -> !methodVs.getMethodName().contains(">")).toArray(GeneralMethod[]::new);
    }


    @Override
    public GeneralField[] getFields() {
        return Fields.toArray(GeneralField[]::new);
    }

    @Override
    public GeneralField getFieldByName(String name) {
        return Arrays.stream(getFields()).filter(field -> field.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public GeneralField[] getFieldByType(String name) {
        return Arrays.stream(getFields()).filter(field -> field.getType().equals(name)).toArray(GeneralField[]::new);
    }

    @Override
    public GeneralField[] getFieldByType(Class<?> name) {
        return Arrays.stream(getFields()).filter(field -> field.getType().equals(name.getTypeName())).toArray(GeneralField[]::new);
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public boolean isInterface() {
        return isInterface;
    }

    @Override
    public boolean isAnnotation() {
        return isAnnotation;
    }

    @Override
    public boolean isEnum() {
        return isEnum;
    }

    protected void setAnnotationNameSource(String annotationName) {
        this.AnnotationNameSource.add(annotationName);
    }

    protected void setField(FieldVs field) {
        this.Fields.add(field);
    }

    protected void setMethod(MethodVs methodVs) {
        this.Methods.add(methodVs);
    }

    protected void setModifier(Modifier modifier) {
        this.ModifierValue = modifier;
    }

    protected void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    protected void setInterface(boolean isInterface) {
        this.isInterface = isInterface;
    }

    protected void setEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

    protected void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    protected void setAnnotation(boolean isAnnotation) {
        this.isAnnotation = isAnnotation;
    }

    protected void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    protected void setModule(boolean isModule) {
        this.isModule = isModule;
    }

    protected void setInnerClass(boolean isInnerClass) {
        this.isInnerClass = isInnerClass;
    }

    protected void setPath(String path) {
        this.path = path;
    }
}
