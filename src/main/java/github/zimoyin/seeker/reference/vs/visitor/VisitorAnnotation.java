package github.zimoyin.seeker.reference.vs.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

/**
 * @author : zimo
 * @date : 2024/05/11
 */
public class VisitorAnnotation extends AnnotationVisitor {
    private final AnnotationVs annotationVs;

    protected VisitorAnnotation(AnnotationVs annotationVs) {
        super(Opcodes.ASM9);
        this.annotationVs = annotationVs;
    }

    @Override
    public AnnotationVisitor getDelegate() {
        return super.getDelegate();
    }

    @Override
    public void visit(String name, Object value) {
        annotationVs.setValue(name, value);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        annotationVs.setValue(name, new AnnotationVs.EnumVs(descriptor, value));
        super.visitEnum(name, descriptor, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        AnnotationVs vs = new AnnotationVs(descriptor, true);
        annotationVs.setValue(name, vs);
        return new VisitorAnnotation(vs);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        ArrayList<Object> list = new ArrayList<>();
        annotationVs.setValue(name, list);
        return new AnnotationVsArray(list);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    private static class AnnotationVsArray extends AnnotationVisitor {
        private final ArrayList<Object> list0;
        protected AnnotationVsArray(ArrayList<Object> list) {
            super(Opcodes.ASM9);
            this.list0 = list;
        }

        @Override
        public void visit(String name, Object value) {
            list0.add(value);
            super.visit(name, value);
        }
    }
}
