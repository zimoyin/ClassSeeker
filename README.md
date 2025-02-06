# ClassSeeker

ClassSeeker是一个Java项目，提供了一个类扫描工具，使用ASM库对Java类进行分析。它可以帮助你识别一个特定类所继承的类，以及该类中存在的方法和字段等。
该工具主要解决，在扫描第三方Jar 时，检测是否缺少类

# 入门指南

引入当前的依赖
```xml
<dependency>
    <groupId>com.com.github.zimoyin</groupId>
    <artifactId>ClassSeeker</artifactId>
    <version>1.0.2</version>
</dependency>
```

## 如何使用ClassSeeker

1. 首先，导入`ClassSeeker`类。  
所有的操作都从该类进行

```java

```

2. 接着你需要了解你需要的方法。该类通过了两个方法，以及部分重载。  
**注意：扫描器不会扫描标准库中的类,如果你在你的项目里面运行的话**

```java
ClassSeeker.findClass(...); // 查找指定的类，或者具有指定前缀的类，如 com.github.zimoyin 会查找该包下所有类
ClassSeeker.findClassAll(...); // 返回所有的类
```

3. 方法可以传入的参数，这些参数不是必须的，你可以根据需要选择对应的重载方法来传入方法

```
packetOrClsName     包名或者类名。 这是你要查找的类或者包的全限定名
jarPath             jar 路径。如果你需要查找某个jar的class，请指定jar的路径
filters             过滤器。用于过滤出你需要的class。
```

4. 常量
```
ClassSeeker.ClassALL 包名或者类名的常量形式，使用该常量会扫描出所有的类
```

5. Filter 的参数 [ClassVs.java](src%2Fmain%2Fjava%2Fgithub%2Fzimoyin%2Fseeker%2Freference%2Fvs%2Fvisitor%2FClassVs.java)   
该参数是用于过滤器之中，这个类是针对class的分析封装。如果你需要一个继承或者拥有某个注解的class请考虑他     
**注意：如果不需要 ClassVs 请不需要传入过滤器**  
**注意：如果只是想需要指定类名的类，不建议使用过滤器，他是非常消耗资源的一种方式**  
6. ~~请手动关闭流`ClassReaderUtil.close();` 否则会造成一定的资源无法被回收~~  注意可以重复关闭，但是不要频繁关闭否则会导致缓存不可用，导致效率低下  
7. 使用类描述对某个类进行分析
```java
ClassVs classVS = ClassVsFactory.getClassVS(Main.class);
```

6. [ClassVsFactory.java](src%2Fmain%2Fjava%2Fgithub%2Fzimoyin%2Fseeker%2Freference%2FClassVsFactory.java) 可以直接生产一个ClassVs对象，不需要手动创建
