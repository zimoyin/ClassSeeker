package io.github.zimoyin.seeker.reference.vs.visitor;

import org.objectweb.asm.*;


/**
 * 类引用分析
 */
public class VisitorClass extends ClassVisitor {
    private final String path;
    private final String[] libs;
    private ClassVs ClassVsInstance = null;
    private volatile boolean isInit = false;

    public VisitorClass(String path, String[] libs) {
        super(Opcodes.ASM9);
        this.path = path;
        this.libs = libs;
    }


    public ClassVs getClassVsInstance() {
        return isInit ? ClassVsInstance : null;
    }

    //类的基本信息
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        ClassVsInstance = new ClassVs(name, superName, interfaces);
        ClassVsInstance.setPath(this.path);
        ClassVsInstance.setLibs(this.libs);
        ClassVsInstance.setModifier(getModifier(access));
        //设置类的修饰符
        boolean isStatic = (access & Opcodes.ACC_STATIC) != 0;
        boolean isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
        boolean isEnum = (access & Opcodes.ACC_ENUM) != 0;
        boolean isAbstract = (access & Opcodes.ACC_ABSTRACT) != 0;
        boolean isFinal = (access & Opcodes.ACC_FINAL) != 0;
        boolean isAnnotation = (access & Opcodes.ACC_ANNOTATION) != 0;
        boolean isModule = (access & Opcodes.ACC_MODULE) != 0;
        boolean isInnerClass = name.contains("$");//是否是内部类
        ClassVsInstance.setStatic(isStatic);
        ClassVsInstance.setInterface(isInterface);
        ClassVsInstance.setEnum(isEnum);
        ClassVsInstance.setAbstract(isAbstract);
        ClassVsInstance.setAnnotation(isAnnotation);
        ClassVsInstance.setFinal(isFinal);
        ClassVsInstance.setModule(isModule);
        ClassVsInstance.setInnerClass(isInnerClass);
    }

    //类的注解信息
    /*
        等待考察的方法
        visit(String name, Object value)：访问注解中的普通属性。name参数表示属性名，value参数表示属性值。
        visitEnum(String name, String descriptor, String value)：访问枚举类型的属性。name参数表示属性名，descriptor参数表示枚举类型的描述符，value参数表示枚举常量的名称。
        visitAnnotation(String name, String descriptor)：访问注解类型的属性。name参数表示属性名，descriptor参数表示注解类型的描述符。
        visitArray(String name)：访问数组类型的属性。name参数表示属性名。
        visitEnd()：访问注解结束
     */
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVs vs = new AnnotationVs(desc, visible);
        ClassVsInstance.setAnnotationNameSource(vs);
        return new VisitorAnnotation(vs);
    }

    //类的字段
    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        Type type = Type.getType(desc);
        String className = type.getClassName();
        //处理参数为数组的情况,添加数组后缀
        if (className.contains("[]")) {
            className = className + "&array";
        }
        FieldVs fieldVs = new FieldVs(name, className);
        ClassVsInstance.setField(fieldVs);
        //设置字段所属的类
        fieldVs.setClassVs(ClassVsInstance);
        // 获取可见程度
        fieldVs.setModifier(getModifier(access));
        //限制符
        boolean isStatic = (access & Opcodes.ACC_STATIC) != 0;
        boolean isFinal = (access & Opcodes.ACC_FINAL) != 0;
        boolean isVolatile = (access & Opcodes.ACC_VOLATILE) != 0;
        fieldVs.setStatic(isStatic);
        fieldVs.setFinal(isFinal);
        fieldVs.setVolatile(isVolatile);

        // 解析泛型
        if (signature != null) {
            GenericType[] types = GenericType.getGenericTypes(signature);
            if (types != null) for (GenericType genericType : types) {
                fieldVs.setGenericType(genericType);
            }
        }

        return new VisitorAnnotationField(fieldVs);
    }


    //获取可见程度
    private static Modifier getModifier(int access) {
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            return Modifier.Public;
        } else if ((access & Opcodes.ACC_PRIVATE) != 0) {
            return Modifier.Private;
        } else if ((access & Opcodes.ACC_PROTECTED) != 0) {
            return Modifier.Protected;
        } else {
            return Modifier.Default;
        }
    }

    //方法
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVs methodVs = new MethodVs(name, desc);
        ClassVsInstance.setMethod(methodVs);
        //设置方法的this引用
        methodVs.setThisClass(ClassVsInstance);
        // 获取方法的可见程度
        methodVs.setModifier(getModifier(access));
        //方法返回值
        String returnType = desc.replaceAll("\\(.*\\)", "").trim();

        if (returnType.contains("[")) returnType += "&array";

        if (!returnType.isEmpty()) {
            if (returnType.startsWith("L")) returnType = returnType.replaceFirst("L", "").trim();
            returnType = returnType.replaceAll("\\(.*\\).", "").trim();
            methodVs.setReturn(returnType);
        } else {
            throw new IllegalArgumentException("Method 返回值解析失败,返回值为 null");
        }
        //方法参数
        Type[] argTypes = Type.getArgumentTypes(desc);
        for (Type argType : argTypes) {
            String className = argType.getClassName();
            if (className.contains("[]")) className += "&array";

            String argName = className.replaceAll("[\\[|\\]]", "");
            methodVs.setParameterType(argName);
        }
        //方法中抛出的异常的异常
        if (exceptions != null) for (String exception : exceptions) {
            String exceptionName = exception.replaceAll("[\\[|\\]]", "");
            methodVs.setThrowException(exceptionName);
        }
        //修饰符
        boolean isStatic = (access & Opcodes.ACC_STATIC) != 0;
        boolean isFinal = (access & Opcodes.ACC_FINAL) != 0;
        boolean isSynthetic = (access & Opcodes.ACC_SYNTHETIC) != 0;
        boolean isNative = (access & Opcodes.ACC_NATIVE) != 0;
        methodVs.setSynthetic(isSynthetic);
        methodVs.setNative(isNative);
        methodVs.setStatic(isStatic);
        methodVs.setFinal(isFinal);
        methodVs.setSignature(signature);
        if (signature != null) {
            GenericType[] types = GenericType.getGenericTypes(signature);
            if (types != null) for (GenericType genericType : GenericType.getGenericTypes(signature)) {
                methodVs.setGenericType(genericType);
            }
        }
        return new VisitorMethod(methodVs);
    }



    @Override
    public void visitEnd() {
        isInit = true;
        super.visitEnd();
    }
}