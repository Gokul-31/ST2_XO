package com.example.android.xo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main1Activity extends AppCompatActivity {

    private String name1;
    private EditText n1;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        n1=findViewById(R.id.name11);
        doneBtn=findViewById(R.id.done1);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1=n1.getText().toString();
                Intent Players1=new Intent(getApplicationContext(),game2Activity.class);
                Players1.putExtra("name1",name1);

                startActivity(Players1);
            }
        });
    }
}
