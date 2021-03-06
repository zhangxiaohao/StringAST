package cn.edu.fudan.stringast.structure;

import cn.edu.fudan.baseast.structure.OperationRelationship;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/9/21.
 */
public class S_Node {
    public StringBuilder operationString;
    private ArrayList<S_Operation> operations;

    /**
     * 深拷贝构造函数
     * @param operationString
     * @param operations
     */
    public S_Node(StringBuilder operationString, ArrayList<S_Operation> operations) {
        this.operationString = new StringBuilder(operationString);
        this.operations = new ArrayList<S_Operation>();
        for(S_Operation s_operation : operations) {
            this.operations.add(new S_Operation(s_operation));
        }
    }

    /**
     * 深拷贝构造函数 ，针对创建一个新节点
     * @param operationString
     * @param op
     */
    public S_Node(StringBuilder operationString, S_Operation op) {
        this.operationString = new StringBuilder(operationString);
        operations = new ArrayList<S_Operation>();
        operations.add(new S_Operation(op));
    }

    /**
     * 获取节点插入操作
     * @return operaton 节点中的插入操作
     */
    public S_Operation getInsertOperation() {
        return operations.get(0);
    }

    /**
     * 增加节点的删除操作
     * @param op
     */
    public void addDelete(S_Operation op) {
        operations.add(op);
    }

    /**
     * 判断操作是否有效
     * @param s_operation
     * @return
     */
    public boolean isEffect(S_Operation s_operation){
        boolean effect = false;
        if(operations.get(0).getOperationRelationShip(s_operation) == OperationRelationship.CAUSAL) {
            effect = true;
        }
        for(int i=1; i<operations.size(); i++) {
            S_Operation op = operations.get(i);
            if(op.getOperationRelationShip(s_operation) == OperationRelationship.CAUSAL) {
                effect = false;
            }
        }
        return effect;
    }

    /**
     * 判断操作在当前时间戳中是不是有效
     * @return
     */
    public boolean isEffect() {
        return operations.size() == 1;
    }

    /**
     * 打印节点中的操作信息
     */
    public void print() {
        System.out.println("-----Node-----");
        for(S_Operation operation : operations) {
            operation.print();
        }
        System.out.println("--------------");
    }

    /**
     * 将一个节点在position位置前分裂成两个
     * @param position
     * @return ret 有分裂后的两个节点组成的链表
     */
    public ArrayList<S_Node> split(int position) {
        S_Node node = new S_Node(this.operationString, this.operations);
        if(position > 0) node.operationString.delete(0, position);
        for(int i=0; i<node.operations.size(); i++) {
            if(position > 0) node.operations.get(i).operationString.delete(0, position);
        }
        S_Node copy = new S_Node(this.operationString, this.operations);
        if(position != copy.operationString.length()) copy.operationString.delete(position, copy.operationString.length());
        for(int i=0; i<copy.operations.size(); i++) {
            if(position != copy.operations.get(i).operationString.length())
                copy.operations.get(i).operationString.delete(position, copy.operations.get(i).operationString.length());
        }
        ArrayList<S_Node> ret = new ArrayList<S_Node>();
        //this.print(); copy.print(); node.print();
        ret.add(copy); ret.add(node);
        return ret;
    }
}
