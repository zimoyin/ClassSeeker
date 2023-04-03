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
        for (String cls : ClassSeeker.findClass(ClassSeeker.ClassALL, "out/rt.jar")) {
            System.out.println(cls);
        }
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
