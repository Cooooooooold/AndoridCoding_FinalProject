package com.example.experiment2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.experiment2.BottomUI.MissionsFragment;

public class TaskItemDetailActivity extends AppCompatActivity {

    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskitem_details);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");

            if (name != null) {
                double points = intent.getDoubleExtra("points", 0);
                int quantity = intent.getIntExtra("quantity", 0);
                int position = intent.getIntExtra("position", -1);

                EditText editTextTaskName = findViewById(R.id.title_text_view);
                EditText editTextPoints = findViewById(R.id.achievement_points);
                EditText editTextQuantity = findViewById(R.id.number_of_task);

                editTextTaskName.setText(name);
                editTextPoints.setText(String.valueOf(points));
                editTextQuantity.setText(String.valueOf(quantity));
        }

        Button buttonOk = findViewById(R.id.button_task_items);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent resultIntent = new Intent();
                    EditText editTextTaskName = findViewById(R.id.title_text_view);
                    EditText editTextPoints = findViewById(R.id.achievement_points);
                    EditText editTextQuantity = findViewById(R.id.number_of_task);

                    String names = editTextTaskName.getText().toString();
                    double points = Double.parseDouble(editTextPoints.getText().toString());
                    int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                    resultIntent.putExtra("name", names);
                    resultIntent.putExtra("points", points);
                    resultIntent.putExtra("quantity", quantity);
                    resultIntent.putExtra("position", position);

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
            }
        });
    }
}
}
