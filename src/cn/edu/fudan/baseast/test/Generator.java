package cn.edu.fudan.baseast.test;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.structure.Operation;
import cn.edu.fudan.baseast.structure.OperationType;
import cn.edu.fudan.baseast.structure.TimeStamp;

import java.util.Random;

/**
 * Created by zhangxiaohao on 16/9/18.
 * 负责产生本地操作的线程
 */
public class Generator extends Thread{
    Algorithm algorithm;
    Integer Number;
    private String alpha = "abcdefghijklmnopqrstuvwxyz";

    public Generator(Algorithm algorithm, Integer Number) {
        //System.out.println("Generator has been run !");
        this.algorithm = algorithm;
        this.Number = Number;
    }

    private void executeOperation() {
        System.out.println("Operation has been execute!");
        int operationType = OperationType.INSERT;
        if(this.algorithm.effectLength > 0) {
            operationType = Math.random() > 0.5 ? OperationType.INSERT : OperationType.DELETE;
        }
        int position = 0;
        if(operationType == OperationType.INSERT) {
            position = new Random().nextInt(this.algorithm.effectLength + 1);
        }else if (operationType == OperationType.DELETE) {
            position = new Random().nextInt(this.algorithm.effectLength) + 1;
        }
        int pos = new Random().nextInt(26);
        System.out.println(algorithm.timeStamp.timeStamp.size());
        int old = algorithm.timeStamp.timeStamp.get(algorithm.timeStamp.getSiteNumber());
        algorithm.timeStamp.timeStamp.set(algorithm.timeStamp.getSiteNumber(), old + 1);
        Operation operation = new Operation(this.algorithm.timeStamp, new StringBuilder(alpha.substring(pos, pos + 1)), operationType, position);
        operation.setTimeStamp(new TimeStamp(algorithm.timeStamp));
        operation.print();
        algorithm.execute(operation);
        algorithm.outQueue.add(operation);
    }

    public void run() {
        try {
            int C = 1;
            while(C -- != 0) {
                for(int i=0; i<Number; i++) {
                    executeOperation();
                }
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
