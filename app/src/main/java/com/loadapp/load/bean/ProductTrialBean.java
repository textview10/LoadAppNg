package com.loadapp.load.bean;

import java.util.List;

public class ProductTrialBean {

    private List<Trial> trials;

    public List<Trial> getTrials() {
        return trials;
    }

    public void setTrials(List<Trial> trials) {
        this.trials = trials;
    }

    public static class Trial {
        //当前期数
        private int stage_no;
        //总期数
        private int stages;
        //产品名字
        private String name;
        //还款日期
        private long repay_date;
        //应还款总金额
        private int total;
        //本金
        private int amount;
        //放款金额
        private int disburse_amount;
        //服务费
        private int service_fee;
        //砍头服务费，非砍头产品为0
        private int service_fee_prepaid;
        //利息
        private int interest;
        //砍头利息，非砍头产品为0
        private int interest_prepaid;

        public int getStage_no() {
            return stage_no;
        }

        public void setStage_no(int stage_no) {
            this.stage_no = stage_no;
        }

        public int getStages() {
            return stages;
        }

        public void setStages(int stages) {
            this.stages = stages;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getRepay_date() {
            return repay_date;
        }

        public void setRepay_date(long repay_date) {
            this.repay_date = repay_date;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getDisburse_amount() {
            return disburse_amount;
        }

        public void setDisburse_amount(int disburse_amount) {
            this.disburse_amount = disburse_amount;
        }

        public int getService_fee() {
            return service_fee;
        }

        public void setService_fee(int service_fee) {
            this.service_fee = service_fee;
        }

        public int getService_fee_prepaid() {
            return service_fee_prepaid;
        }

        public void setService_fee_prepaid(int service_fee_prepaid) {
            this.service_fee_prepaid = service_fee_prepaid;
        }

        public int getInterest() {
            return interest;
        }

        public void setInterest(int interest) {
            this.interest = interest;
        }

        public int getInterest_prepaid() {
            return interest_prepaid;
        }

        public void setInterest_prepaid(int interest_prepaid) {
            this.interest_prepaid = interest_prepaid;
        }
    }

}
