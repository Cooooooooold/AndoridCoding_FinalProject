package com.example.experiment2.TopUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experiment2.BottomUI.MissionsFragment;
import com.example.experiment2.R;
import com.example.experiment2.TaskItemDetailActivity;
import com.example.experiment2.data.DataBank;
import com.example.experiment2.data.PointChange;
import com.example.experiment2.data.TaskItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DailyTaskFragment extends Fragment {

    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private TaskItemAdapter taskItemAdapter;
    private String fragmentType = "DailyTaskFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_task, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.daily_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        //定义一个Arraylist
        taskItems= new ArrayList<>();
        taskItems = new DataBank().loaddailyItems(getContext());
        if(0 == taskItems.size()){
            taskItems.add(new TaskItem("读一篇英文文章",500,false));
            taskItems.add(new TaskItem("读一本课外读物",600,false));
            taskItems.add(new TaskItem("写5题数学题",700,false));
            taskItems.add(new TaskItem("背单词30个",1000,false));
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
                                int point=Integer.parseInt(points);
                                // 添加到任务列表并更新界面
                                taskItems.add(new TaskItem(name, point,false));
                                taskItemAdapter.notifyItemInserted(taskItems.size());
                                new DataBank().savedailyItems(requireActivity(), taskItems);
                                Log.d("DailyTaskFragment", "Received fragment type: " + fragmentType);
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

                        TaskItem taskItem = taskItems.get(position);
                        taskItem.setName(name);
                        taskItem.setPoint(point);
                        taskItemAdapter.notifyItemChanged(position);
                        new DataBank().savedailyItems(requireActivity(), taskItems);
                    }
                }
        );

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MissionsFragment) getParentFragment()).setCurrentActiveFragment(this);
    }
    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent> updateItemLauncher;
    public void addTaskItem(TaskItem taskItem) {
        Log.d("DailyTaskFragment", "addTaskItem: Adding task: " + taskItem.getName());
        taskItems.add(taskItem);
        taskItemAdapter.notifyItemInserted(taskItems.size() - 1);
        new DataBank().savedailyItems(requireActivity(), taskItems);
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
                            new DataBank().savedailyItems(requireActivity(), taskItems);
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case MENU_ITEM_UPDATE:
                Intent intentUpdate = new Intent(requireActivity(), TaskItemDetailActivity.class);
                TaskItem taskItemToUpdate = taskItems.get(item.getOrder());
                intentUpdate.putExtra("name", taskItemToUpdate.getName());
                intentUpdate.putExtra("points", taskItemToUpdate.getPoint());
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
    // 添加一个方法来处理任务项的删除和积分更新
    private void deleteTaskAndUpdatePoints(int position) {
        if (position >= 0 && position < taskItems.size()) {
            TaskItem removedItem = taskItems.remove(position);
            taskItemAdapter.notifyItemRemoved(position);

//            // 调用方法来更新总积分
            updateTotalPoints((int) removedItem.getPoint());
        }
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

//    private void updateChart() {
//        if (getView() != null) {
//            setupLineChart(getView());
//        }
//    }

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
                        TaskItem taskItem = taskItemArrayList.get(position);
                        if (checkboxView.isChecked()) {
                            // 当 CheckBox 被勾选时，记录当前时间
                            taskItem.setCompletionTime(System.currentTimeMillis());
                            new DataBank().savedailyItems(requireActivity(), taskItemArrayList);

                        } else {
                            // 当 CheckBox 被取消勾选时，可以选择重置完成时间
                            taskItem.setCompletionTime(0);
                        }
                        // 使用 Fragment 引用调用删除方法
                        deleteTaskAndUpdatePoints(position);
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
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            TaskItem taskItem = taskItemArrayList.get(position);
            holder.textViewName.setText(taskItem.getName());
            holder.textViewPoint.setText(String.valueOf(taskItem.getPoint()));
        }

        @Override
        public int getItemCount() {
            return taskItemArrayList.size();
        }


    }
}
