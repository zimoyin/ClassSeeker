package github.zimoyin.seeker;

import github.zimoyin.seeker.find.FindClass;
import github.zimoyin.seeker.reference.ClassReaderUtil;
import github.zimoyin.seeker.reference.ClassReferencePacket;
import github.zimoyin.seeker.reference.visitor.ClassReferenceVisitor;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ClassSeeker {
    private ClassSeeker() {
    }

    /**
     * 获取所有包与类
     */
    public final static String ClassALL = "";

    /**
     * 查找所有class
     *
     * @return 查找到的类
     */
    public static List<String> findClassAll() throws IOException {
        return findClassAll(null);
    }

    /**
     * 查找所有class
     *
     * @param jarPath jar 路径
     * @return 查找到的类
     */
    public static List<String> findClassAll(String jarPath) throws IOException {
        return findClass(ClassALL, jarPath);
    }

    /**
     * 查找指定的类或者包。提供提供的包或类的前缀进行搜索
     *
     * @param packetOrClsName 包名 或者类名
     * @return 查找到的类
     */
    public static List<String> findClass(@NonNull String packetOrClsName) throws IOException {
        return findClass(packetOrClsName, null);
    }

    /**
     * 查找指定的类或者包。提供提供的包或类的前缀进行搜索
     *
     * @param packetOrClsName 包名 或者类名
     * @param jarPath         jar 路径
     * @return 查找到的类
     */
    public static List<String> findClass(@NonNull String packetOrClsName, String jarPath) throws IOException {
        return findClass(packetOrClsName, jarPath, (Filter) null);
    }


    /**
     * 查找指定的类或者包。提供提供的包或类的前缀进行搜索
     *
     * @param packetOrClsName 包名 或者类名
     * @param jarPath         jar 路径
     * @param filters         过滤器
     * @return 查找到的类
     */
    public static List<String> findClass(@NonNull String packetOrClsName, String jarPath, Filter... filters) throws IOException {
        List<String> list;
        //查找所有类
        if (jarPath != null) list = FindClass.getJarClassName(jarPath);
        else list = FindClass.getClazzNameForURL(packetOrClsName, true);
        //如果过滤器为null，则默认放行
        Stream<String> stream = list.stream().filter(Objects::nonNull)
                .filter(s -> s.contains(packetOrClsName));
        if (filters != null) for (Filter filter : filters) {
            if (filter == null) continue;
            stream = stream.filter(s -> filter.test(buildClassReferencePacket(s, jarPath)));
        }

        ClassReaderUtil.close(jarPath);
        return stream.collect(Collectors.toList());
    }


    private static ClassReferencePacket buildClassReferencePacket(String className, String path) {
        try {
            return ClassReferenceVisitor.getClassReference(className, path).getPacket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
