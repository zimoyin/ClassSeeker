# ClassSeeker

ClassSeeker是一个Java项目，提供了一个类扫描工具，使用ASM库对Java类进行分析。它可以帮助你识别一个特定类所继承的类，以及该类中存在的方法和字段等。

# 入门指南

要使用ClassSeeker，您需要自行打包并依赖。并且你还需要ASM 9.0 的依赖

## 如何使用ClassSeeker

1. 首先，导入`ClassSeeker`类。  
所有的操作都从该类进行

```java
import github.zimoyin.seeker.ClassSeeker
```

2. 接着你需要了解你需要的方法。该类通过了两个方法，以及部分重载。  
**注意：扫描器不会扫描标准库中的类**

```java
ClassSeeker.findClass(...); // 查找指定的类，或者具有指定前缀的类，如 github.zimoyin 会查找该包下所有类
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

5. Filter 的参数 ClassReferencePacket  

6. ~~请手动关闭流`ClassReaderUtil.close();` 否则会造成一定的资源无法被回收~~  注意可以重复关闭，但是不要频繁关闭否则会导致缓存不可用，导致效率低下  
如果你想自己构建一个`ClassReferencePacket`请通过`ClassReferenceVisitor.getClassReference(...).getPacket()  
该参数是用于过滤器之中，这个类是针对class的分析封装。如果你需要一个继承或者拥有某个注解的class请考虑他     
**注意：如果不需要 ClassReferencePacket 请不需要传入过滤器**  
