package github.zimoyin.seeker.reference.vs.visitor;

import lombok.val;
import org.objectweb.asm.*;

class VisitorMethod extends MethodVisitor {
//MethodVisitor 下的 visitLocalVariable 方法
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
        Type type = Type.getType(desc);
        String className = type.getClassName();
        if (className.contains("[]")) className += "&array";
        String value = className.replaceAll("[\\[|\\]]", "");
//        if (method.getName().equals("main")) {
//            System.err.println(index + ": " + name + " = " + value);
//        }
        //如果变量是this
        if (index == 0 && name.equals("this")) {
            method.setClassName(value);
            return;
        }
        //如果变量是参数列表内的变量
        int paramSize = method.getParameterTypeNameSource().size();
        if (method.isStatic()) {
            if (index >= 0 && index < paramSize) {
                method.getParameterNameSourceMap().put(name, value);
                return;
            }
        } else {
            if (index > 0 && index <= paramSize) {
                method.getParameterNameSourceMap().put(name, value);
                return;
            }
        }

        //如果是方法内定义的变量，且变量作用域为整个方法
        method.setLocalVariable(name, value);
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        method.setTryCatchBlockException(type);
        super.visitTryCatchBlock(start, end, handler, type);
    }
}
