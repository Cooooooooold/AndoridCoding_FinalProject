package com.example.experiment2.data;

public class PointChange {
    private long timestamp;
    private int pointsChange; // 正数为收入，负数为支出

    // 构造函数
    public PointChange(long timestamp, int pointsChange) {
        this.timestamp = timestamp;
        this.pointsChange = pointsChange;
    }

    // 获取时间戳
    public long getTimestamp() {
        return timestamp;
    }

    // 获取积分变化量
    public int getPointsChange() {
        return pointsChange;
    }
}
