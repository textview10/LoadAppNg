package com.loadapp.load.bean;

import java.util.List;

public class OrderInfoBean {

    private OrderDetail order_detail;

    public OrderDetail getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(OrderDetail order_detail) {
        this.order_detail = order_detail;
    }

    public static class OrderDetail {
        //订单ID
        private String order_id;
        //订单状态， 状态内容请查看 sheet 订单状态说明
        private int check_status;
        //拒绝消息
        private String reject_message;
        //拒绝之后，剩余多少天可以申请
        private int limit_day;
        //是否可以申请
        private boolean can_apply;
        //首借，复借， 0 首借 1复借
        private boolean is_reloan;
        //（分期信息）
        private List<Stage> stages;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public int getCheck_status() {
            return check_status;
        }

        public void setCheck_status(int check_status) {
            this.check_status = check_status;
        }

        public String getReject_message() {
            return reject_message;
        }

        public void setReject_message(String reject_message) {
            this.reject_message = reject_message;
        }

        public int getLimit_day() {
            return limit_day;
        }

        public void setLimit_day(int limit_day) {
            this.limit_day = limit_day;
        }

        public boolean isCan_apply() {
            return can_apply;
        }

        public void setCan_apply(boolean can_apply) {
            this.can_apply = can_apply;
        }

        public boolean isIs_reloan() {
            return is_reloan;
        }

        public void setIs_reloan(boolean is_reloan) {
            this.is_reloan = is_reloan;
        }

        public List<Stage> getStages() {
            return stages;
        }

        public void setStages(List<Stage> stages) {
            this.stages = stages;
        }


    }

    public static class Stage {
        //当前分期
        private int stage_no;
        //应还总额
        private int total_amount;
        //还款日期
        private int repay_date;
        //应还本金
        private int amount;
        //利息
        private int interest;
        //砍头利息
        private int interest_pre_paid;
        //服务费
        private int service_fee;
        //砍头服务费
        private int service_fee_pre_paid;
        //罚息
        private int penalty;

        public int getStage_no() {
            return stage_no;
        }

        public void setStage_no(int stage_no) {
            this.stage_no = stage_no;
        }

        public int getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(int total_amount) {
            this.total_amount = total_amount;
        }

        public int getRepay_date() {
            return repay_date;
        }

        public void setRepay_date(int repay_date) {
            this.repay_date = repay_date;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getInterest() {
            return interest;
        }

        public void setInterest(int interest) {
            this.interest = interest;
        }

        public int getInterest_pre_paid() {
            return interest_pre_paid;
        }

        public void setInterest_pre_paid(int interest_pre_paid) {
            this.interest_pre_paid = interest_pre_paid;
        }

        public int getService_fee() {
            return service_fee;
        }

        public void setService_fee(int service_fee) {
            this.service_fee = service_fee;
        }

        public int getService_fee_pre_paid() {
            return service_fee_pre_paid;
        }

        public void setService_fee_pre_paid(int service_fee_pre_paid) {
            this.service_fee_pre_paid = service_fee_pre_paid;
        }

        public int getPenalty() {
            return penalty;
        }

        public void setPenalty(int penalty) {
            this.penalty = penalty;
        }
    }
}
