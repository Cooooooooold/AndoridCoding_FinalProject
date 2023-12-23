package com.example.experiment2.BottomUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.experiment2.R;


public class MesFragment extends Fragment {
    private ImageView imageViewProfile;
    private EditText editTextUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mes, container, false);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        Button buttonSourceCode = view.findViewById(R.id.buttonSourceCode);
        Button buttonContactAuthor = view.findViewById(R.id.buttonContactAuthor);

        setupImageViewProfileClickListener();
        setupEditTextUsernameTextWatcher();

            buttonSourceCode.setOnClickListener(v -> {
                // TODO: 实现跳转逻辑
                // 示例：打开浏览器访问网站
                openWebPage("https://github.com/Cooooooooold/AndoridCoding_FinalProject");
            });


            buttonContactAuthor.setOnClickListener(v -> {
                // TODO: 实现联系作者逻辑
                showContactDialog();
            });

        return view;
    }
    private void showContactDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("联系作者（2021153018高鹏）");
        builder.setMessage("Email: 617864963@qq.com\n微信/QQ:617864963");
        builder.setPositiveButton("关闭", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void setupImageViewProfileClickListener() {
        imageViewProfile.setOnClickListener(v -> {
            // TODO: 实现头像更换逻辑
            // 示例：打开图库选择图片
            openImageSelector();
        });
    }

    private void showEditUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("编辑用户名");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("保存", (dialog, which) -> saveUsername(input.getText().toString()));
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void setupEditTextUsernameTextWatcher() {
        editTextUsername.setOnClickListener(v -> {
            showEditUsernameDialog();
        });
    }


    private static final int PICK_IMAGE_REQUEST = 1; // 请求码

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.getData() == null) {
                return;
            }

            Uri imageUri = data.getData();
            // 使用图片的 URI
            imageViewProfile.setImageURI(imageUri);

            // 进一步的处理，例如上传图片到服务器或保存在本地
        }
    }
    private void saveUsername(String username) {
        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 保存用户名
        editor.putString("username", username);
        editor.apply(); // 使用 apply() 而不是 commit()，因为 apply() 是异步的

        // 更新 EditText 显示的用户名
        editTextUsername.setText(username);
    }

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void sendEmail(String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        startActivity(intent);
    }
}