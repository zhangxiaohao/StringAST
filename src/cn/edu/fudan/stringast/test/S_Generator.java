package cn.edu.fudan.stringast.test;

import cn.edu.fudan.baseast.structure.Operation;
import cn.edu.fudan.baseast.structure.OperationType;
import cn.edu.fudan.stringast.algorithm.S_Algorithm;
import cn.edu.fudan.stringast.structure.S_Operation;
import cn.edu.fudan.stringast.structure.S_TimeStamp;
import org.omg.CORBA.INTERNAL;

import java.util.Random;

/**
 * Created by zhangxiaohao on 2016/9/22.
 */
public class S_Generator extends Thread{
    S_Algorithm s_algorithm;
    Integer Number;
    private String alpha = "abcdefghijklmnopqrstuvwxyz";

    public S_Generator(S_Algorithm s_algorithm, Integer Number) {
        this.s_algorithm = s_algorithm;
        this.Number = Number;
    }

    private StringBuilder getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        int pos = new Random().nextInt(26);
        for(int i=0; i<length; i++) sb.append(alpha.substring(pos, pos + 1));
        return sb;
    }

    private void executeOperatoin() {
        System.out.println("Operation has been execute!");
        int operationType = OperationType.INSERT;
        if(this.s_algorithm.effectLength > 0) {
            operationType = Math.random() > 0.5 ? OperationType.INSERT : OperationType.DELETE;
        }
        int position = 0, length = 0;
        if(operationType == OperationType.INSERT) {
            position = new Random().nextInt(this.s_algorithm.effectLength + 1);
            length = new Random().nextInt(20);
        }else if(operationType == OperationType.DELETE) {
            position = new Random().nextInt(this.s_algorithm.effectLength) + 1;
            length = new Random().nextInt(this.s_algorithm.effectLength);
        }
        System.out.println(s_algorithm.s_timeStamp.timeStamp.size());
        int old = s_algorithm.s_timeStamp.timeStamp.get(s_algorithm.s_timeStamp.getSiteNumber());
        s_algorithm.s_timeStamp.timeStamp.set(s_algorithm.s_timeStamp.getSiteNumber(), old + 1);
        StringBuilder randomString = getRandomString(length);
        S_Operation s_operation = new S_Operation(new S_TimeStamp(this.s_algorithm.s_timeStamp), randomString, operationType, position, length);
        s_operation.print();
        s_algorithm.execute(s_operation);
        s_algorithm.outQueue.add(s_operation);
    }

    public void run() {
        for(int i=0; i<Number; i++) {
            executeOperatoin();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
