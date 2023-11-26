package com.example.experiment2.TopUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.experiment2.BookitemDetailsActivity;
import com.example.experiment2.R;
import com.example.experiment2.TaskItemDetailActivity;
import com.example.experiment2.data.DataBank;
import com.example.experiment2.data.TaskItem;

import java.util.ArrayList;

public class DailyTaskFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private TaskItemAdapter taskItemAdapter;

    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent> updateItemLauncher;

    private static final int MENU_ITEM_ADD = 0;
    private static final int MENU_ITEM_DELETE = 1;
    private static final int MENU_ITEM_UPDATE = 2;

    public DailyTaskFragment() {
        // Required empty public constructor
    }

    public static DailyTaskFragment newInstance() {
        DailyTaskFragment fragment = new DailyTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // 处理参数
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        RecyclerView mainRecyclerView = rootView.findViewById(R.id.dailyrecycler_view);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        taskItems = new DataBank().loadTaskItems(requireActivity());
        if (0 == taskItems.size()) {
            // 添加示例数据
            taskItems.add(new TaskItem("看书", 500, 5,"学习"));
        }

        taskItemAdapter = new TaskItemAdapter(taskItems);
        mainRecyclerView.setAdapter(taskItemAdapter);

        registerForContextMenu(mainRecyclerView);

        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        double points = data.getDoubleExtra("points", 0);
                        int quantity = data.getIntExtra("quantity", 0);
                        String category = data.getStringExtra("category");

                        taskItems.add(new TaskItem(name, points, quantity, category));
                        taskItemAdapter.notifyItemInserted(taskItems.size() - 1);

                        new DataBank().SaveTaskItems(requireActivity(), taskItems);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // 处理取消操作
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
                        double point = data.getDoubleExtra("price", 0); // 使用与TaskItemDetailActivity中一致的键
                        int number = data.getIntExtra("number", 0); // 假设您也处理了“number”
                        String category = data.getStringExtra("category"); // 假设您也处理了“category”

                        TaskItem taskItem = taskItems.get(position);
                        taskItem.setName(name);
                        taskItem.setPoint(point);
                        // 更新其他属性
                        // taskItem.setNumber(number); // 假设TaskItem有这个方法
                        // taskItem.setCategory(category); // 假设TaskItem有这个方法

                        taskItemAdapter.notifyItemChanged(position);

                        // 保存数据
                        new DataBank().SaveTaskItems(requireActivity(), taskItems);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // 处理取消操作
                    }
                }
        );


        return rootView;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = item.getOrder();
        switch (item.getItemId()) {
            case MENU_ITEM_ADD:
                // 添加任务项的逻辑
                Intent intent = new Intent(requireActivity(), TaskItemDetailActivity.class);
                addItemLauncher.launch(intent);
                break;
            case MENU_ITEM_DELETE:
                // 删除任务项的逻辑
                new AlertDialog.Builder(requireContext())
                        .setTitle("删除任务")
                        .setMessage("确定要删除这个任务吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskItems.remove(position);
                                taskItemAdapter.notifyItemRemoved(position);
                                new DataBank().SaveTaskItems(requireActivity(), taskItems);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case MENU_ITEM_UPDATE:
                // 更新任务项的逻辑
                TaskItem taskItemToUpdate = taskItems.get(position);
                Intent intentUpdate = new Intent(requireActivity(), TaskItemDetailActivity.class);
                intentUpdate.putExtra("name", taskItemToUpdate.getName());
                intentUpdate.putExtra("points", taskItemToUpdate.getPoint()); // 确保使用正确的字段
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
    public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> {
        private ArrayList<TaskItem> taskItemArrayList;
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final TextView textViewPrice;
            private final ImageView imageViewItem;
            private final CheckBox checkboxView;

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("更多操作");
                menu.add(0, 0, getAdapterPosition(), "添加" + getAdapterPosition());
                menu.add(0, 1, getAdapterPosition(), "删除" + getAdapterPosition());
                menu.add(0, 2, getAdapterPosition(), "修改" + getAdapterPosition());
            }

            public ViewHolder(View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.task_item_name);
                textViewPrice = itemView.findViewById(R.id.task_item_reward);
                imageViewItem = itemView.findViewById(R.id.imageView_item);
                checkboxView = itemView.findViewById(R.id.checkBox);

                itemView.setOnCreateContextMenuListener(this);
            }

            public TextView getTextViewName() {
                return textViewName;
            }

            public TextView getTextViewPrice() {
                return textViewPrice;
            }

            public ImageView getImageViewItem() {
                return imageViewItem;
            }
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
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TaskItem taskItem = taskItemArrayList.get(position);
            holder.getTextViewName().setText(taskItem.getName());
            holder.getTextViewPrice().setText(String.valueOf(taskItem.getPoint()));
            // holder.getImageViewItem().setImageResource(taskItem.getImageResourceId());

//            holder.checkboxView.setOnClickListener(v -> {
//                int currentPosition = holder.getAdapterPosition();
//                taskItem = taskItemArrayList.get(currentPosition);
//                taskItem.setQuantity(taskItem.getQuantity() - 1);
//
//                if (taskItem.getQuantity() <= 0) {
//                    taskItemArrayList.remove(currentPosition);
//                    notifyItemRemoved(currentPosition);
//                } else {
//                    notifyItemChanged(currentPosition);
//                }
//            });
        }


        @Override
        public int getItemCount() {
            return taskItemArrayList.size();
        }
    }
}