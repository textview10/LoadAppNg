package com.loadapp.load.bean;

import org.json.JSONObject;

public class BaseConfigBean {
    //月收入
    private JSONObject month_salary;
    //联系人关系
    private JSONObject relationship;
    //工作年限
    private JSONObject working_year;
    //所在州，市
    private JSONObject state_city;
    //返回时间
    private JSONObject server_time;

    public JSONObject getMonth_salary() {
        return month_salary;
    }

    public void setMonth_salary(JSONObject month_salary) {
        this.month_salary = month_salary;
    }

    public JSONObject getRelationship() {
        return relationship;
    }

    public void setRelationship(JSONObject relationship) {
        this.relationship = relationship;
    }

    public JSONObject getWorking_year() {
        return working_year;
    }

    public void setWorking_year(JSONObject working_year) {
        this.working_year = working_year;
    }

    public JSONObject getState_city() {
        return state_city;
    }

    public void setState_city(JSONObject state_city) {
        this.state_city = state_city;
    }

    public JSONObject getServer_time() {
        return server_time;
    }

    public void setServer_time(JSONObject server_time) {
        this.server_time = server_time;
    }
}
