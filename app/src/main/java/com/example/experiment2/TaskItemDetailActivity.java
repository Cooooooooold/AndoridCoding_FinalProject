package com.example.experiment2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TaskItemDetailActivity extends AppCompatActivity {

    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskitem_details);

        Intent intent = getIntent();
        if (null != intent) {
            String name = intent.getStringExtra("name");

            if (null != name) {
                double points = intent.getDoubleExtra("points", 0);
                int quantity = intent.getIntExtra("quantity", 0);
                String category = intent.getStringExtra("category");
                position = intent.getIntExtra("position", -1);

                EditText editTextTaskName = findViewById(R.id.title_text_view);
                EditText editTextPoints = findViewById(R.id.achievement_points);
                EditText editTextQuantity = findViewById(R.id.number_of_task);
                EditText editTextCategory = findViewById(R.id.classify_tasks);

                editTextTaskName.setText(name);
                editTextPoints.setText(String.valueOf(points));
                editTextQuantity.setText(String.valueOf(quantity));
                editTextCategory.setText(category);
            }
        }

        Button buttonOk = findViewById(R.id.button_task_items);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    EditText editTextTaskName = findViewById(R.id.title_text_view);
                    EditText editTextPoints = findViewById(R.id.achievement_points);
                    EditText editTextQuantity = findViewById(R.id.number_of_task);
                    EditText editTextCategory = findViewById(R.id.classify_tasks);

                    // 输入验证
                    if (editTextTaskName.getText().toString().isEmpty() ||
                            editTextPoints.getText().toString().isEmpty() ||
                            editTextQuantity.getText().toString().isEmpty() ||
                            editTextCategory.getText().toString().isEmpty()) {
                        // 显示错误消息
                        return;
                    }

                    double points = Double.parseDouble(editTextPoints.getText().toString());
                    int quantity = Integer.parseInt(editTextQuantity.getText().toString());

                    intent.putExtra("name", editTextTaskName.getText().toString());
                    intent.putExtra("points", points);
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("category", editTextCategory.getText().toString());
                    intent.putExtra("position", position);

                    setResult(Activity.RESULT_OK, intent);
                    TaskItemDetailActivity.this.finish();
                } catch (NumberFormatException e) {
                    // 处理输入格式错误
                }
            }
        });

    }
}
