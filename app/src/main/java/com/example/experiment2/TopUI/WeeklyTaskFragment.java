package com.example.experiment2.TopUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.experiment2.R;
import com.example.experiment2.TaskItemDetailActivity;
import com.example.experiment2.data.DataBank;
import com.example.experiment2.data.TaskItem;

import java.util.ArrayList;
public class WeeklyTaskFragment extends Fragment {
    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private TaskItemAdapter taskItemAdapter;
    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent> updateItemLauncher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weekly_task, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.weekly_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        taskItems = new ArrayList<>();
        taskItems = new DataBank().loadTaskItems(getContext(), "weekly_tasks.data");
        if (0 == taskItems.size()) {
            taskItems.add(new TaskItem("背单词", 500, 5, "学习"));
        }
        taskItemAdapter = new TaskItemAdapter(taskItems);
        recyclerView.setAdapter(taskItemAdapter);
        registerForContextMenu(recyclerView);

        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // 检查返回的任务类型是否为 "每周任务"
                            String fragmentType = data.getStringExtra("fragmentType");
                            if ("每周任务".equals(fragmentType)) {
                                // 处理任务数据
                                String name = data.getStringExtra("name");
                                double points = data.getDoubleExtra("points", 0);
                                int quantity = data.getIntExtra("quantity", 0);
                                String category = data.getStringExtra("category");

                                TaskItem newTask = new TaskItem(name, points, quantity, category);
                                taskItems.add(newTask);
                                taskItemAdapter.notifyItemInserted(taskItems.size());
                                new DataBank().saveTaskItems(requireActivity(), taskItems,"weekly_tasks.data");
                                Log.d("WeeklyTaskFragment", "Received fragment type: " + fragmentType);
                            }
                        }
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
                    }
                }
        );
        return rootView;
    }
    public void addTaskItem(TaskItem taskItem) {
        Log.d("WeeklyTaskFragment", "addTaskItem: Adding task: " + taskItem.getName());
        taskItems.add(taskItem);
        taskItemAdapter.notifyItemInserted(taskItems.size() - 1);
        new DataBank().saveTaskItems(requireActivity(), taskItems, "weekly_tasks.data");
    }
    private static final int MENU_ITEM_ADD = 0;
    private static final int MENU_ITEM_DELETE = 1;
    private static final int MENU_ITEM_UPDATE = 2;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = item.getOrder();
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
                            taskItems.remove(position);
                            taskItemAdapter.notifyItemRemoved(position);
                            new DataBank().saveTaskItems(requireActivity(),taskItems,"weekly_tasks.data");
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case MENU_ITEM_UPDATE:
                TaskItem taskItemToUpdate = taskItems.get(position);
                Intent intentUpdate = new Intent(requireActivity(), TaskItemDetailActivity.class);
                intentUpdate.putExtra("name", taskItemToUpdate.getName());
                intentUpdate.putExtra("points", taskItemToUpdate.getPoint());
                intentUpdate.putExtra("quantity", taskItemToUpdate.getNumber());
                intentUpdate.putExtra("category", taskItemToUpdate.getcategory());
                intentUpdate.putExtra("position", position);
                updateItemLauncher.launch(intentUpdate);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> {
        private final ArrayList<TaskItem> taskItemArrayList;
        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final TextView textViewPrice;
            private final ImageView imageViewItem;
            private final CheckBox checkboxView;

            public ViewHolder(View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.task_item_name);
                textViewPrice = itemView.findViewById(R.id.task_item_reward);
                imageViewItem = itemView.findViewById(R.id.imageView_item);
                checkboxView = itemView.findViewById(R.id.checkBox);

                itemView.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("更多操作");
                menu.add(0, MENU_ITEM_ADD, getAdapterPosition(), "添加");
                menu.add(0, MENU_ITEM_DELETE, getAdapterPosition(), "删除");
                menu.add(0, MENU_ITEM_UPDATE, getAdapterPosition(), "修改");
            }
        }
        public TaskItemAdapter(ArrayList<TaskItem> taskItems) {
            this.taskItemArrayList = taskItems;
        }

        @NonNull
        @Override
        public TaskItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_item_row, parent, false);
            return new TaskItemAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskItemAdapter.ViewHolder holder, int position) {
            TaskItem taskItem = taskItemArrayList.get(position);
            holder.textViewName.setText(taskItem.getName());
            holder.textViewPrice.setText(String.valueOf(taskItem.getPoint()));
            // Set image resource if needed
        }

        @Override
        public int getItemCount() {
            return taskItemArrayList.size();
        }


    }
}
