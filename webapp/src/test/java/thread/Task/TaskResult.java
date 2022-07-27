package thread.Task;



import java.io.Serializable;

public class TaskResult implements Serializable {
    private static final long serialVersionUID = 8678277072402730062L;
    /**
     * 任务状态
     */
    private Integer taskStatus;
    /**
     * 任务消息
     */
    private String taskMessage;
    /**
     * 任务结果数据
     */
    private String taskResult;

    public TaskResult() {
    }

    //省略getter和setter方法
    @Override
    public String toString() {
        return "TaskResult{" +
                "taskStatus=" + taskStatus +
                ", taskMessage='" + taskMessage + '\'' +
                ", taskResult='" + taskResult + '\'' +
                '}';
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setTaskMessage(String taskMessage) {
        this.taskMessage = taskMessage;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }
}
