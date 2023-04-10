package github.zimoyin.seeker;

import github.zimoyin.seeker.reference.ClassReferencePacket;

import java.io.IOException;
import java.lang.annotation.Native;

@Deprecated
public class Main  {
    @Deprecated
    private String aa = "";
    private String aag = "";

    public static void main(String[] args) throws IOException {
        ClassSeeker.findClass(ClassSeeker.ClassALL, null, new Filter() {
            @Override
            public boolean test(ClassReferencePacket packet) {
                if (!packet.getClassName().equals("github.zimoyin.seeker.Main")) return false;
                System.out.println(packet.getClassName());
                System.out.println(packet.getClassAnnotationCollection());
                System.out.println(packet.getMethodAnnotationCollection());
                System.out.println();
                //字段注解
                //方法参数注解
                return true;
            }
        });
    }

    @Deprecated
    public static int g(@Deprecated int a) {
        return 0;
    }

}
