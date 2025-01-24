package io.github.zimoyin.seeker.reference.vs.visitor;

import io.github.zimoyin.seeker.reference.vs.interfaces.General;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author : zimo
 * @date : 2024/05/11
 * 非外部连接
 */
@Getter
public class AnnotationVs extends GeneralImpl implements General {
    private final String annotationDesc;
    private final boolean isRuntimeVisible;
    private final HashMap<String, Object> values = new HashMap<>();

    public AnnotationVs(String desc, boolean visible) {
        this.annotationDesc = desc;
        this.isRuntimeVisible = visible;
    }

    public Object getValue(String name){
        return values.get(name);
    }

    @Override
    public String getName() {
        return getTypeClassNameGI(annotationDesc);
    }

    @Override
    public String getSimpleName() {
        return getTypeClassNameGI(annotationDesc);
    }

    @Override
    public String getModifier() {
        return null;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public AnnotationVs[] getAnnotations() {
        throw new RuntimeException("无实现");
    }

    @Override
    public AnnotationVs getAnnotations(String name) {
        throw new RuntimeException("无实现");
    }

    @Override
    public AnnotationVs getAnnotations(AnnotationVs av) {
        throw new RuntimeException("无实现");
    }

    @Override
    public AnnotationVs getAnnotations(Class<?> av) {
        return null;
    }

    @Override
    public boolean isContainAnnotation(String annotation) {
        throw new RuntimeException("不支持");
//        return true;
    }

    @Override
    public ArrayList<String> getReferences() {
        return null;
    }

    protected void setValue(String name, Object value) {
        values.put(name, value);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotationVs)) return false;

        AnnotationVs vs = (AnnotationVs) o;
        return getName().equals(vs.getName());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Getter
    public static class EnumVs {
        private final String enumDesc;
        private final String value;

        public EnumVs(String enumDesc, String value) {
            this.enumDesc = enumDesc;
            this.value = value;
        }

        public String getEnumName() {
            return getTypeClassNameGI(enumDesc);
        }

        protected String getTypeClassNameGI(String str) {
//        List<String> list = new ArrayList<>();
//        list.add(str);
            if (str == null) return null;
            String after = "";
            if (str.contains("&array")) {
                after = "[]";
                str = str.replaceAll("&array", "");
            }
            str = str.replaceAll("[\\[|\\]]", "");
            str = str.replaceAll("[\\(|\\)]", "");
            str = str.replaceAll("/", ".");
            str = str.replaceAll(";", "");
            if (str.startsWith("L")) str = str.substring(1);
            str = getTypeNameGI(str);
            str = str + after;
//        str = str.replaceAll("&array", "[]");

            return str;
        }

        protected String getTypeNameGI(String s) {
            if (s.equals("V")) s = void.class.getTypeName();
            if (s.equals("Z")) s = boolean.class.getTypeName();
            if (s.equals("I")) s = int.class.getTypeName();
            if (s.equals("D")) s = double.class.getTypeName();
            if (s.equals("B")) s = byte.class.getTypeName();
            if (s.equals("C")) s = char.class.getTypeName();
            if (s.equals("S")) s = short.class.getTypeName();
            if (s.equals("F")) s = float.class.getTypeName();
            if (s.equals("J")) s = long.class.getTypeName();
            return s;
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
