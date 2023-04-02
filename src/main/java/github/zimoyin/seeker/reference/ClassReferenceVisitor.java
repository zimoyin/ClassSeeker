package github.zimoyin.seeker.reference;

import lombok.Getter;
import lombok.val;
import org.objectweb.asm.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 类引用分析
 */
public class ClassReferenceVisitor extends ClassVisitor {
    private final HashSet<String> classSources = new HashSet<>();
    private final ClassReferencePacket packet = new ClassReferencePacket();

    /**
     * 获取类引用
     */
    public static ClassReferenceVisitor getClassReference(byte[] bytes) throws IOException {
        ClassReader classReader = new ClassReader(bytes);
        ClassReferenceVisitor visitor = new ClassReferenceVisitor();
        classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
        return visitor;
    }

    /**
     * 获取类引用
     *
     * @param cls   类名
     * @param paths jar路径
     */
    public static ClassReferenceVisitor getClassReference(String cls, String... paths) throws IOException {
        ClassReader classReader = new ClassReader(ClassReaderUtil.readClassBytes(cls, paths, true));
        ClassReferenceVisitor visitor = new ClassReferenceVisitor();
        classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
        return visitor;
    }

    private ClassReferenceVisitor() {
        super(Opcodes.ASM9);
    }

    public List<String> getClassAll() {
        return classSources.stream()
                .filter(Objects::nonNull)
                .map(s -> s.replaceAll("[\\[|\\]]", ""))
                .map(s -> s.replaceAll("[\\(|\\)]", ""))
                .map(s -> s.replaceAll("/", "."))
                .map(s -> s.replaceAll(";", ""))
                .map(s -> {
                    if (s.startsWith("L")) return s.substring(1);
                    return s;
                })
                .filter(s -> !s.equals("int"))
                .filter(s -> !s.equals("I"))
                .filter(s -> !s.equals("long"))
                .filter(s -> !s.equals("L"))
                .filter(s -> !s.equals("char"))
                .filter(s -> !s.equals("C"))
                .filter(s -> !s.equals("double"))
                .filter(s -> !s.equals("D"))
                .filter(s -> !s.equals("float"))
                .filter(s -> !s.equals("F"))
                .filter(s -> !s.equals("boolean"))
                .filter(s -> !s.equals("B"))
                .filter(s -> !s.equals("byte"))
                .collect(Collectors.toList());
    }


    public ClassReferencePacket getPacket() {
        return this.packet.build();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        classSources.add(name);
        classSources.add(superName);
        this.packet.getFatherCollection().add(superName);
        this.packet.setClassName(name);
//            log.debug("Class: " + name + " extends: " + superName);
        for (String iface : interfaces) {
            classSources.add(iface);
            packet.getInterfaceCollection().add(iface);
//                log.debug("Implements: " + iface);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        classSources.add(desc.substring(1));
        packet.getClassAnnotationCollection().add(desc.substring(1));
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        Type type = Type.getType(desc);
        packet.getFieldCollection().put(name, type.getClassName());
//            log.debug("Field: " + name + " Type: " + type.getClassName());
        classSources.add(type.getClassName());
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        String returnType = desc.replaceAll("\\(.*\\)", "").trim();
        if (!returnType.isEmpty()) {
            if (returnType.startsWith("L")) returnType = returnType.replaceFirst("L", "").trim();
            returnType = returnType.replaceAll("\\(.*\\).", "").trim();
//                log.debug("return Type: " + returnType);
            packet.getReturnValueCollection().put(name, returnType);
            classSources.add(returnType);
        }

        Type[] argTypes = Type.getArgumentTypes(desc);
//            log.debug("Method: " + name + " Args: " + argTypes.length);
        for (Type argType : argTypes) {
//                log.debug("Arg Type: " + argType.getClassName().replaceAll("[\\[|\\]]", ""));
            String argName = argType.getClassName().replaceAll("[\\[|\\]]", "");
            classSources.add(argName);
            List<String> list = packet.getMethodParameterCollection().getOrDefault(name, new ArrayList<>());
            list.add(argName);
            packet.getMethodParameterCollection().put(name, list);
        }
        if (exceptions != null) for (String exception : exceptions) {
//                log.debug("exception: " + exception.replaceAll("[\\[|\\]]", ""));
            String exceptionName = exception.replaceAll("[\\[|\\]]", "");
            classSources.add(exceptionName);
            List<String> list = packet.getMethodExceptionCollection().getOrDefault(name, new ArrayList<>());
            list.add(exceptionName);
            packet.getMethodExceptionCollection().put(name, list);
        }
        return new MyMethodVisitor(classSources, packet, name);
    }

    private static class MyMethodVisitor extends MethodVisitor {
        @Getter
        private final HashSet<String> classSources;
        @Getter
        private final ClassReferencePacket packet;
        @Getter
        private final String methodName;

        public MyMethodVisitor(HashSet<String> hashSet, ClassReferencePacket packet, String name) {
            super(Opcodes.ASM9);
            this.classSources = hashSet;
            this.packet = packet;
            this.methodName = name;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//                log.debug("Method Annotation: " + desc.substring(1));
            val annName = desc.substring(1);
            classSources.add(annName);
            List<String> list = packet.getMethodAnnotationCollection().getOrDefault(methodName, new ArrayList<>());
            list.add(annName);
            packet.getMethodAnnotationCollection().put(methodName, list);
            return super.visitAnnotation(desc, visible);
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
//                log.debug("Parameter Annotation: " + desc.substring(1));
            String para = desc.substring(1);
            classSources.add(para);
            List<String> list = packet.getMethodParameterCollection().getOrDefault(methodName, new ArrayList<>());
            list.add(para);
            packet.getMethodParameterCollection().put(methodName, list);
            return super.visitParameterAnnotation(parameter, desc, visible);
        }

        //方法内部的变量
        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            Type type = Type.getType(desc);
//                log.debug("Local Variable: " + name + " Type: " + type.getClassName().replaceAll("[\\[|\\]]", ""));
            String value = type.getClassName().replaceAll("[\\[|\\]]", "");
            classSources.add(value);
            HashMap<String, String> hashMap = packet.getLocalVariableTypeCollection().getOrDefault(methodName, new HashMap<>());
            hashMap.put(name, value);
            packet.getLocalVariableTypeCollection().put(methodName, hashMap);
            super.visitLocalVariable(name, desc, signature, start, end, index);
        }
    }
}