package github.zimoyin.seeker;

import github.zimoyin.seeker.find.FindClass;
import github.zimoyin.seeker.reference.ClassReferencePacket;
import github.zimoyin.seeker.reference.ClassReferenceVisitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static github.zimoyin.seeker.reference.ClassReferenceVisitor.getClassReference;

//@Deprecated
public class Main implements Runnable{
    private String aa = "";
    @Deprecated
    public static void main(String[] args) throws IOException {
        ClassSeeker.findClass(ClassSeeker.ClassALL, "out/CLI-1.0-SNAPSHOT.jar", packet -> {
            System.out.println(packet.getClassName());
            return true;
        });


    }

    public static int g(int a){
        return 0;
    }

    @Override
    public void run() {

    }
}
