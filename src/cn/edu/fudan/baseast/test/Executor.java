package cn.edu.fudan.baseast.test;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.structure.Operation;

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
        int queueSize = algorithm.inQueue.size();
        if(queueSize > 0) {
            System.out.println("Check site " + queueSize + " has been execute!");
        }
        while(queueSize > 0) {
            Operation operation = algorithm.inQueue.poll();
            if(algorithm.isCausalReady(operation) == false) {
                algorithm.inQueue.add(operation);
                continue;
            }
            algorithm.execute(operation);
        }
    }

    public void run() {
        while(true) {
            try {
                executeRemoteOperation();
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
