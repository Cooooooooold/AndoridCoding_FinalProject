package com.example.experiment2.BottomUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.experiment2.R;
import com.example.experiment2.TaskItemDetailActivity;
import com.example.experiment2.data.DataBank;
import com.example.experiment2.data.TaskItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RewardsFragment extends Fragment {
    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private TaskItemAdapter taskItemAdapter;
    private String fragmentType = "RewardsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.reward_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        //定义一个Arraylist
        taskItems= new ArrayList<>();
        taskItems = new DataBank().loadrewardItems(getContext());
        if(0 == taskItems.size()){
            taskItems.add(new TaskItem("打游戏2小时",2000,false));
            taskItems.add(new TaskItem("看电视",200,false));
            taskItems.add(new TaskItem("去电影院看电影",10000,false));
            taskItems.add(new TaskItem("听音乐",50,false));
            taskItems.add(new TaskItem("旅行",1000000,false));
        }
        taskItemAdapter = new TaskItemAdapter(taskItems);
        recyclerView.setAdapter(taskItemAdapter);
        registerForContextMenu(recyclerView);//注册

        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // 处理任务数据
                        String name = data.getStringExtra("name");
                        String points = data.getStringExtra("points");
//                                String quantity = data.getStringExtra("quantity");
//                        String category = data.getStringExtra("category");
                        int point=Integer.parseInt(points);
                        // 添加到任务列表并更新界面
                        taskItems.add(new TaskItem(name, point,false));
                        taskItemAdapter.notifyItemInserted(taskItems.size());

                        new DataBank().saverewardItems(requireActivity(), taskItems);
                        Log.d("RewardFragment", "Received fragment type: " + fragmentType);
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    }
                }
        );


        updateItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int position = data.getIntExtra("position", 0);
                        String name = data.getStringExtra("name");
                        double point = data.getDoubleExtra("point", 0);
                        int number = data.getIntExtra("number", 0);
                        String category = data.getStringExtra("category");

                        TaskItem taskItem = taskItems.get(position);
                        taskItem.setName(name);
                        taskItem.setPoint(point);
                        taskItemAdapter.notifyItemChanged(position);
                        new DataBank().saverewardItems(requireActivity(), taskItems);
                    }
                }
        );
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    public void handleRewardTaskResult(Intent data) {
        // 从 Intent 中获取任务数据
        String name = data.getStringExtra("name");
        String points = data.getStringExtra("points");
        int point = Integer.parseInt(points);

        // 添加任务到 RewardsFragment 的列表中
        addTaskItem(new TaskItem(name, point, false));
    }


    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent> updateItemLauncher;
    public void addTaskItem(TaskItem taskItem) {
        Log.d("RewardsFragment", "addTaskItem: Adding task: " + taskItem.getName());
        taskItems.add(taskItem);
        taskItemAdapter.notifyItemInserted(taskItems.size() - 1);
        new DataBank().saverewardItems(requireActivity(), taskItems);
    }
    private static final int MENU_ITEM_ADD = 0;
    private static final int MENU_ITEM_DELETE = 1;
    private static final int MENU_ITEM_UPDATE = 2;
    private static final int MENU_ITEM_SORT = 3;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 检查是否是当前活动的 Fragment
        if (!isResumed()) {
            return false;
        }
        switch (item.getItemId()) {
            case MENU_ITEM_ADD:
                Intent intent = new Intent(requireActivity(), TaskItemDetailActivity.class);
                addItemLauncher.launch(intent);
                break;
            case MENU_ITEM_DELETE:
                new AlertDialog.Builder(requireContext())
                        .setTitle("删除任务")
                        .setMessage("确定要删除这个任务吗？")
                        .setPositiveButton("删除", (dialog, which) -> {
                            taskItems.remove(item.getOrder());
                            taskItemAdapter.notifyItemRemoved(item.getOrder());
                            new DataBank().saverewardItems(requireActivity(), taskItems);
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case MENU_ITEM_UPDATE:
                Intent intentUpdate = new Intent(requireActivity(), TaskItemDetailActivity.class);
                TaskItem taskItemToUpdate = taskItems.get(item.getOrder());
                intentUpdate.putExtra("name", taskItemToUpdate.getName());
                intentUpdate.putExtra("points", taskItemToUpdate.getPoint());
//                intentUpdate.putExtra("quantity", taskItemToUpdate.getNumber());
//                intentUpdate.putExtra("category", taskItemToUpdate.getcategory());
                intentUpdate.putExtra("position", item.getOrder());
                updateItemLauncher.launch(intentUpdate);
                break;
            case MENU_ITEM_SORT:
                sortTasks(); // 排序任务
                break;
//            default:
//                return super.onContextItemSelected(item);

        }
        return true;
    }

    private void sortTasks() {
        Collections.sort(taskItems, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem o1, TaskItem o2) {
                return Integer.compare((int) o2.getPoint(), (int) o1.getPoint()); // 降序排序
            }
        });
        taskItemAdapter.notifyDataSetChanged();
    }
    private void deleteTaskAndUpdatePoints(TaskItem rewardItem, int position) {
        if (position >= 0 && position < taskItems.size()) {
            TaskItem removedItem = taskItems.get(position);
            DataBank dataBank = new DataBank();
            int totalPoints = dataBank.getTotalPoints(requireActivity());

            if (totalPoints >= removedItem.getPoint()) {
                // 积分足够，扣除积分
                updateTotalPoints((int) -removedItem.getPoint()); // 使用 updateTotalPoints 方法更新积分
                rewardItem.setCompletionTime(System.currentTimeMillis()); // 记录消费时间
                new DataBank().saverewardItems(requireActivity(), taskItems);
                // 删除任务项
                taskItems.remove(position);
                taskItemAdapter.notifyItemRemoved(position);
            } else {
                // 积分不足，显示 Toast
                showToast("积分不足，无法完成此消费");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // 添加用于更新总积分的方法
    private void updateTotalPoints(int points) {
        // 实现更新积分的逻辑
        DataBank dataBank = new DataBank();
        int totalPoints = dataBank.getTotalPoints(requireActivity());
        totalPoints += points;
        dataBank.setTotalPoints(requireActivity(), totalPoints);

        // 更新显示总积分的 TextView
        TextView totalPointsTextView = getActivity().findViewById(R.id.total_point);
        totalPointsTextView.setText(String.valueOf(totalPoints));
    }
    private class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> {
        private final ArrayList<TaskItem> taskItemArrayList;

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final TextView textViewPoint;
            private final CheckBox checkboxView;

            public ViewHolder(View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.task_item_name);
                textViewPoint = itemView.findViewById(R.id.task_item_reward);
                checkboxView = itemView.findViewById(R.id.checkBox);

                itemView.setOnCreateContextMenuListener(this);
                checkboxView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        TaskItem rewardItem = taskItemArrayList.get(position);
                        // 使用 Fragment 引用调用删除方法
                        deleteTaskAndUpdatePoints(rewardItem,position);
                    }
                });
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("更多操作");
                menu.add(0, MENU_ITEM_ADD, getAdapterPosition(), "添加"+this.getAdapterPosition());
                menu.add(0, MENU_ITEM_DELETE, getAdapterPosition(), "删除"+this.getAdapterPosition());
                menu.add(0, MENU_ITEM_UPDATE, getAdapterPosition(), "修改"+this.getAdapterPosition());
                menu.add(0, MENU_ITEM_SORT, getAdapterPosition(), "排序"+this.getAdapterPosition());
            }
            public TextView getTaskName() {
                return textViewName;
            }

            public TextView getTaskPoint() {
                return textViewPoint;
            }
            public CheckBox getCheckBox(){return checkboxView;}
        }
        public TaskItemAdapter(ArrayList<TaskItem> taskItems) {
            taskItemArrayList = taskItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_item_row, parent, false);
            return new TaskItemAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            TaskItem taskItem = taskItemArrayList.get(position);
            holder.textViewName.setText(taskItem.getName());
            holder.textViewPoint.setText(String.valueOf(taskItem.getPoint()));
//            holder.itemView.setTag(position); // 存储当前位置作为tag
            // Set image resource if needed
        }

        @Override
        public int getItemCount() {
            return taskItemArrayList.size();
        }

    }
}