package com.nickbattam.firstapp;

import java.util.Date;

/**
 * Created by nick on 01/12/15.
 */
public class Blood {
    private int id;
    private Date datetime;
    private Double value;

    public Blood()
    {
    }

    public Blood(int id, Date datetime, Double value)
    {
        this.id=id;
        this.datetime=datetime;
        this.value=value;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    public int getId() {
        return id;
    }
    public Date getDatetime() {
        return datetime;
    }
    public Double getValue() {
        return value;
    }
}