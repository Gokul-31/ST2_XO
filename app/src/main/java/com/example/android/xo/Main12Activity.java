package com.example.android.xo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main12Activity extends AppCompatActivity {
    
    private Button b1;
    private Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12);
        
        b1=findViewById(R.id.p1);
        b2=findViewById(R.id.p2);
        
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p1=new Intent(getApplicationContext(),Main1Activity.class);
                startActivity(p1);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p2=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(p2);
            }
        });
    }
}
