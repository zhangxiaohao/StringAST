package cn.edu.fudan.baseast.test;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.structure.Operation;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhangxiaohao on 16/9/18.
 * 负责执行远程操作线程
 */
public class Executor extends Thread{
    Algorithm algorithm;

    public Executor(Algorithm algorithm) {
        //System.out.println("Executor has run !");
        this.algorithm = algorithm;
    }

    private void executeRemoteOperation() {
        ConcurrentLinkedQueue<Operation> queue = algorithm.inQueue;
        if(queue.size() > 0) {
            System.out.println("Check site " + this.algorithm.timeStamp.getSiteNumber() + ", " + queue.size() + " operations has been execute!");
        }
        while(queue.size() > 0) {
            Operation operation = algorithm.inQueue.poll();
            if(algorithm.isCausalReady(operation) == false) {
                algorithm.inQueue.add(operation);
                continue;
            }
            algorithm.execute(operation);
            int old = algorithm.timeStamp.timeStamp.get(operation.getTimeStamp().getSiteNumber());
            algorithm.timeStamp.timeStamp.set(operation.getTimeStamp().getSiteNumber(), old + 1);
        }
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(5000);
                executeRemoteOperation();
            } catch (InterruptedException e) {}
        }
    }
}
