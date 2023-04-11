package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;

import java.util.ArrayList;
import java.util.Arrays;

public class MethodLocalVariableVs extends GeneralImpl implements GeneralMethodParameter {
    private final String name;
    private final String type;
    private final ArrayList<String> AnnotationsSource;

    public MethodLocalVariableVs(String name, String type) {
        this.name = name;
        this.type = type;
        this.AnnotationsSource = new ArrayList<>();
    }

    public MethodLocalVariableVs(String name, String type, ArrayList<String> annotations) {
        this.name = name;
        this.type = type;
        this.AnnotationsSource = annotations;
    }

    @Override
    public String toString() {
        return "ParameterVs{" +
                "name='" + name + '\'' +
                ", type='" + getType() + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSimpleName() {
        return getName();
    }

    @Override
    public String getModifier() {
        return null;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public String getType() {
        return getTypeClassNameGI(type);
    }

    @Override
    public String[] getAnnotations() {
        return AnnotationsSource.stream().map(this::getTypeClassNameGI).toArray(String[]::new);
    }

    @Override
    public boolean isAnnotation(String annotation) {
        return Arrays.asList(getAnnotations()).contains(annotation);
    }
}
