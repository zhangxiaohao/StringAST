package cn.edu.fudan.stringast.test;

import cn.edu.fudan.stringast.algorithm.S_Algorithm;
import cn.edu.fudan.stringast.structure.S_Operation;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhangxiaohao on 2016/9/22.
 */
public class S_Sender extends Thread{
    public int siteNumber;
    public ArrayList<S_Algorithm> s_algorithms;

    public S_Sender(Integer siteNumber, ArrayList<S_Algorithm> s_algorithms) {
        this.siteNumber = siteNumber;
        this.s_algorithms = s_algorithms;
    }

    private void sendOperation() {
        ConcurrentLinkedQueue<S_Operation> queue = s_algorithms.get(siteNumber).outQueue;
        int queueSize = queue.size();
        if(queueSize > 0) {
            System.out.println("In site " + siteNumber + " has " + queueSize + " operations been send");
        }
        while(queue.size() > 0) {
            S_Operation s_operation = s_algorithms.get(siteNumber).outQueue.poll();
            if(s_operation == null) System.out.println("!!!!!!!!!!!!!!!!");
            for(int i=0; i<s_algorithms.size(); i++) if(i != this.siteNumber) {
                s_algorithms.get(i).inQueue.add(s_operation);
            }
        }
    }

    public void run() {
        while(true) {
            try {
                sendOperation();
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
