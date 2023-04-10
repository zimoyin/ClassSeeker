package github.zimoyin.seeker.reference.visitor;

import github.zimoyin.seeker.reference.ClassReferencePacket;
import lombok.Getter;
import lombok.val;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

class MyMethodVisitor extends MethodVisitor {
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
