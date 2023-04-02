package github.zimoyin.seeker.find;


import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 通过反射来查找包下的所有的类
 * v3.1
 */
public class FindClass {
    private static final String CLASS_SUFFIX = ".class";
    private static final String CLASS_FILE_PREFIX = File.separator + "classes" + File.separator;
    private static final String PACKAGE_SEPARATOR = ".";
    private static final boolean isLog = true;

    /**
     * 通过JAR的URL路径来解析
     *
     * @param packageName          包名
     * @param showChildPackageFlag 是否递归查询
     * @return class 的全限定名
     */
    public static List<String> getClazzNameForURL(String packageName, boolean showChildPackageFlag) {
        List<String> result;
        try {
            Enumeration<URL> urls = Collections.enumeration(FindPackage.getClassPathToURLForApplication());
            result = getClazzName0(packageName, urls, showChildPackageFlag);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 通过类加载器来解析
     *
     * @param packageName          包名
     * @param showChildPackageFlag 是否递归查询
     * @return class 的全限定名
     */
    @Deprecated
    public static List<String> getClazzNameForClassLoad(String packageName, boolean showChildPackageFlag) {
        List<String> result;
        String suffixPath = packageName.replaceAll("\\.", "/");
        //获取线程的类加载器（线程类加载器突破双亲委派）
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        //获取url： 查找具有给定(路径)名称的所有资源
        try {
            Enumeration<URL> urls = loader.getResources(suffixPath);
            result = getClazzName0(packageName, urls, showChildPackageFlag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 查找包下的所有类的名字
     *
     * @return List集合，内容为类的全名
     */
    private static List<String> getClazzName0(String packageName, Enumeration<URL> urls, boolean showChildPackageFlag) {
        if (isLog) System.out.println("======================= 查找类 ===========================");
        List<String> result = new ArrayList<>();
        if (isLog) System.out.println("查找 " + packageName + " 下的类，递归查找子文件: " + showChildPackageFlag);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();

            if (isLog) System.out.println("查找 " + packageName + " 下的类(URL): " + url);

            if (url != null) {
                //获取URL的协议，如果是class就是file协议，jar就是jar协议
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String path = url.getPath();//类路径
                    result.addAll(getAllClassNameByFile(new File(path), showChildPackageFlag));

                    if (isLog)
                        System.out.println("查找 " + packageName + " 下的类: " + getAllClassNameByFile(new File(path), showChildPackageFlag).size());
                } else if ("jar".equals(protocol)) {
                    if (packageName == null || packageName.trim().isEmpty())
                        if (isLog)
                            System.err.println("FindClass 扫描JAR时，packageName 不应该为null,否则有可能导致异常发送");
                    JarFile jarFile = null;
                    try {
                        jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jarFile != null) {
                        result.addAll(getAllClassNameByJar(jarFile, packageName, showChildPackageFlag));

                        if (isLog)
                            System.out.println("查找 " + packageName + " 下(jar中)的类: " + getAllClassNameByJar(jarFile, packageName, showChildPackageFlag).size());
                        try {
                            jarFile.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        }

        if (isLog) System.out.println("查找到的类数量： " + result.size());
        if (isLog) System.out.println("======================= 查找类END ========================");
        return result;
    }


    /**
     * 递归获取所有class文件的名字
     *
     * @param file
     * @param flag 是否需要迭代遍历
     * @return List
     */
    private static List<String> getAllClassNameByFile(File file, boolean flag) {
        List<String> result = new ArrayList<>();
        if (!file.exists()) {
            return result;
        }
        if (file.isFile()) {
            addFiles(file, result);
        } else {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File f : listFiles) {
                    if (flag) {
                        result.addAll(getAllClassNameByFile(f, true));
                    } else {
                        if (f.isFile()) {
                            addFiles(f, result);
                        }
                    }
                }
            }
        }
        return result;
    }

    private static void addFiles(File file, List<String> result) {
        String path = file.getPath();
        // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题
        if (path.endsWith(CLASS_SUFFIX)) {
            path = path.replace(CLASS_SUFFIX, "");
            // 从"/classes/"后面开始截取
            String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())
                    .replace(File.separator, PACKAGE_SEPARATOR);
            if (!clazzName.contains("$")) {
                result.add(clazzName);
            }
        }
    }


    /**
     * 递归获取jar所有class文件的名字
     *
     * @param jarFile     jar 的路径
     * @param packageName 包名
     * @param flag        是否需要迭代遍历
     * @return List
     */
    private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag) {
        List<String> result = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if (name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if (flag) {
                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok
                    if (name.startsWith(packageName) && !name.contains("$")) {
                        result.add(name);
                    }
                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if (packageName.equals(name.substring(0, name.lastIndexOf("."))) && !name.contains("$")) {
                        result.add(name);
                    }
                }
            }
        }
        return result;
    }


    /**
     * 获取jar包下的class文件名称
     *
     * @param jarFilePath jar 路径
     */
    public static List<String> getJarClassName(String jarFilePath) throws IOException {
        if (isLog) System.out.println("======================= 查找类 ===========================");
        if (isLog) System.out.println("查找 " + jarFilePath + " 下的类");
        ArrayList<String> classNames = new ArrayList<>();
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();

                //如果是class文件就放入集合
                try {
                    String classPath = jarEntry.getName().replaceAll("/", "\\.");
                    if (classPath.lastIndexOf(".") == classPath.length() - 1) continue;
                    String lastName = classPath.substring(classPath.lastIndexOf("."));
                    if (lastName.equals(".class"))
                        classNames.add(classPath.substring(0, classPath.lastIndexOf(".")));//去掉.class后缀
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (isLog) System.out.println("查找到的类数量： " + classNames.size());
        if (isLog) System.out.println("======================= 查找类END ========================");
        return classNames;
    }
}
