package cn.edu.fudan.baseast.algorithm;

import cn.edu.fudan.baseast.structure.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhangxiaohao on 16/9/14.
 */
public class Algorithm {
    public Integer effectLength;
    public TimeStamp timeStamp;
    public ArrayList<Node> document;
    public ConcurrentLinkedQueue<Operation> inQueue;
    public ConcurrentLinkedQueue<Operation> outQueue;

    public Algorithm(int number) {
        outQueue = new ConcurrentLinkedQueue<Operation>();
        inQueue = new ConcurrentLinkedQueue<Operation>();
        document = new ArrayList<Node>();
        effectLength = 0;
        timeStamp = new TimeStamp(number);
    }

    public Algorithm(ArrayList<Node> document) {
        this.document = document;
    }

    private void getEffectLength() {
        effectLength = 0;
        for(Node node : document) {
            effectLength += node.isEffect()?1:0;
        }
    }

    /**
     * 查找插入节点位置
     * @param pos
     * @param operation
     */
    private void rangescan(int pos, Operation operation) {
        int i, p = -1;
        for(i=pos + 1; i<document.size(); i++) {
            Node node = document.get(i);
            if(node.isEffect(operation)) break;
            if(node.getInsertOperation().getOperationRelationship(operation) == OperationRelationship.CAUSAL) {
                p = i;
                break;
            }else {
                if(node.getInsertOperation().getTimeStamp().getTotalOrderRelationship(operation.getTimeStamp()) == 1) {
                    p = i;
                }else if(p != -1 && node.getInsertOperation().getOperationRelationship(document.get(i).getInsertOperation()) == OperationRelationship.CAUSAL){
                    p = -1;
                }
            }
        }
        if(p == -1) p = document.size();
        document.add(p, new Node(operation.getOperationString(), operation));
    }

    /**
     * 执行操作函数
     * @param operation
     */
    public void execute(Operation operation) {
        int cnt = 0, pos = 0;
        if(operation.getPosition() == 0 && operation.getOperationType() == OperationType.INSERT) rangescan(pos, operation);
        for(Node node : document) {
            if(node.isEffect(operation)) cnt ++;
            if(cnt == operation.getPosition()) {
                if(operation.getOperationType() == OperationType.INSERT) {
                    rangescan(pos, operation);
                }else if(operation.getOperationType() == OperationType.DELETE) {
                    node.addDelete(operation);
                }
                break;
            }
            pos ++;
        }
        getEffectLength();
    }

    /**
     * 返回一个远程操作是否在当前站点上因果就绪
     * @param operation
     * @return true or false
     */
    public boolean isCausalReady(Operation operation) {
        for(int i=0; i<operation.getTimeStamp().timeStamp.size(); i++) {
            if(i == operation.getTimeStamp().getSiteNumber()) {
                if(operation.getTimeStamp().timeStamp.get(i) != timeStamp.timeStamp.get(i) + 1)
                    return false;
            }else {
               if(operation.getTimeStamp().timeStamp.get(i) > timeStamp.timeStamp.get(i))
                    return false;
            }
        }
        return true;
    }

    /**
     * 打印模型
     */
    public void printModel() {
        for(Node node : document) {
            if(node.isEffect()) {
                System.out.print(node.operationString);
            }
        }
        System.out.println();
    }
}
