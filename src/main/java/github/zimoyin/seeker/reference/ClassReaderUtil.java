package github.zimoyin.seeker.reference;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 读取Class 字节
 * 只有读取 class 字节功能，没有加载功能
 */
public class ClassReaderUtil {
    private ClassReaderUtil() {
    }


    /**
     * 读取类文件
     *
     * @param className class 文件的全限定名
     * @param classPath class 存在的路径，可以是一个具体文件夹路径，也可以是一个Jar 路径
     * @return class 字节数组
     */
    public static byte[] readClassBytes(String className, String classPath) throws IOException {
        return readClassBytes(className, new String[]{classPath}, false);
    }

    /**
     * 读取类文件
     *
     * @param className        class 文件的全限定名
     * @param classPaths       class 存在的路径，可以是一个具体文件夹路径，也可以是一个Jar 路径。如果类在这些路径中都存在，则只返回一个
     * @param isFindSTDClasses 如果在第三方jar中没有找到，是否去标准库中找
     * @return class 字节数组
     */
    public static byte[] readClassBytes(String className, String[] classPaths, boolean isFindSTDClasses) throws IOException {
        // Search for the class in the given classpath
        byte[] bytes = null;
        if (classPaths != null) for (String path : classPaths) {
            if (path == null) continue;
            if (bytes != null && bytes.length != 0) break;
            if (path.toLowerCase().endsWith(".jar")) {
                // The class is in a JAR file
                bytes = readClassBytesFromJar(new File(path), className);
            } else {
                // The class is in a directory
                //当前路径加上类全限定名来判断文件是否存在，存在则加载，否则则忽略
                File classFile = new File(path, className.replace('.', File.separatorChar) + ".class");
                bytes = readClassBytesFromFile(classFile);
            }
        }
        //如果没有在第三方Jar中就去标准库中找
        if (bytes == null || bytes.length == 0) {
            if (!isFindSTDClasses) throw new IOException("无法在路径下读取到class文件: " + className);
            else bytes = readStream(ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class"), true);
        }
        //没有找到任何Class
        if (bytes == null || bytes.length == 0)
            throw new IOException("无法在路径下读取到class文件且标准类库中也无法找到类: " + className);
        return bytes;
    }

    private static byte[] readStream(final InputStream inputStream, final boolean close)
            throws IOException {
        if (inputStream == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[1024];
            int bytesRead;
            int readCount = 0;
            while ((bytesRead = inputStream.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, bytesRead);
                readCount++;
            }
            outputStream.flush();
            if (readCount == 1) {
                return data;
            }
            return outputStream.toByteArray();
        } finally {
            if (close) {
                inputStream.close();
            }
        }
    }

    /**
     * 从文件中读取字节
     *
     * @param classFile class 文件对象
     */
    private static byte[] readClassBytesFromFile(File classFile) {
        try (InputStream in = Files.newInputStream(classFile.toPath());
             BufferedInputStream bis = new BufferedInputStream(in);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 从JAR文件中读取类的字节码
     * 引入 JarFile 缓存，增加在同一jar中进行查找class时避免不必要的性能损耗
     *
     * @param jarFile   jar 路径
     * @param className class 全限定名
     */
    private static byte[] readClassBytesFromJar(File jarFile, String className) {
        JarFile jar = null;
        try {
            jar = getJarFileInstance(jarFile);
            JarEntry entry = jar.getJarEntry(className.replace('.', '/') + ".class");

            if (entry == null) {
//                throw new IOException("无法在jar中读取到class文件: jar:" + jarFile + File.separator + "!" + className);
                return null;
            }
            try (InputStream in = jar.getInputStream(entry);
                 BufferedInputStream bis = new BufferedInputStream(in);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                return out.toByteArray();
            }
        } catch (IOException e) {
            return null;
        } finally {
            //清理最先添加的IO
            if (CACHES.size() >= 3) {
                Iterator<Map.Entry<File, JarFile>> iterator = CACHES.entrySet().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getValue() != jar) CACHES.remove(iterator.next().getKey());
                }
            }
        }
    }


    /**
     * 获取 JarFile 实例
     */
    private synchronized static JarFile getJarFileInstance(File file) throws IOException {
        JarFile jarFile = CACHES.get(file);
        if (jarFile == null) jarFile = new JarFile(file);
        CACHES.put(file, jarFile);
        return jarFile;
    }

    /**
     * 缓存
     */
    private static final HashMap<File, JarFile> CACHES = new HashMap<>();

    /**
     * 关闭所有流
     */
    public static void close() {
        CACHES.forEach((file, jarFile) -> {
            try {
                jarFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        CACHES.clear();
    }

    /**
     * 关闭指定的流
     * @param jarPath 文件路径
     */
    public static void close(String jarPath) {
        try {
            if (jarPath != null) {
                File jf = new File(jarPath);
                JarFile file = CACHES.get(jf);
                if (file != null) file.close();
                CACHES.remove(jf);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
