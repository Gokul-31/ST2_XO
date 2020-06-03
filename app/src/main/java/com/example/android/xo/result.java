package com.example.android.xo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class result extends AppCompatActivity {

    private TextView resultView;
    private TextView aboveResultView;
    private String name;
    private int winner;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultView=findViewById(R.id.result);
        aboveResultView=findViewById(R.id.aboveResult);

        Intent getResult=getIntent();
        name=getResult.getStringExtra("name");
        winner=getResult.getIntExtra("win",-1);
        if(winner==-1){
            aboveResultView.setText("It is a ");
        }
        resultView.setText(name);

        final MediaPlayer xMedia = MediaPlayer.create(this,R.raw.x_tada);
        final MediaPlayer oMedia = MediaPlayer.create(this,R.raw.o_prize);
        final MediaPlayer tMedia = MediaPlayer.create(this,R.raw.t_coc);

        switch (winner){
            case -1:
                tMedia.start();
                break;
            case 0:
                xMedia.start();
                break;
            case 1:
                oMedia.start();
                break;
        }
    }

    public void goLeader(View view) {
        Intent goLeaderIntent=new Intent(getApplicationContext(),Leader.class);
        goLeaderIntent.putExtra("Name",name);
        startActivity(goLeaderIntent);
    }

    public void reset(View view) {
        Intent reset=new Intent(getApplicationContext(),Main12Activity.class);
        startActivity(reset);
    }
}
