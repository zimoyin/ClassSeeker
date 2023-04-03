package github.zimoyin.seeker;

import java.io.IOException;
import java.util.ArrayList;

@Deprecated
public class Main implements Runnable {
    private String aa = "";

    private static long s = 0;

    public static ArrayList<Long> timesIO = new ArrayList<Long>();


    @Deprecated
    public static void main(String[] args) throws IOException {
        ArrayList<Long> times = new ArrayList<Long>();
        long start = System.currentTimeMillis();
        ClassSeeker.findClass(ClassSeeker.ClassALL, "out/rt.jar", packet -> {
            System.out.println(packet.getClassName());
            System.out.println(packet.getMethodParameterCollection());
            System.out.println(packet.getMethodCollection());
            System.out.println(packet.getLocalVariableTypeCollection());
            System.out.println(packet.getReturnValueCollection());
            System.out.println();
            System.out.println();
            System.out.println();
            s++;
            System.out.print("\r" + s);
            return true;
        });
        System.out.println();
        long end = System.currentTimeMillis();
        times.add(end - start);
        System.out.println("ASM扫描时间: " + ((double) end - start) / 1000);
    }

    public static int g(int a) {
        return 0;
    }

    public static long ag(int a) {
        return 0;
    }

    public static short aga(int a) {
        return 0;
    }

    public static double agaa(int a) {
        return 0;
    }

    @Override
    public void run() {

    }
}
