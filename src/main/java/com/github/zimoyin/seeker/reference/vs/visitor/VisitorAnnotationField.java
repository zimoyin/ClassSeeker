package com.github.zimoyin.seeker.reference.vs.visitor;

import lombok.Getter;
import lombok.val;
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
        AnnotationVs annotationVs = new AnnotationVs(descriptor, visible);
        field.setAnnotation(annotationVs);
        return new VisitorAnnotation(annotationVs);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        AnnotationVs annotationVs = new AnnotationVs(descriptor, visible);
        field.setTypeAnnotation(annotationVs);
        return new VisitorAnnotation(annotationVs);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }
}
