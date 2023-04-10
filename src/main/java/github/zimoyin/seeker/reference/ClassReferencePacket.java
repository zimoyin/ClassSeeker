package github.zimoyin.seeker.reference;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字符
 */
@Data
public class ClassReferencePacket {
    /**
     * Class Name
     */
    private String className;
    /**
     * 类中接口集合
     */
    private List<String> InterfaceCollection = new ArrayList<String>();
    /**
     * 类中字段集合
     */
    private HashMap<String, String> FieldCollection = new HashMap<String, String>();
    /**
     * 父亲类集合
     */
    private List<String> FatherCollection = new ArrayList<String>(1);
    /**
     * 类上面的注解集合
     */
    private List<String> ClassAnnotationCollection = new ArrayList<String>();
    /**
     * 方法集合
     */
    private HashMap<String, String> MethodCollection = new HashMap<String, String>();
    /**
     * 方法上注解集合，这是个HashMap
     */
    private HashMap<String, List<String>> MethodAnnotationCollection = new HashMap<String, List<String>>();
    /**
     * 方法参数集合
     */
    private HashMap<String, List<String>> MethodParameterCollection = new HashMap<>();
    /**
     * 方法抛出异常集合
     */
    private HashMap<String, List<String>> MethodExceptionCollection = new HashMap<>();
    /**
     * 方法内定义的变量类型的集合
     */
    private HashMap<String, HashMap<String, String>> LocalVariableTypeCollection = new HashMap<>();
    /**
     * 方法返回值集合
     */
    private HashMap<String, String> ReturnValueCollection = new HashMap<>();

    /**
     * 构建
     */
    public ClassReferencePacket build() {
        InterfaceCollection = getClassBuild(InterfaceCollection);
        FieldCollection.replaceAll((k, v) -> getClassBuild(v));
        FatherCollection = getClassBuild(FatherCollection);
        ClassAnnotationCollection = getClassBuild(ClassAnnotationCollection);
        MethodCollection.replaceAll((k, v) -> getClassBuild(v));
        MethodAnnotationCollection.replaceAll((k, v) -> getClassBuild(v));
        MethodParameterCollection.replaceAll((k, v) -> getClassBuild(v));
        MethodExceptionCollection.replaceAll((k, v) -> getClassBuild(v));
        for (Map.Entry<String, HashMap<String, String>> next : LocalVariableTypeCollection.entrySet()) {
            HashMap<String, String> value = next.getValue();
            value.replaceAll((k, v) -> getClassBuild(v));
            LocalVariableTypeCollection.put(next.getKey(), value);
        }
        ReturnValueCollection.replaceAll((k, v) -> getClassBuild(v));
        this.className = getClassBuild(className);
        return this;
    }


    private List<String> getClassBuild(List<String> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(s -> s.replaceAll("[\\[|\\]]", ""))
                .map(s -> s.replaceAll("[\\(|\\)]", ""))
                .map(s -> s.replaceAll("/", "."))
                .map(s -> s.replaceAll(";", ""))
                .map(s -> {
                    if (s.startsWith("L")) return s.substring(1);
                    return s;
                })
                .map(this::replaceAll)
                .collect(Collectors.toList());
    }

    private String getClassBuild(String str) {
        List<String> list = new ArrayList<>();
        list.add(str);
        return list.stream()
                .filter(Objects::nonNull)
                .map(s -> s.replaceAll("[\\[|\\]]", ""))
                .map(s -> s.replaceAll("[\\(|\\)]", ""))
                .map(s -> s.replaceAll("/", "."))
                .map(s -> s.replaceAll(";", ""))
                .map(s -> {
                    if (s.startsWith("L")) return s.substring(1);
                    return s;
                })
                .map(this::replaceAll)
                .findFirst()
                .orElse(null);
    }

    private String replaceAll(String s) {
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
