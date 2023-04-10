package github.zimoyin.seeker.reference.visitor;

import github.zimoyin.seeker.reference.ClassReferencePacket;
import lombok.Getter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

@Getter
class AnnotationFieldScanner extends FieldVisitor {

    private final String FieldName;
    private final ClassReferencePacket Packet;

    protected AnnotationFieldScanner(String fieldName, ClassReferencePacket packet) {
        super(Opcodes.ASM9);
        this.Packet = packet;
        this.FieldName = fieldName;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println(FieldName+": "+descriptor);
        return super.visitAnnotation(descriptor, visible);
    }

}
