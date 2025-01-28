package io.github.zimoyin.seeker.reference.vs.visitor;

import lombok.Getter;
import org.objectweb.asm.*;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.List;

@Getter
class VisitorSignature extends SignatureVisitor {
    // 保存方法的泛型类型
    private final List<String> genericTypes = new ArrayList<>();

    private boolean visitingParameter = false;
    private boolean visitingReturn = false;
    private boolean visitingException = false;

    // 构造函数
    protected VisitorSignature() {
        super(Opcodes.ASM9);
    }

    // 解析签名的静态方法
    public static VisitorSignature visitor(String signature) {
        VisitorSignature visitorSignature = new VisitorSignature();
        new SignatureReader(signature).accept(visitorSignature);
        return visitorSignature;
    }

    @Override
    public void visitFormalTypeParameter(String name) {
        // 方法的泛型类型参数名称
    }

    @Override
    public SignatureVisitor visitParameterType() {
        visitingParameter = true;
        return this;
    }

    @Override
    public SignatureVisitor visitReturnType() {
        visitingReturn = true;
        return this;
    }

    @Override
    public SignatureVisitor visitExceptionType() {
        visitingException = true;
        return this;
    }

    @Override
    public void visitClassType(String name) {
        genericTypes.add(name.replace('/', '.'));
    }

    @Override
    public void visitEnd() {
        // 重置状态
        visitingParameter = false;
        visitingReturn = false;
        visitingException = false;
    }
}
