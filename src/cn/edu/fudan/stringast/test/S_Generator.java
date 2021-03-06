package cn.edu.fudan.stringast.test;

import cn.edu.fudan.baseast.structure.OperationType;
import cn.edu.fudan.stringast.algorithm.S_Algorithm;
import cn.edu.fudan.stringast.structure.S_Operation;
import cn.edu.fudan.stringast.structure.S_TimeStamp;

import java.util.Random;

/**
 * Created by zhangxiaohao on 2016/9/22.
 */
public class S_Generator extends Thread{
    S_Algorithm s_algorithm;
    Integer Number;
    private String alpha = "abcdefghijklmnopqrstuvwxyz";
    public int operationNum = 0;

    public S_Generator(S_Algorithm s_algorithm, Integer Number) {
        this.s_algorithm = s_algorithm;
        this.Number = Number;
    }

    private StringBuilder getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<length; i++) {
            int pos = new Random().nextInt(26);
            sb.append(alpha.substring(pos, pos + 1));
        }
        return sb;
    }

    private void executeOperatoin() {
        System.out.println("Operation has been execute!");
        int operationType = OperationType.INSERT;
        if(this.s_algorithm.effectLength > 0) {
            operationType = new Random().nextInt(2) + 1;
        }
        int position = 0, length = 0;
        if(operationType == OperationType.INSERT) {
            position = new Random().nextInt(this.s_algorithm.effectLength + 1);
            length = new Random().nextInt(1) + 1;
        }else if(operationType == OperationType.DELETE) {
            position = new Random().nextInt(this.s_algorithm.effectLength) + 1;
            length = 1;//new Random().nextInt(this.s_algorithm.effectLength - position + 1) + 1;
        }
        int old = s_algorithm.s_timeStamp.timeStamp.get(s_algorithm.s_timeStamp.getSiteNumber());
        s_algorithm.s_timeStamp.timeStamp.set(s_algorithm.s_timeStamp.getSiteNumber(), old + 1);
        StringBuilder randomString = getRandomString(length);
        S_Operation s_operation = new S_Operation(new S_TimeStamp(this.s_algorithm.s_timeStamp), randomString, operationType, position, length);
        s_operation.print();
        operationNum += length;
        s_algorithm.execute(s_operation);
        s_algorithm.outQueue.add(s_operation);
    }

    public void run() {
        try {
            int C = 1;
            while(C -- != 0) {
                for(int i=0; i<Number; i++) {
                    executeOperatoin();
                }
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
        }
    }
}
