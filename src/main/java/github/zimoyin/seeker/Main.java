package github.zimoyin.seeker;

import github.zimoyin.seeker.reference.vs.interfaces.GeneralField;
import github.zimoyin.seeker.reference.vs.interfaces.GeneralMethod;
import github.zimoyin.seeker.reference.vs.visitor.ClassVs;
import github.zimoyin.seeker.reference.vs.visitor.Modifier;
import github.zimoyin.seeker.reference.vs.visitor.VisitorClass;

import java.io.IOException;
import java.util.Arrays;

@Deprecated
public class Main  {
    @Deprecated
    private volatile String aa = "";
    private static final String aag = null;
    private static final int agaag = 0;

    public Main() {
    }

    public Main(String aa) {
        this.aa = aa;
    }

    @Deprecated
    public static void main(String[] args) throws IOException {
//        ClassSeeker.findClass(ClassSeeker.ClassALL, null, new Filter() {
//            @Override
//            public boolean test(ClassReferencePacket packet) {
//                //字段注解
//                //方法参数注解
                  // 参数类型如果是数组需要加以标注
//                return true;
//            }
//        });

        String av = "L[aga[]";
        //TODO: 处理参数为数组的情况,使用标记法来实现，如果是数组则在处理前写入标记 &array 之后处理完成将该标记还原成 []
//        ClassReferencePacket packet = VisitorClass.getClassReference(Main.class.getTypeName()).getPacket();
        VisitorClass classReference = VisitorClass.getClassReference(Main.class.getTypeName());
        ClassVs classVs = classReference.getClassVsInstance();


        for (GeneralMethod method : classVs.getMethods()) {
            System.out.println(method.getName());
            System.out.println(Arrays.toString(method.getAnnotations()));
            System.out.println(Arrays.toString(method.getParameters()));
            System.out.println(method.getReturnType());
            System.out.println();
        }
        System.out.println(classVs.getTypeName());
    }

    @Deprecated
    public static int g(@Deprecated int a) {
        try {
            throw new NullPointerException();
        }catch (Exception e){}
        return 0;
    }

}