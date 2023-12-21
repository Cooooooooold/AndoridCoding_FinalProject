package com.example.experiment2.data;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBank {
    final String DAILY_TASK_DATA_FILENAME="daily_tasks.data";
    final String WEEKLY_TASK_DATA_FILENAME="weekly_tasks.data";
    final String NORMAL_TASK_DATA_FILENAME="normal_tasks.data";

    // 加载任务项数据
    public ArrayList<TaskItem> loaddailyItems(Context context) {
        ArrayList<TaskItem> data = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(DAILY_TASK_DATA_FILENAME);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            data = (ArrayList<TaskItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            Log.d("Serialization","DailyTask loaded successfully. item count"+data.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 保存任务项数据
    public void savedailyItems(Context context, ArrayList<TaskItem> taskItems) {
        try {
            FileOutputStream fileOut = context.openFileOutput(DAILY_TASK_DATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(taskItems);
            objectOut.close();
            fileOut.close();
            Log.d("Serialization","DailyTask is serialized and saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<TaskItem> loadweeklyItems(Context context) {
        ArrayList<TaskItem> data = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(WEEKLY_TASK_DATA_FILENAME);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            data = (ArrayList<TaskItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            Log.d("Serialization","WeeklyTask loaded successfully. item count"+data.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 保存任务项数据
    public void saveweeklyItems(Context context, ArrayList<TaskItem> taskItems) {
        try {
            FileOutputStream fileOut = context.openFileOutput(WEEKLY_TASK_DATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(taskItems);
            objectOut.close();
            fileOut.close();
            Log.d("Serialization","WeeklyTask is serialized and saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<TaskItem> loadnormalItems(Context context) {
        ArrayList<TaskItem> data = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(NORMAL_TASK_DATA_FILENAME);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            data = (ArrayList<TaskItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            Log.d("Serialization","NormalTask loaded successfully. item count"+data.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 保存任务项数据
    public void savenormalItems(Context context, ArrayList<TaskItem> taskItems) {
        try {
            FileOutputStream fileOut = context.openFileOutput(NORMAL_TASK_DATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(taskItems);
            objectOut.close();
            fileOut.close();
            Log.d("Serialization","NormalTask is serialized and saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
