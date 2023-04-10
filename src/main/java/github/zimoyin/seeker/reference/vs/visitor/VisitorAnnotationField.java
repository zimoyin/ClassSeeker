package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.ClassReferencePacket;
import lombok.Getter;
import org.objectweb.asm.*;

@Getter
class VisitorAnnotationField extends FieldVisitor {
    private final FieldVs field;
    protected VisitorAnnotationField(FieldVs fieldVs) {
        super(Opcodes.ASM9);
        this.field = fieldVs;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        field.setAnnotation(descriptor);
        return super.visitAnnotation(descriptor, visible);
    }
}
