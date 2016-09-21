package cn.edu.fudan.baseast;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.structure.Node;
import cn.edu.fudan.baseast.test.Executor;
import cn.edu.fudan.baseast.test.Generator;
import cn.edu.fudan.baseast.test.Sender;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 16/9/19.
 */
public class BaseASTTest {
    public static ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
    public static ArrayList<Generator> generators = new ArrayList<Generator>();
    public static ArrayList<Executor> executors = new ArrayList<Executor>();
    public static ArrayList<Sender> senders = new ArrayList<Sender>();
    public static final int SITE = 20;
    public static final int OPNUM = 10;

    public static void out() {
        Algorithm algorithm = algorithms.get(0);
        System.out.println(algorithm.document.size());
        for(Node node : algorithm.document) {
            System.out.print(node.operationString + " ");
        }
        System.out.println();
    }

    public static void run() throws InterruptedException {
        for(int i=0; i<SITE; i++) algorithms.add(new Algorithm(i, SITE));
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
        for(int i=0; i<SITE; i++) {
            generators.get(i).stop();
            senders.get(i).stop();
            executors.get(i).stop();
        }
        for(Algorithm algorithm : algorithms) {
            algorithm.printModel();
//            algorithm.printModelinDetail();
//            System.out.println("/n-----------------------------/n");
        }
    }

    public static void main(String [] args) {
        try {
            run();
        } catch (InterruptedException e) {}
    }
}
