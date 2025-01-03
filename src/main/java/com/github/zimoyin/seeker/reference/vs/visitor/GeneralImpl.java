package com.github.zimoyin.seeker.reference.vs.visitor;

import com.github.zimoyin.seeker.reference.vs.interfaces.General;

public abstract class GeneralImpl implements General {

    @Override
    public int hashCode() {
        return getName().hashCode();
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
