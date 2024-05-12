package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodLocalVariableVs extends GeneralImpl implements GeneralMethodParameter {
    private final String name;
    private final String type;
    private final ArrayList<AnnotationVs> AnnotationsSource;

    public MethodLocalVariableVs(String name, String type) {
        this.name = name;
        this.type = type;
        this.AnnotationsSource = new ArrayList<>();
    }

    public MethodLocalVariableVs(String name, String type, ArrayList<AnnotationVs> annotations) {
        this.name = name;
        this.type = type;
        this.AnnotationsSource = Objects.requireNonNullElseGet(annotations, ArrayList::new);
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
    public AnnotationVs[] getAnnotations() {
        return AnnotationsSource.toArray(new AnnotationVs[0]);
    }

    @Override
    public AnnotationVs getAnnotations(String name) {
        return AnnotationsSource.stream().filter(s -> s.getName().equals(name)).distinct().findFirst().orElse(null);
    }

    @Override
    public AnnotationVs getAnnotations(AnnotationVs av) {
        return AnnotationsSource.stream().filter(s -> s.equals(av)).distinct().findFirst().orElse(null);
    }

    @Override
    public AnnotationVs getAnnotations(Class<?> av) {
        String name = av.getSimpleName();
        return AnnotationsSource.stream().filter(s -> s.getName().equals(name)).distinct().findFirst().orElse(null);
    }

    @Override
    public boolean isContainAnnotation(String annotation) {
        return Arrays.stream(getAnnotations()).map(AnnotationVs::getName).collect(Collectors.toList()).contains(annotation);
    }


    @Override
    public ArrayList<String> getReferences() {
        ArrayList<String> refs = new ArrayList<String>();
        String ref = getType().replaceAll("[\\[|\\]]", "");
        ref = ref.replaceAll("[\\[|\\]]", "");
        refs.add(ref);
        return refs;
    }
}
