package cn.edu.fudan.stringast.algorithm;

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

    public S_Algorithm(int siteNum, int Num) {
        outQueue = new ConcurrentLinkedQueue<S_Operation>();
        inQueue = new ConcurrentLinkedQueue<S_Operation>();
        document = new ArrayList<S_Node>();
        effectLength = 0;
        s_timeStamp = new S_TimeStamp(siteNum, Num);
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
