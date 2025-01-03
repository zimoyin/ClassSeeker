package com.github.zimoyin.seeker.find;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class FindPackage {
    /**
     * 获取应用程序的根包，根据MF，pom，文件分析
     */
    @Deprecated
    public static List<String> getClassRootPathForApplication() throws MalformedURLException {
        return null;
    }

    /**
     * 获取应用程序运行时加载的JAR的URL: 基于获取应用程序运行时加载的JAR 的封装
     * 根据项目来判断，如果是文件夹则加载target内的class，否则加载jar内所有的根包
     */
    public static List<URL> getClassPathToURLForApplication() throws MalformedURLException {
        List<URL> urls = new ArrayList<URL>();
        //获取程序运行的所有依赖以及程序路径
        List<String> classPath = getClassPath();
        //程序是否为但class运行，而非jar运行
        boolean isApplicationForClass = false;
        for (String path : classPath) {
            File file = new File(path);
            URL url = file.toURI().toURL();
            //如果库为一个目录则说明程序是在开发过程中
            if (file.isDirectory()) {
                isApplicationForClass = true;
                urls.add(url);
                continue;
            }
            //如果程序不是开发中就是为jar，加载jar的库
            if (file.isFile() && !isApplicationForClass) urls.add(new URL("jar:" + url + "!/"));
        }
        return urls;
    }


    /**
     * 获取运行时加载的JAR的URL, 基于获取运行时加载Jar的封装
     */
    public static List<URL> getClassPathToURL() throws MalformedURLException {
        List<String> classPath = getClassPath();
        //jar:file:/D:/code/demo5/out/artifacts/MiraiToll2_jar/MiraiToll2.jar!/
        List<URL> urls = new ArrayList<URL>();
        for (String path : classPath) {
            File file = new File(path);
            URL url = file.toURI().toURL();
            if (file.isFile()) url = new URL("jar:" + url + "!/");
        }
        return urls;
    }

    /**
     * 获取应用程序的根包路径: 根据项目来判断，如果是文件夹则加载target内的class，否则加载jar内所有的根包
     */
    public static List<String> getRootPackageNamesForApplication() {
        List<String> rootPackageNames = new ArrayList<>();
        List<String> classPath = getClassPath();
        boolean isTargetApplication = classPath.stream().anyMatch(s -> new File(s).isDirectory());
        if (isTargetApplication) return Collections.singletonList("");
        for (String path : classPath) {
            try {
                rootPackageNames.addAll(getRootPackageNames(path));
            } catch (IOException e) {
                throw new RuntimeException("无法获取到jar的根包列表", e);
            }
        }
        return rootPackageNames;
    }

    /**
     * 获取该JAR的根包，如 rog.xxx -> rog
     *
     * @param jarFilePath JAR 路径
     */
    public static List<String> getRootPackageNames(String jarFilePath) throws IOException {
        System.out.println(jarFilePath);
        File file = new File(jarFilePath);
        URL url = file.toURI().toURL();
//            String urlString = "jar:" + url + "!/";
        try (JarFile jarFile = new JarFile(file)) {
            return jarFile.stream()
                    .filter(jarEntry -> jarEntry.getName().endsWith("/"))
                    .filter(jarEntry -> jarEntry.getName().split("/").length <= 1)
                    .map(jarEntry -> jarEntry.getName().replaceAll("/", ""))
                    .filter(s -> !s.equalsIgnoreCase("META-INF"))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 获取运行时加载的JAR
     */
    public static List<String> getClassPathAll() {
        //sun.java.command
        String property = System.getProperty("java.class.path");
        return Arrays.stream(property.split(";")).filter(s -> s != null && s.length() > 0).collect(Collectors.toList());
    }

    /**
     * 获取运行时加载的JAR或运行路径
     * 注意将依赖打包进Jar或者使用清单链接运行的结果不一样
     */
    public static List<String> getClassPath() {
        //sun.java.command
        String property = System.getProperty("java.class.path");
        return Arrays.stream(property.split(";")).filter(s -> s != null && s.length() > 0).collect(Collectors.toList());
    }
}
