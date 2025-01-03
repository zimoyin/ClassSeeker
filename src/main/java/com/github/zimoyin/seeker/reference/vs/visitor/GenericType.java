package com.github.zimoyin.seeker.reference.vs.visitor;

import org.objectweb.asm.Type;

import java.util.ArrayList;

/**
 * @author : zimo
 * @date : 2024/12/17
 */
public class GenericType {
    private final String source;
    private final Type type;

    private GenericType(String source) {
        this.source = source;
        type = Type.getType(source);
    }

    public String getSource() {
        return source;
    }


    public Type getType() {
        return type;
    }

    public GenericType[] getGenericTypes() {
        return getGenericTypes(source);
    }

    @Override
    public String toString() {
        return getType().getClassName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        GenericType that = (GenericType) o;
        return source.equals(that.source);
    }

    @Override
    public int hashCode() {
        return source.hashCode();
    }

    public static GenericType[] getGenericTypes(String source) {
        if (source == null || !source.contains("<") || source.indexOf(">") < 1) return null;
        source = source.substring(source.indexOf("<") + 1, source.lastIndexOf(">"));
        String[] descriptions = getDescriptions(source);

        ArrayList<GenericType> list = new ArrayList<>();
        for (String description : descriptions) {
            list.add(new GenericType(description));
        }
        GenericType[] aa = list.toArray(GenericType[]::new);

        return list.toArray(GenericType[]::new);
    }

    private static String[] getDescriptions(String input) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> lines = new ArrayList<>();

        int depth = 0;
        boolean isStart = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 'L') isStart = true;
            if (c == '<') depth++;
            if (c == ';') {
                if (depth == 0) {
                    isStart = false;
                }
                depth--;
                if (depth < 0) depth = 0;
            }
            if (isStart) builder.append(c);
            if (c == '[') {
                isStart = true;
                builder.append(input.charAt(++i));
            }
            if (!isStart) {
                lines.add(builder.toString());
                builder = new StringBuilder();
            }
        }
        return lines.stream().filter(s -> !s.isEmpty()).toArray(String[]::new);
    }
}
