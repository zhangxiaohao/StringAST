package cn.edu.fudan.baseast.structure;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 16/9/14.
 */
public class Node {
    public StringBuilder operationString;
    private ArrayList<Operation> operations;

    public Node(StringBuilder operationString, Operation op) {
        this.operationString = new StringBuilder(operationString);
        operations = new ArrayList<Operation>();
        operations.add(new Operation(op));
    }

    /**
     * 获取节点插入操作
     * @return operation 节点中的插入操作
     */
    public Operation getInsertOperation() {
        return operations.get(0);
    }

    /**
     * 增加节点的删除操作
     * @param op
     */
    public void addDelete(Operation op) {
        operations.add(new Operation(op));
    }

    /**
     * 判断操作是否有效
     * @param operation
     * @return
     */
    public boolean isEffect(Operation operation) {
        boolean effect = false;
        if(operations.get(0).getOperationRelationship(operation) == OperationRelationship.CAUSAL) {
            effect = true;
        }
        for(int i=1; i<operations.size(); i++) {
            Operation op = operations.get(i);
            if(op.getOperationRelationship(operation) == OperationRelationship.CAUSAL) {
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
        System.out.println("------Node------");
        for(Operation operation : operations) {
            operation.print();
        }
        System.out.println("----------------");
    }
}
