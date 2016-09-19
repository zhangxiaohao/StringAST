package cn.edu.fudan.baseast;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.test.Executor;
import cn.edu.fudan.baseast.test.Generator;
import cn.edu.fudan.baseast.test.Sender;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.tools.javac.jvm.Gen;
import com.sun.tools.jdi.ArrayReferenceImpl;

import javax.crypto.AEADBadTagException;
import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 16/9/19.
 */
public class BaseASTTest {
    public static ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
    public static ArrayList<Generator> generators = new ArrayList<Generator>();
    public static ArrayList<Executor> executors = new ArrayList<Executor>();
    public static ArrayList<Sender> senders = new ArrayList<Sender>();
    public static final int SITE = 1;
    public static final int OPNUM = 1;

    public static void run() throws InterruptedException {
        for(int i=0; i<SITE; i++) algorithms.add(new Algorithm(SITE));
        Thread.sleep(1000);
        for(int i=0; i<SITE; i++) {
            generators.add(new Generator(algorithms.get(i), OPNUM));
            generators.get(i).start();
            senders.add(new Sender(i, algorithms));
            senders.get(i).start();
            executors.add(new Executor(algorithms.get(i)));
            executors.get(i).start();
        }
        Thread.sleep(3000);
        for(Algorithm algorithm : algorithms) {
            algorithm.printModel();
        }
        for(int i=0; i<SITE; i++) {
            generators.get(i).stop();
            senders.get(i).stop();
            executors.get(i).stop();
        }
    }

    public static void main(String [] args) {
        try {
            run();
        } catch (InterruptedException e) {}
    }
}
