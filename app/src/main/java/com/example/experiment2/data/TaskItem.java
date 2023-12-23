package com.example.experiment2.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TaskItem implements Serializable {
    private long completionTime; // 任务完成时间
    private String name;
    private double point;
    private boolean done; // 任务是否完成

    public TaskItem(String name_, double point_, boolean done_) {
        this.name = name_;
        this.point = point_;
        this.done = done_;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getPoint() {
        return point;
    }

    public boolean isDone() {
        return done;
    }
    // 设置任务完成时间
    public void setCompletionTime(long completionTime) {
        this.completionTime = completionTime;
    }

    // 获取任务完成时间
    public long getCompletionTime() {
        return completionTime;
    }
    class PointChange {
        long timestamp;
        int pointsChange; // 正数表示收入，负数表示支出

        // 构造函数、getter和setter省略
    }
}
