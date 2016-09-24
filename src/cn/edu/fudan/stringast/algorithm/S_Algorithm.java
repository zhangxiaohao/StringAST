package cn.edu.fudan.stringast.algorithm;

import cn.edu.fudan.baseast.structure.OperationRelationship;
import cn.edu.fudan.baseast.structure.OperationType;
import cn.edu.fudan.baseast.structure.TimeStamp;
import cn.edu.fudan.stringast.Timer;
import cn.edu.fudan.stringast.structure.S_Node;
import cn.edu.fudan.stringast.structure.S_Operation;
import cn.edu.fudan.stringast.structure.S_TimeStamp;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhangxiaohao on 2016/9/21.
 */
public class S_Algorithm {
    public Integer effectLength;
    public S_TimeStamp s_timeStamp;
    public ArrayList<S_Node> document;
    public ConcurrentLinkedQueue<S_Operation> inQueue;
    public ConcurrentLinkedQueue<S_Operation> outQueue;
    public long timeSpend;

    public S_Algorithm(int siteNum, int Num) {
        outQueue = new ConcurrentLinkedQueue<S_Operation>();
        inQueue = new ConcurrentLinkedQueue<S_Operation>();
        document = new ArrayList<S_Node>();
        effectLength = 0;
        s_timeStamp = new S_TimeStamp(siteNum, Num);
        timeSpend = 0;
    }

    /**
     * 运行中新加入的站点的构造函数
     * 从其他站点复制所有的信息
     */
    public S_Algorithm(ArrayList<S_Node> document) {
        this.document = new ArrayList<S_Node>();
        for(S_Node s_node : document) {
            this.document.add(s_node);
        }
    }

    /**
     * 更新有效节点的个数
     */
    private void getEffectLength() {
        effectLength = 0;
        for(S_Node s_node : document) {
            effectLength += s_node.isEffect() ? s_node.operationString.length() : 0;
        }
    }

    /**
     * rangescan 考虑几种情况
     * 在一个有效节点之间的插入
     * 在有效节点的边界插入，后面可能包含无效节点
     * @param pos
     * @param shift
     * @param s_operation
     */
    public void rangescan(int pos, int shift, S_Operation s_operation) {
        if(pos >= 0 && shift != document.get(pos).operationString.length()) {
            ArrayList<S_Node> nodes = document.get(pos).split(shift);
            document.remove(pos);
            document.addAll(pos, nodes);
            document.add(pos + 1, new S_Node(s_operation.getOperationString(), new S_Operation(s_operation)));
            return ;
        }
        int i, p = -1;
        for(i=pos + 1; i<document.size(); i++) {
            S_Node s_node = document.get(i);
            if(s_node.isEffect(s_operation)) {
                p = i;
                break;
            }
            if(s_node.getInsertOperation().getOperationRelationShip(s_operation) == OperationRelationship.CAUSAL) {
                p = i;
                break;
            }else {
                if(p == -1 && s_node.getInsertOperation().getS_timeStamp().getTotalOrderRelationship(s_operation.getS_timeStamp()) == 1) {
                    p = i;
                }
                if(p != -1 && s_node.getInsertOperation().getS_timeStamp().getTotalOrderRelationship(s_operation.getS_timeStamp()) == 0
                   && s_node.getInsertOperation().getOperationRelationShip(document.get(i).getInsertOperation()) == OperationRelationship.CAUSAL) {
                    p = -1;
                }
            }
        }
        if(p == -1) p = document.size();
        document.add(p, new S_Node(s_operation.getOperationString(), new S_Operation(s_operation)));
    }

    /**
     * 删除函数 首先找到删除范围的边界
     * 检查是否需要分裂，分裂完毕后对操作执行范围内的所有有效节点添加删除操作。
     * @param pos
     * @param shift
     * @param s_operation
     */
    public void delete(int pos, int shift, S_Operation s_operation) {
        if(shift != 1) {
            ArrayList<S_Node> nodes = document.get(pos).split(shift - 1);
            document.remove(pos);
            document.addAll(pos, nodes);
            pos ++;
        }
        int i, cnt = 0;
        for(i = pos; i<document.size(); i++) {
            if(document.get(i).isEffect(s_operation) == false) continue;
            cnt += document.get(i).operationString.length();
            if(cnt >= s_operation.getLength()) {
                int tpos = document.get(i).operationString.length() - (cnt - s_operation.getLength());
                if(tpos != document.get(i).operationString.length()) {
                    ArrayList<S_Node> nodes = document.get(i).split(tpos);
                    document.remove(pos);
                    document.addAll(pos, nodes);
                }
                break;
            }
        }
        cnt = 0;
        for(i=pos; i<document.size(); i++) {
            if(document.get(i).isEffect(s_operation) == false) continue;
            cnt += document.get(i).operationString.length();
            if(cnt > s_operation.getLength()) break;
            S_Operation nop = new S_Operation(s_operation);
            nop.getOperationString().replace(0, nop.getOperationString().length(), document.get(i).operationString.toString());
            document.get(i).addDelete(nop);
        }
    }

    /**
     * 执行操作函数
     * @param s_operation
     */
    public void execute(S_Operation s_operation) {
        long start = Timer.now();
        int cnt = 0, pos = 0;
        if(s_operation.getPosition() == 0 && s_operation.getOperationType() == OperationType.INSERT) {
            rangescan(-1, 0, s_operation);
            getEffectLength();
            return ;
        }
        for(S_Node s_node : document) {
            if(s_node.isEffect(s_operation)) cnt += s_node.operationString.length();
            if(cnt >= s_operation.getPosition()) {
                if(s_operation.getOperationType() == OperationType.INSERT) {
                    rangescan(pos, s_node.operationString.length() - (cnt - s_operation.getPosition()), s_operation);
                }else if(s_operation.getOperationType() == OperationType.DELETE) {
                    delete(pos, s_node.operationString.length() - (cnt - s_operation.getPosition()), s_operation);
                }
                break;
            }
            pos ++;
        }
        getEffectLength();
        timeSpend += Timer.now() - start;
    }

    /**
     * 返回一个远程操作是否在当前站点上因果就绪
     * @param s_operation
     * @return true or false
     */
    public boolean isCausalReady(S_Operation s_operation) {
        for(int i=0; i<s_operation.getS_timeStamp().timeStamp.size(); i++) {
            if(i == s_operation.getS_timeStamp().getSiteNumber()) {
                if(s_operation.getS_timeStamp().timeStamp.get(i) != s_timeStamp.timeStamp.get(i) + 1)
                    return false;
            }else {
                if(s_operation.getS_timeStamp().timeStamp.get(i) > s_timeStamp.timeStamp.get(i))
                    return false;
            }
        }
        return true;
    }

    /**
     * 打印模型的详细信息
     */
    public void printModelinDetail() {
        for(S_Node node : document) {
            node.print();
        }
    }

    /**
     * 打印模型
     */
    public void printModel() {
        for(S_Node node : document) {
            if(node.isEffect()) {
                System.out.print(node.operationString);
            }
        }
        System.out.println();
    }
}
