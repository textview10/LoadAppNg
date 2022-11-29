package com.loadapp.load.bean;

public class CommitLoanBean {
    //订单ID，验证通过会有订单号，不通过为0
    private long order_id;
    //资料未完成时返回  当前步骤
    private int current_phase;
    //资料未完成时返回  下一步骤
    private int next_phase;
    //资料未完成时返回  进程
    private int progress;


    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public int getCurrent_phase() {
        return current_phase;
    }

    public void setCurrent_phase(int current_phase) {
        this.current_phase = current_phase;
    }

    public int getNext_phase() {
        return next_phase;
    }

    public void setNext_phase(int next_phase) {
        this.next_phase = next_phase;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
