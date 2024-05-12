package github.zimoyin.seeker.reference.vs.visitor;

import lombok.Getter;
import lombok.val;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

@Getter
class VisitorAnnotationField extends FieldVisitor {
    private final FieldVs field;

    protected VisitorAnnotationField(FieldVs fieldVs) {
        super(Opcodes.ASM9);
        this.field = fieldVs;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVs annotationVs = new AnnotationVs(descriptor, visible);
        field.setAnnotation(annotationVs);
        return new VisitorAnnotation(annotationVs);
    }
}
