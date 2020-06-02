package com.example.android.xo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {

    private String name1;
    private String name2;
    private EditText name1ET;
    private EditText name2ET;
    private Button doneBtn;
    private Intent homeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initiateViews();

        homeScreen = new Intent(Main2Activity.this,gameActivity.class);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1=name1ET.getText().toString();
                name2=name2ET.getText().toString();

                homeScreen.putExtra("name1",name1);
                homeScreen.putExtra("name2",name2);

                startActivity(homeScreen);
            }
        });
    }

    private void initiateViews() {
        name1ET=findViewById(R.id.name1);
        name2ET=findViewById(R.id.name2);
        doneBtn=findViewById(R.id.done);
    }
}
