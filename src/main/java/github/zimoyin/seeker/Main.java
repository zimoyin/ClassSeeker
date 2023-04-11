package github.zimoyin.seeker;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralClass;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralField;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethod;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethodParameter;
import github.zimoyin.seeker.reference.vs.visitor.ClassVs;
import github.zimoyin.seeker.reference.vs.visitor.VisitorClass;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class Main {
    public String name;
    public int version;
    public String[] a;
    private int[] b;

    @Deprecated
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        //TODO: 添加引用列表方法，返回类引用的所有的类
        long start = System.currentTimeMillis();
        List<String> list = ClassSeeker.findClass(ClassSeeker.ClassALL, "./out/rt.jar", new Filter() {
            @Override
            public boolean test(GeneralClass cls) {
                try {
                    System.out.println(cls);
                    for (GeneralMethod method : cls.getMethods()) {
                        System.out.println(method);
                    }
                    for (GeneralField field : cls.getFields()) {
                        System.out.println(field);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        });
        System.out.println();
        System.out.println("class size: "+list.size());
        System.out.println("time: "+(System.currentTimeMillis()-start)+"ms");
    }

    private void a() {
        @Deprecated int[] a = new int[12];
        String[] b = new String[6];
        String c = new String();
        int d = 0;
        if (true) {
            int e = 0;
        }
        int f = AA;
        AA += AA;
        try {
            int g = 0;
        } catch (Exception e) {
        }
    }

    int AA = 0;

    private int[] a(@Deprecated  int A) {
        int[] a = new int[12];
        String[] b = new String[6];
        String c = new String();
        int d = 0;
        if (true) {
            int e = 0;
        }
        int f = AA;
        AA += AA;
        try {
            int g = 0;
        } catch (Exception e) {
        }
        return null;
    }
}