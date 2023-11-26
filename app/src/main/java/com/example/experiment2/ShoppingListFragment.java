package com.example.experiment2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.experiment2.data.DataBank;
import com.example.experiment2.data.ShopItem;

import java.util.ArrayList;

public class ShoppingListFragment extends Fragment {

    public ShoppingListFragment() {
        // Required empty public constructor
    }


    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
        View rootview = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        RecyclerView mainRecyclerView = rootview.findViewById(R.id.recycler_view);// 创建布局管理器
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        //定义一个Arraylist
        shopItems= new ArrayList<>();

        shopItems = new DataBank().LoadShopItems(requireActivity());//静态
        if(0 == shopItems.size()){
            shopItems.add(new ShopItem("信息安全数学基础（第2版）",59,R.drawable.book_1));
        }
//        shopItems.add(new ShopItem("信息安全数学基础（第2版）",59,R.drawable.book_1));
//        shopItems.add(new ShopItem("软件项目管理案例教程（第4版）",60, R.drawable.book_2));
//        shopItems.add(new ShopItem("创新工程实践",65, R.drawable.book_no_name));

        //数组是固定的，不方便插入数据
//        String []itemNames = new String[]{"商品1","商品2","商品3"};//数据

        shopItemAdapter = new ShopItemAdapter(shopItems);//接收一个数组
        mainRecyclerView.setAdapter(shopItemAdapter);

        registerForContextMenu(mainRecyclerView);//注册

        addItemlauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        String name =data.getStringExtra("name");
                        String priceText =data.getStringExtra("price");

                        double price = Double.parseDouble(priceText);
                        shopItems.add(new ShopItem(name,price,R.drawable.book_no_name));
                        shopItemAdapter.notifyItemInserted(shopItems.size());

                        new DataBank().SaveShopItems(requireActivity(), shopItems);


                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED){
                    }
                }
        );
        updateItemlauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();

                        int position = data.getIntExtra("position",0);
                        String name =data.getStringExtra("name");
                        String priceText =data.getStringExtra("price");


                        double price = Double.parseDouble(priceText);
                        ShopItem shopItem = shopItems.get(position);
                        shopItem.setPrice(price);
                        shopItem.setName(name);

                        shopItemAdapter.notifyItemChanged(position);

                        new DataBank().SaveShopItems(requireActivity(), shopItems);


                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED){
                    }
                }
        );
        return rootview;
    }
    //将 shopItems 和 shopItemAdapter 定义为类的成员变量
    private ArrayList<ShopItem> shopItems = new ArrayList<>();
    private ShopItemAdapter shopItemAdapter;

    ActivityResultLauncher<Intent> addItemlauncher;
    ActivityResultLauncher<Intent> updateItemlauncher;
    private static final int MENU_ITEM_ADD = 0;
    private static final int MENU_ITEM_DELETE = 1;
    private static final int MENU_ITEM_UPDATE = 2;

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int position = item.getOrder();  // 获取被点击的项的位置
        switch (item.getItemId())
        {
            //使用描述性的名字代替case0，case1，case2
            case MENU_ITEM_ADD:
                Intent intent = new Intent(requireActivity(), BookitemDetailsActivity.class);
                addItemlauncher.launch(intent);
                break;
            case MENU_ITEM_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("确认删除");
                builder.setMessage("确定要删除吗？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击了确认按钮，执行删除操作
                        shopItems.remove(position);  // 从数据集中删除项
                        shopItemAdapter.notifyItemRemoved(position);  // 通知适配器项已删除
                        new DataBank().SaveShopItems(requireActivity(),shopItems);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击了取消按钮，不执行删除操作
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case MENU_ITEM_UPDATE:
                Intent intentUpdate = new Intent(requireActivity(), BookitemDetailsActivity.class);
                ShopItem shopItem = shopItems.get(item.getOrder());
                intentUpdate.putExtra("name",shopItem.getName());
                intentUpdate.putExtra("price",shopItem.getPrice());
                intentUpdate.putExtra("position",item.getOrder());

                updateItemlauncher.launch(intentUpdate);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {

        private ArrayList<ShopItem> shopItemArrayList;


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final TextView textViewPrice;
            private final ImageView imageViewItem;
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");

                menu.add(0,0, this.getAdapterPosition(),"添加"+this.getAdapterPosition());
                menu.add(0,1, this.getAdapterPosition(),"删除"+this.getAdapterPosition());
                menu.add(0,2, this.getAdapterPosition(),"修改"+this.getAdapterPosition());
            }

            public ViewHolder(View shopItemView) {
                super(shopItemView);
                // Define click listener for the ViewHolder's View

                //id并不是唯一，记得加view.,要不然找出来的是一个数组
                textViewName = shopItemView.findViewById(R.id.task_item_name);
                textViewPrice = shopItemView.findViewById(R.id.task_item_reward);
                imageViewItem =shopItemView.findViewById(R.id.imageView_item);

                shopItemView.setOnCreateContextMenuListener(this);
            }

            public TextView getTextViewName() {
                return textViewName;
            }
            public TextView getTextViewPrice() {
                return textViewPrice;
            }
            public ImageView getIamgeViewItem() {
                return imageViewItem;
            }


        }
        public ShopItemAdapter(ArrayList<ShopItem> shopItems) {
            shopItemArrayList = shopItems;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())//布局渲染器
                    .inflate(R.layout.shop_item_row, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getTextViewName().setText(shopItemArrayList.get(position).getName());//绑定name
            viewHolder.getTextViewPrice().setText(shopItemArrayList.get(position).getPrice()+"");//绑定价格
            viewHolder.getIamgeViewItem().setImageResource(shopItemArrayList.get(position).getImageResouceid());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return shopItemArrayList.size();
        }
    }
}