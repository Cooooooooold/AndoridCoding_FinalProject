package com.example.experiment2.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

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
    final String REWARD_TASK_DATA_FILENAME="reward_tasks.data";

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
    public ArrayList<TaskItem> loadrewardItems(Context context) {
        ArrayList<TaskItem> data = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(REWARD_TASK_DATA_FILENAME);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            data = (ArrayList<TaskItem>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            // 添加日志以显示加载的奖励项数量
            Log.d("DataBank", "Loaded " + data.size() + " reward items.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
    public void saverewardItems(Context context, ArrayList<TaskItem> taskItems) {
        try {
            FileOutputStream fileOut = context.openFileOutput(REWARD_TASK_DATA_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(taskItems);
            objectOut.close();
            fileOut.close();
            Log.d("Serialization","Reward is serialized and saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<TaskItem> loadTotalTaskItems(Context context) {
        ArrayList<TaskItem> totalTasks = new ArrayList<>();

        // 加载并添加每日任务项
        ArrayList<TaskItem> dailyTasks = loaddailyItems(context);
        totalTasks.addAll(dailyTasks);

        // 加载并添加每周任务项
        ArrayList<TaskItem> weeklyTasks = loadweeklyItems(context);
        totalTasks.addAll(weeklyTasks);

        // 加载并添加普通任务项
        ArrayList<TaskItem> normalTasks = loadnormalItems(context);
        totalTasks.addAll(normalTasks);

        return totalTasks;
    }


    public void savePointChanges(Context context, ArrayList<PointChange> changes) {
        // 实现保存 pointChanges 的逻辑
        try {
            FileOutputStream fileOut = context.openFileOutput("PointChangesDataFile", Context.MODE_PRIVATE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(changes);
            objectOut.close();
            fileOut.close();
            Log.d("Serialization", "Point changes are serialized and saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PointChange> loadPointChanges(Context context) {
        ArrayList<PointChange> data = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput("PointChangesDataFile");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            data = (ArrayList<PointChange>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            Log.d("DataBank", "Loaded " + data.size() + " point changes.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 获取当前的总积分
    public int getTotalPoints(Context context) {
        // 实现获取总积分的逻辑
        SharedPreferences prefs = context.getSharedPreferences("TotalPointsPrefs", Context.MODE_PRIVATE);
        return prefs.getInt("TotalPoints", 0);
    }

    // 更新总积分
    public void setTotalPoints(Context context, int totalPoints) {
        // 实现更新总积分的逻辑
        SharedPreferences prefs = context.getSharedPreferences("TotalPointsPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("TotalPoints", totalPoints);
        editor.apply();
    }
}
