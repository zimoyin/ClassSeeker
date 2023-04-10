package github.zimoyin.seeker.reference.vs.visitor;

import github.zimoyin.seeker.reference.vs.interfaces.General;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class GeneralImpl implements General {

    @Override
    public int hashCode() {
        return getName().hashCode();
    }


    public String getTypeClassNameGI(String str) {
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
                .map(this::getTypeNameGI)
                .findFirst()
                .orElse(null);
    }

    public String getTypeNameGI(String s) {
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
