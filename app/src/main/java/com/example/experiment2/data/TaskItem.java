package com.example.experiment2.data;

import java.io.Serializable;

public class TaskItem implements Serializable { //对象实现序列化

    public int getNumber() {

        return number;
    }
    public String getName() {
        return name;
    }
    public String getcategory() {
        return category;
    }
    public double getPoint() {
        return point;
    }
    private final int number;
    private  String name;
    private  double point;
    private  String category;
    public TaskItem(String name_, double point_, int number_, String category_) {
        this.name=name_;
        this.point =point_;
        this.number =number_;
        this.category =category_;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public void setName(String name) {
        this.name = name;
    }
}
