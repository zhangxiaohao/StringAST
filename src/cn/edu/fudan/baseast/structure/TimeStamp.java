package cn.edu.fudan.baseast.structure;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zhangxiaohao on 16/9/14.
 */
public class TimeStamp {
    private Integer siteNumber;
    public ArrayList<Integer> timeStamp;

    public TimeStamp(int siteNumber, int Num) {
        this.siteNumber = siteNumber;
        timeStamp = new ArrayList<Integer>(Num);
        while(Num -- != 0) timeStamp.add(0);
    }

    public Integer getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(Integer siteNumber) {
        this.siteNumber = siteNumber;
    }

    /**
     * 获取操作之间的关系
     * @param timeStamp
     * @return CAUSAL CONCURRENT
     *
     */
    public int getTimeStampRelationship(TimeStamp timeStamp) {
        for(int i=0; i<siteNumber; i++) {
            if(this.timeStamp.get(i) > timeStamp.timeStamp.get(i)) return OperationRelationship.CONCURRENT;
        }
        return OperationRelationship.CAUSAL;
    }

    /**
     * 获取操作的Torder值
     * @return
     */
    public int getTotalOrder() {
        int totalOrder = 0;
        for(int i=0; i<timeStamp.size(); i++) {
            totalOrder += timeStamp.get(i);
        }
        return totalOrder;
    }

    /**
     * 获取时间戳之间的全序关系
     * @param timeStamp
     * @return 0 小于 1 大于
     */
    public int getTotalOrderRelationship(TimeStamp timeStamp) {
        if(this.getTotalOrder() < timeStamp.getTotalOrder()) return 0;
        else if(this.getTotalOrder() == timeStamp.getTotalOrder()) {
            if(this.getSiteNumber() < timeStamp.getSiteNumber()) return 0;
        }
        return 1;
    }

    /**
     * 打印时间戳
     */
    public void print() {
        System.out.print("( ");
        for(int i=0; i<timeStamp.size(); i++) {
            System.out.print(timeStamp.get(i) + " ");
        }
        System.out.println(")");
    }
}
