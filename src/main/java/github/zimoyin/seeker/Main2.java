package github.zimoyin.seeker;

import github.zimoyin.seeker.find.FindClass;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

@Deprecated
public class Main2 {
    public static void main(String[] args) {
//取得应用(系统)类加载器
        URLClassLoader appClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();

        System.out.println(appClassLoader);
        System.out.println("应用(系统)类加载器 的加载路径: ");

        for(URL url : appClassLoader.getURLs())
            System.out.println(url);
    }
}