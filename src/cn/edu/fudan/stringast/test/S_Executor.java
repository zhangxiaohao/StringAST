package cn.edu.fudan.stringast.test;

import cn.edu.fudan.stringast.algorithm.S_Algorithm;
import cn.edu.fudan.stringast.structure.S_Operation;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhangxiaohao on 2016/9/22.
 */
public class S_Executor extends Thread{
    S_Algorithm s_algorithm;

    public S_Executor(S_Algorithm s_algorithm) {
        this.s_algorithm = s_algorithm;
    }

    private void executeRemoteOperation() {
        ConcurrentLinkedQueue<S_Operation> queue = s_algorithm.inQueue;
        if(queue.size() > 0) {
            System.out.println("Check site " + this.s_algorithm.s_timeStamp.getSiteNumber() + ", " + queue.size() + " operations has been execute!");
        }
        while(queue.size() > 0) {
            S_Operation s_operation = s_algorithm.inQueue.poll();
            if(s_algorithm.isCausalReady(s_operation) == false) {
                s_algorithm.inQueue.add(s_operation);
                continue;
            }
            s_algorithm.execute(s_operation);
            int old = s_algorithm.s_timeStamp.timeStamp.get(s_operation.getS_timeStamp().getSiteNumber());
            s_algorithm.s_timeStamp.timeStamp.set(s_operation.getS_timeStamp().getSiteNumber(), old + 1);
        }
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(5000);
                executeRemoteOperation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
