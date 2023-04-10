package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;
import lombok.Getter;
import lombok.ToString;

public class ParameterVs implements GeneralMethodParameter {
    private final String name;
    private final String type;

    public ParameterVs(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ParameterVs{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String[] getAnnotations() {
        return new String[0];
    }

    @Override
    public boolean isAnnotation(String annotation) {
        return false;
    }
}
