package cn.edu.fudan.baseast.test;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.structure.Operation;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 16/9/18.
 * 负责发送本地执行的操作线程
 */
public class Sender extends Thread{
    public int siteNumber;
    public ArrayList<Algorithm> algorithms;

    public Sender(Integer siteNumber, ArrayList<Algorithm> algorithms) {
        //System.out.println("Sender has run!");
        this.siteNumber = siteNumber;
        this.algorithms = algorithms;
    }

    private void sendOperation() {
        int queueSize = algorithms.get(siteNumber).outQueue.size();
        if(queueSize > 0) {
            System.out.println("In site " + siteNumber + " has " + queueSize + " operations been send");
        }
        while(queueSize > 0){
            Operation operation = algorithms.get(siteNumber).outQueue.poll();
            for(int i=0; i<algorithms.size(); i++) if(i != this.siteNumber){
                algorithms.get(i).inQueue.add(operation);
            }
        }
    }

    public void run() {
        while(true) {
            try {
                sendOperation();
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
