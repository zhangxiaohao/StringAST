package cn.edu.fudan.stringast;

import cn.edu.fudan.baseast.structure.Operation;
import cn.edu.fudan.stringast.algorithm.S_Algorithm;
import cn.edu.fudan.stringast.structure.S_Operation;
import cn.edu.fudan.stringast.test.S_Executor;
import cn.edu.fudan.stringast.test.S_Generator;
import cn.edu.fudan.stringast.test.S_Sender;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/9/22.
 */
public class StringASTTest {
    public static ArrayList<S_Algorithm> s_algorithms = new ArrayList<S_Algorithm>();
    public static ArrayList<S_Generator> s_generators = new ArrayList<S_Generator>();
    public static ArrayList<S_Executor> s_executors = new ArrayList<S_Executor>();
    public static ArrayList<S_Sender> s_senders = new ArrayList<S_Sender>();
    public static final int SITE = 10;
    public static final int OPNUM = 10;

    public static void run() throws InterruptedException {
        for(int i=0; i<SITE; i++) s_algorithms.add(new S_Algorithm(i, SITE));
        Thread.sleep(1000);
        for(int i=0; i<SITE; i++) {
            s_generators.add(new S_Generator(s_algorithms.get(i), OPNUM));
            s_generators.get(i).start();
            s_senders.add(new S_Sender(i, s_algorithms));
            s_senders.get(i).start();
            s_executors.add(new S_Executor(s_algorithms.get(i)));
            s_executors.get(i).start();
        }
        Thread.sleep(3000);
        for(int i=0; i<SITE; i++) {
            s_generators.get(i).stop();
            s_senders.get(i).stop();
            s_executors.get(i).stop();
        }
        for(S_Algorithm s_algorithm : s_algorithms) {
            s_algorithm.printModel();
            //s_algorithm.printModelinDetail();
            //System.out.println("***-------------------------------***");
        }
    }

    public static void main(String [] args) {
        try {
            run();
        } catch (InterruptedException e) {}
    }
}
