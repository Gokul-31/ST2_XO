package com.example.android.xo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Leader extends AppCompatActivity {

    private Intent getResultIntent;
    private String winName;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Rank> winners;
    //create arrays


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        getResultIntent=getIntent();

        workList();
        buildRecycle();

    }

    private void workList() {
        boolean flag=false;
        int m;
        Rank mtemp=new Rank("",0);
        //load
        SharedPreferences sPrefGet=getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gsonGet=new Gson();
        String jsonGet= sPrefGet.getString("Rank List",null);
        Type type=new TypeToken<ArrayList<Rank>>(){}.getType();
        winners=gsonGet.fromJson(jsonGet,type);

        if(winners==null){
            winners=new ArrayList<Rank>();
        }
        //receeive data and add
        winName=getResultIntent.getStringExtra("Name");
        assert winName != null;
        if(!winName.equals("Tie")){
            for(int i=0;i<winners.size();i++){
                if(winners.get(i).getName().equals(winName)){
                    winners.get(i).incR();
                    m=i;
                    while(m>=0){
                        if(winners.get(m).getR()>winners.get(m-1).getR()){
                         mtemp.setR(winners.get(m).getR());
                         mtemp.setName(winners.get(m).getName());
                         winners.remove(m);
                         winners.add(m-1,new Rank(mtemp.getName(),mtemp.getR()));
                         m--;
                        }
                        else
                            break;
                    }
                    flag=true;
                }
            }
            if(!flag){
                winners.add(new Rank(winName,1));
            }
        }

        //sort and save
        SharedPreferences sPref=getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sPref.edit();
        Gson gson=new Gson();
        String json=gson.toJson(winners);
        editor.putString("Rank List",json);
        editor.apply();
    }

    private void buildRecycle() {
        mRecycleView=findViewById(R.id.recycle);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mAdapter=new ExampleAdapter(winners);

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mAdapter);
    }

    public void restart(View view) {
        //save the data and go to main12activity
        Intent restart=new Intent(getApplicationContext(),Main12Activity.class);
        startActivity(restart);
    }
}

