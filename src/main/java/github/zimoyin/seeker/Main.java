package github.zimoyin.seeker;

import github.zimoyin.seeker.reference.ClassVsFactory;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralClass;
import github.zimoyin.seeker.reference.vs.visitor.ClassVs;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Deprecated
public class Main {
    public String name;
    private Integer agaa;
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
                    System.out.println(cls.getSuperClassVs());
                    System.out.println(Arrays.toString(cls.getInterfacesVs()));
                    for (String reference : cls.getReferences()) {
                        System.out.println(reference);
                    }
                    System.out.println();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                return true;
            }
        });
        System.out.println();
        System.out.println("class size: "+list.size());
        System.out.println("time: "+(System.currentTimeMillis()-start)+"ms");

        ClassVs classVS = ClassVsFactory.getClassVS(Main.class);
        System.out.println(classVS);
        for (String reference : classVS.getReferences()) {
            System.out.println(reference);
        }
    }
    private void  aga(Object a){}
    private void  agag(Menu a){}

    private void a() throws IOException {
        @Deprecated int[] a = new int[12];
        String[] b = new String[6];
        String c = new String();
        int d = 0;
        if (true) {
            int e = 0;
        }
        try {
            int g = 0;
        } catch (Exception e) {
        }
    }

    int AA = 0;

    private int[] a(@Deprecated int A) {
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