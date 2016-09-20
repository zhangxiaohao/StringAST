package cn.edu.fudan.baseast.structure;

/**
 * Created by zhangxiaohao on 16/9/14.
 */
public class Operation {
    TimeStamp timeStamp;
    String operationString;
    Integer operationType;
    Integer position;

    public Operation(TimeStamp timeStamp, String operationString, Integer operationType, Integer position) {
        this.timeStamp = timeStamp;
        this.operationString = operationString;
        this.operationType = operationType;
        this.position = position;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getOperationString() {
        return operationString;
    }

    public void setOperationString(String operationString) {
        this.operationString = operationString;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * 获取操作之间的关系
     * @param operation
     * @return CAUSAL CONCURRENT
     */
    public int getOperationRelationship(Operation operation) {
        return this.timeStamp.getTimeStampRelationship(operation.getTimeStamp());
    }

    /**
     * 操作打印
     */
    public void print() {
        if(operationType == OperationType.INSERT) System.out.print("Insert");
        else if(operationType == OperationType.DELETE) System.out.print("Delete");
        else System.out.print("Undo");
        System.out.println(" " + position + " " + operationString);
        timeStamp.print();
    }
}
