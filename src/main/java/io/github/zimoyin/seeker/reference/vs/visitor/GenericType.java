package io.github.zimoyin.seeker.reference.vs.visitor;

import lombok.Getter;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author : zimo
 * @date : 2024/12/17
 */
@Getter
public class GenericType {
    private final String source;
    private final GType type;

    private GenericType(String source, GType type) {
        this.source = source;
        this.type = type;
    }

    public GenericType[] getGenericTypes() {
        return getGenericTypes(source);
    }

    @Override
    public String toString() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        GenericType that = (GenericType) o;
        return source.equals(that.source) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public static GenericType[] getGenericTypes(String source) {
        VisitorSignature signature = VisitorSignature.visitor(source);
        ArrayList<GenericType> list = new ArrayList<>();
        signature.getGenericTypes().forEach(s -> {
            if (s != null) list.add(new GenericType(s, GType.UNKNOWN_TYPE));
        });
        return list.toArray(GenericType[]::new);
    }

    enum GType {
        RETURN_TYPE,
        PARAMETER_TYPE,
        EXCEPTION_TYPE,
        BASE_TYPE,
        METHOD_TYPE,
        UNKNOWN_TYPE
    }
}
