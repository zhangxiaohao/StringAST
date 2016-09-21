package stringast.structure;

import cn.edu.fudan.baseast.structure.OperationType;
import org.omg.CORBA.INTERNAL;

/**
 * Created by zhangxiaohao on 2016/9/21.
 */
public class S_Operation {
    /**
     * 操作时间戳
     */
    S_TimeStamp s_timeStamp;
    /**
     * 操作目标字符串，仅仅对插入操作有效
     */
    StringBuilder operationString;
    /**
     * 操作类型
     */
    Integer operationType;
    /**
     * 操作的起始位置
     */
    Integer position;
    /**
     * 对于插入操作是插入字符串的长度
     * 对于删除操作是删除多少个字符
     */
    Integer length;

    public S_Operation(S_TimeStamp s_timeStamp, StringBuilder operationString, Integer operationType, Integer position, Integer length) {
        this.s_timeStamp = s_timeStamp;
        this.operationString = operationString;
        this.operationType = operationType;
        this.position = position;
        this.length = length;
    }

    /**
     * 深拷贝函数
     * @param s_operation
     */
    public S_Operation(S_Operation s_operation) {
        this.s_timeStamp = new S_TimeStamp(s_operation.getS_timeStamp());
        this.operationString = new StringBuilder(s_operation.getOperationString());
        this.operationType = s_operation.getOperationType();
        this.position = s_operation.getPosition();
        this.length = s_operation.getLength();
    }

    public S_TimeStamp getS_timeStamp() {
        return s_timeStamp;
    }

    public void setS_timeStamp(S_TimeStamp s_timeStamp) {
        this.s_timeStamp = s_timeStamp;
    }

    public StringBuilder getOperationString() {
        return operationString;
    }

    public void setOperationString(StringBuilder operationString) {
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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * 获取操作之间的关系
     * @param s_operation
     * @return CAUSAL CONCURRENT
     */
    public int getOperationRelationShip(S_Operation s_operation) {
        return this.s_timeStamp.getTimeStampRelationship(s_operation.getS_timeStamp());
    }

    /**
     * 操作打印
     */
    public void print() {
        if(operationType == OperationType.INSERT) System.out.print("Insert");
        else if(operationType == OperationType.DELETE) System.out.print("Delete");
        else System.out.print("Undo");
        System.out.println(" " + position + " " + length + " " + operationString);
        s_timeStamp.print();
    }
}
