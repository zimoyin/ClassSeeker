package github.zimoyin.seeker.reference.vs.visitor;

import lombok.val;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class VisitorMethod extends MethodVisitor {

    private final MethodVs method;

    public VisitorMethod(MethodVs methodVs) {
        super(Opcodes.ASM9);
        this.method = methodVs;
    }

    //方法上的注解
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        val annName = desc.substring(1);
        method.setAnnotation(annName);
        return super.visitAnnotation(desc, visible);
    }

    //方法参数上的注解
    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        String para = desc.substring(1);
        method.setParameterAnnotation(para);
        return super.visitParameterAnnotation(parameter, desc, visible);
    }

    //方法内部的变量
    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        //TODO: 处理参数为数组的情况
        Type type = Type.getType(desc);
        String value = type.getClassName().replaceAll("[\\[|\\]]", "");
        method.setLocalVariable(name,value);
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }


}
