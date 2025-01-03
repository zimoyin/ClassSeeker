package com.github.zimoyin.seeker.reference;

import com.github.zimoyin.seeker.reference.vs.visitor.ClassVs;
import com.github.zimoyin.seeker.reference.vs.visitor.VisitorClass;
import lombok.NonNull;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

public class ClassVsFactory {
    private ClassVsFactory(){}

    /**
     * 获取类引用
     */
    public static ClassVs getClassVS(byte[] bytes) throws IOException {
        ClassReader classReader = new ClassReader(bytes);
        VisitorClass visitor = new VisitorClass("", new String[]{""});
        classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
        return visitor.getClassVsInstance();
    }

    /**
     * 获取类引用
     */
    public static ClassVs getClassVS(Class<?> cls) throws IOException {
        ClassReader classReader = new ClassReader(cls.getTypeName());
        VisitorClass visitor = new VisitorClass("", new String[]{""});
        classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
        return visitor.getClassVsInstance();
    }


    /**
     * 获取类引用
     *
     * @param cls   类名
     * @param paths jar路径
     */
    public static ClassVs getClassVS(@NonNull String cls, String... paths) throws IOException {
        byte[] bytes = null;
        String path = "";
        if (paths != null && paths.length > 0) {
            for (String path0 : paths) {
                if (bytes != null) break;
                path = path0;
                bytes = ClassReaderUtil.readClassBytes(cls, new String[]{path0}, true);
            }
        } else {
            bytes = ClassReaderUtil.readClassBytes(cls, null, true);
        }

        ClassReader classReader = new ClassReader(bytes);
        VisitorClass visitor = new VisitorClass(path, paths);
        classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
        return visitor.getClassVsInstance();
    }
}
