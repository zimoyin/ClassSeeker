package github.zimoyin.seeker;

import github.zimoyin.seeker.find.FindClass;
import github.zimoyin.seeker.reference.ClassReaderUtil;
import github.zimoyin.seeker.reference.ClassVsFactory;
import github.zimoyin.seeker.reference.vs.visitor.ClassVs;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ClassSeeker {
    /**
     * 当类被ASM读取时发生了异常，在AbnormalBlocking为true的时候会抛出异常，并在某段程序中对程序进行中断
     * 如果为false不会抛出异常，而是打印异常，并返回null，如果处理程序没有处理null的逻辑会抛出空指针异常
     */
    public static boolean AbnormalBlocking = true;

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
    public static List<String> findClassAll(Filter... filter) throws IOException {
        return findClass(ClassALL, null, filter);
    }

    /**
     * 查找所有class
     * <p>
     * 支持kotlin调用
     * ClassSeeker.findClass("github.zimoyin.ffm") {
     * true
     * }
     *
     * @return 查找到的类
     */
    public static List<String> findClassAll(Filter filter) throws IOException {
        return findClass(ClassALL, null, filter);
    }

    /**
     * 查找所有class
     *
     * @return 查找到的类
     */
    public static List<String> findClassAll() throws IOException {
        return findClassAll((String) null);
    }

    /**
     * 查找所有class
     *
     * @param jarPath jar 路径
     * @return 查找到的类
     */
    public static List<String> findClassAll(String jarPath, Filter... filter) throws IOException {
        return findClass(ClassALL, jarPath, filter);
    }

    /**
     * 查找所有class
     * <p>
     * 支持kotlin调用
     * ClassSeeker.findClass("github.zimoyin.ffm") {
     * true
     * }
     *
     * @param jarPath jar 路径
     * @return 查找到的类
     */
    public static List<String> findClassAll(String jarPath, Filter filter) throws IOException {
        return findClass(ClassALL, jarPath, filter);
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
        return findClass(packetOrClsName, null, (Filter) null);
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
     * @param filters         过滤器
     * @return 查找到的类
     */
    public static List<String> findClass(@NonNull String packetOrClsName, Filter... filters) throws IOException {
        return findClass(packetOrClsName, null, filters);
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
        //过滤符合要求的类
        list = list.stream().filter(s -> s != null && !s.isEmpty()).filter(s -> s.startsWith(packetOrClsName)).collect(Collectors.toList());
        //如果过滤器为null，则默认放行
        Stream<String> stream = list.stream().filter(Objects::nonNull).filter(s -> s.contains(packetOrClsName));
        if (filters != null) for (Filter filter : filters) {
            if (filter == null) continue;
            stream = stream.filter(s -> filter.test(buildClassVs(s, jarPath)));
        }

        ClassReaderUtil.close(jarPath);
        return stream.collect(Collectors.toList());
    }

    /**
     * 查找指定的类或者包。提供提供的包或类的前缀进行搜索
     * <p>
     * 支持kotlin 调用
     * ClassSeeker.findClass("github.zimoyin.ffm") {
     * true
     * }
     *
     * @param packetOrClsName 包名 或者类名
     * @param filter          过滤器
     * @return 查找到的类
     * @throws IOException
     */
    public static List<String> findClass(@NonNull String packetOrClsName, Filter filter) throws IOException {
        return findClass(packetOrClsName, null, filter);
    }

    /**
     * 查找指定的类或者包。提供提供的包或类的前缀进行搜索
     * <p>
     * 支持kotlin 调用
     * ClassSeeker.findClass("github.zimoyin.ffm",null) {
     * true
     * }
     *
     * @param packetOrClsName 包名 或者类名
     * @param jarPath         jar 路径
     * @param filter          过滤器
     * @return 查找到的类
     */
    public static List<String> findClass(@NonNull String packetOrClsName, String jarPath, Filter filter) throws IOException {
        List<String> list;
        //查找所有类
        if (jarPath != null) list = FindClass.getJarClassName(jarPath);
        else list = FindClass.getClazzNameForURL(packetOrClsName, true);
        //过滤符合要求的类
        list = list.stream().filter(s -> s != null && !s.isEmpty()).filter(s -> s.startsWith(packetOrClsName)).collect(Collectors.toList());
        //如果过滤器为null，则默认放行
        Stream<String> stream = list.stream().filter(Objects::nonNull).filter(s -> s.contains(packetOrClsName));
        if (filter != null) stream = stream.filter(s -> filter.test(buildClassVs(s, jarPath)));
        ClassReaderUtil.close(jarPath);
        return stream.collect(Collectors.toList());
    }


    private static ClassVs buildClassVs(String className, String path) {
        try {
            return ClassVsFactory.getClassVS(className, path);
        } catch (IOException e) {
            if (AbnormalBlocking) {
                throw new RuntimeException(e);
            } else {
                e.printStackTrace();
            }
        }
        return null;
    }
}
