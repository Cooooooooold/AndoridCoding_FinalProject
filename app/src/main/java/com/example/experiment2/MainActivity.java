package com.example.experiment2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.experiment2.BottomUI.MesFragment;
import com.example.experiment2.BottomUI.MissionsFragment;
import com.example.experiment2.BottomUI.RewardsFragment;
import com.example.experiment2.TopUI.DailyTaskFragment;
import com.example.experiment2.TopUI.NormalTaskFragment;
import com.example.experiment2.TopUI.WeeklyTaskFragment;
import com.example.experiment2.data.TaskItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{
    private BottomNavigationView bottomNav;
    private MissionsFragment missionsFragment;
    private RewardsFragment rewardsFragment;
    private MesFragment mesFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalproject_main);


        bottomNav = findViewById(R.id.bottom_navigation);

        // 检查是否重新创建了 Activity
        if (savedInstanceState == null) {
            // 第一次创建 Activity 时添加 Fragment
            missionsFragment = new MissionsFragment();
            rewardsFragment = new RewardsFragment();
            mesFragment = new MesFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nav_container, missionsFragment, "Missions")
                    .hide(missionsFragment)
                    .add(R.id.nav_container, rewardsFragment, "Rewards")
                    .hide(rewardsFragment)
                    .add(R.id.nav_container, mesFragment, "Mes")
                    .hide(mesFragment)
                    .commit();
            activeFragment = missionsFragment;
        } else {
            // 从保存的状态中恢复 Fragment 实例
            missionsFragment = (MissionsFragment) getSupportFragmentManager().findFragmentByTag("Missions");
            rewardsFragment = (RewardsFragment) getSupportFragmentManager().findFragmentByTag("Rewards");
            mesFragment = (MesFragment) getSupportFragmentManager().findFragmentByTag("Mes");
            activeFragment = getSupportFragmentManager().getFragment(savedInstanceState, "activeFragment");
        }
        // 显示当前 Fragment
        getSupportFragmentManager().beginTransaction().show(activeFragment).commit();

        // 设置底部导航栏的点击事件监听器
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_item_missions) {
                switchFragment(missionsFragment);
                return true;
            } else if (id == R.id.nav_item_rewards) {
                switchFragment(rewardsFragment);
                return true;
            } else if (id == R.id.nav_item_mes) {
                switchFragment(mesFragment);
                return true;
            }
            return false;
        });

        // 找到按钮并设置点击监听器
        Button addButton = findViewById(R.id.button_addmissions);
        addButton.setOnClickListener(view -> showMenu(view));
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存当前显示的 Fragment
        getSupportFragmentManager().putFragment(outState, "activeFragment", activeFragment);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (activeFragment != null) {
            transaction.hide(activeFragment);
        }
        transaction.show(fragment);
        transaction.commit();
        activeFragment = fragment;
    }
    // 显示菜单的方法
    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        // 添加菜单项
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "新建任务");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "排序");

        // 设置菜单项点击监听器
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1:
                    // 处理新建任务的点击事件
                    Intent intent = new Intent(MainActivity.this, TaskItemDetailActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
                    return true;
                case 2:
                    // 处理排序的点击事件
                    sortTasks();
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == Activity.RESULT_OK && data != null) {
            // 调用 handleTaskResult 方法处理返回的数据
            if (missionsFragment != null) {
                missionsFragment.handleTaskResult(data);
            }
        }
    }

    // 排序任务的方法
    private void sortTasks() {
        // 实现排序任务的逻辑
    }

    // 这里的 REQUEST_CODE_ADD_TASK 是您启动 TaskItemDetailActivity 时使用的请求码
    private static final int REQUEST_CODE_ADD_TASK = 1;

}
//        ViewPager2 viewPager = findViewById(R.id.view_pager);
//        TabLayout tabLayout = findViewById(R.id.tab_layout);
//
//        FragmentAdapter pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
//        viewPager.setAdapter(pagerAdapter);
//
//        new TabLayoutMediator(tabLayout, viewPager,
//                (tab, position) -> tab.setText(tabHeaderStrings[position])
//            // 设置TabLayout的标题
//        ).attach();
//    }
//
//    private class FragmentAdapter extends FragmentStateAdapter {
//        private static final int NUM_TABS = 3;
//
//        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
//            super(fragmentManager, lifecycle);
//
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            switch (position) {
//                case 0:
//                    return new ShoppingListFragment();
//                case 1:
//                    return new TencentMapFragment();
//                case 2:
//                    return new WebViewFragment();
//                default:
//                    return null;
//            }
//        }
//
//        @NonNull
//        @Override
//        public int getItemCount() {
//            return NUM_TABS;
//        }
//    }
//}
    /*
    //将 shopItems 和 shopItemAdapter 定义为类的成员变量
    private ArrayList<ShopItem> shopItems = new ArrayList<>();
    private ShopItemAdapter shopItemAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycleview);

        //实验6RecyclerView

        RecyclerView mainRecyclerView = findViewById(R.id.recycler_view);// 创建布局管理器
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //定义一个Arraylist
        shopItems= new ArrayList<>();

        shopItems = new DataBank().LoadShopItems(MainActivity.this);//静态
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

                        new DataBank().SaveShopItems(MainActivity.this, shopItems);


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

                        new DataBank().SaveShopItems(MainActivity.this, shopItems);


                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED){
                    }
                }
        );

    }
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
                Intent intent = new Intent(MainActivity.this, BookitemDetailsActivity.class);
                addItemlauncher.launch(intent);
                break;
            case MENU_ITEM_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("确认删除");
                builder.setMessage("确定要删除吗？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击了确认按钮，执行删除操作
                        shopItems.remove(position);  // 从数据集中删除项
                        shopItemAdapter.notifyItemRemoved(position);  // 通知适配器项已删除
                        new DataBank().SaveShopItems(MainActivity.this,shopItems);
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
                Intent intentUpdate = new Intent(MainActivity.this, BookitemDetailsActivity.class);
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
                textViewName = shopItemView.findViewById(R.id.textView_item_name);
                textViewPrice = shopItemView.findViewById(R.id.textView_item_price);
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
    /*
}
        //实验6RecyclerView


//交换文字

//        Button button=findViewById(R.id.button_change);
//        button.setText(R.string.button_1);
//        button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Button clickedButton = (Button) view;
//                TextView text_hello = findViewById(R.id.text_View_1);
//                TextView text_jnu = findViewById(R.id.text_View_2);
//
//                CharSequence text1 = text_hello.getText();
//                CharSequence text2 = text_jnu.getText();
//                text_jnu.setText(text1);
//                text_hello.setText(text2);
//
//                Toast.makeText(MainActivity.this,"交换成功", Toast.LENGTH_SHORT).show();
//                showDialog(MainActivity.this, "", "交换成功啦！");
//                };
//            });
//        };
//        private void showDialog(MainActivity context, String title, String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(title)
//                .setMessage(message)
//                .setPositiveButton("我知道了", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        // 在确定按钮点击时执行的逻辑
//                    }
//                })
//
//                //不需要取消按钮，这里先注释掉了
////                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int which) {
////                        // 在取消按钮点击时执行的逻辑
////                    }
////                })
//                .show();

//交换文字

        //相对布局
        /*RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);  //设置布局中的控件置顶显示
        params.addRule(RelativeLayout.CENTER_HORIZONTAL); //设置布局中的控件居中显示
        params.leftMargin = 100;  // X轴位置，单位为像素
        params.topMargin = 900;   // Y轴位置，单位为像素

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); //设置imageview控件布局

        TextView textView = new TextView(this);                       //创建TextView控件
        ImageView imageView = new ImageView(this);                    //创建 ImageView 控件

        textView.setText(R.string.Hello_World);                     //设置TextView的文字内容

        textView.setTextColor(Color.GREEN);                                  //设置TextView的文字颜色
        textView.setTextSize(18);                                                //设置TextView的文字大小
        imageView.setImageResource(R.drawable.my_image);
        relativeLayout.addView(textView, params);                  //添加TextView对象和TextView的布局属性
        relativeLayout.addView(imageView,layoutParams);
        setContentView(relativeLayout);                                  //设置在Activity中显示RelativeLayout*/
        //相对布局
//    }
//}