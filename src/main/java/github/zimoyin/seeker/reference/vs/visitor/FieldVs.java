package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralField;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@ToString
public class FieldVs extends GeneralImpl implements GeneralField {
    private final String FieldName;
    private final String FieldTypeNameSource;
    private final ArrayList<String> AnnotationNameSource = new ArrayList<String>();
    private Modifier ModifierValue;
    private boolean isStatic;
    private boolean isFinal;
    private boolean isVolatile;

    public FieldVs(String fieldName, String fieldTypeNameSource) {
        FieldName = fieldName;
        FieldTypeNameSource = fieldTypeNameSource;
    }


    @Override
    public String getName() {
        return FieldName;
    }

    @Override
    public String getSimpleName() {
        return getName();
    }

    @Override
    public String getModifier() {
        return ModifierValue.name().toLowerCase();
    }

    @Override
    public boolean isVolatile() {
        return isVolatile;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public String[] getAnnotations() {
        return AnnotationNameSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }

    @Override
    public boolean isAnnotation(String annotation) {
        return Arrays.asList(getAnnotations()).contains(annotation);
    }

    @Override
    public Object getValue() {
        throw new NullPointerException("无法获取到字段的值");
    }

    @Override
    public String getType() {
        return getTypeClassNameGI(FieldTypeNameSource);
    }

    protected void setAnnotation(String descriptor) {
        this.AnnotationNameSource.add(descriptor);
    }

    protected void setModifier(Modifier modifier) {
        this.ModifierValue = modifier;
    }

    protected void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    protected void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    protected void setVolatile(boolean isVolatile) {
        this.isVolatile = isVolatile;
    }
}
