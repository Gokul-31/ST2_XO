package com.example.android.xo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;

public class game2Activity extends AppCompatActivity {

    private Button startBtn;
    private TextView turnsView;
    private ImageView gridView;
    private ConstraintLayout allView;
    //canvas
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int allWidth;
    private int needWidth;
    private int needHeight;
    private int space;
    private int margin = 35;
    private int marginLine = 5;
    private Paint mPaintGrid = new Paint();
    private Paint mPaintWhite = new Paint();
    private Paint mPaintX = new Paint();
    private Paint mPaintO = new Paint();
    private int xTouch;
    private int yTouch;
    private int[] lines = new int[4];
    private int[] index = new int[2];
    private int[][] winIndex = new int[8][3];
    //play
    private boolean done=false;
    private String name1;
    private int turnsP = 0;
    private int turnsC = 0;
    private int winner = -1;
    private int k;
    private int[] player = new int[5];
    private int[] comp = new int[4];
    private int[] rem = new int[6];
    private Symbol[][] boxes = new Symbol[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);


        Intent get = getIntent();
        name1 = get.getStringExtra("name1");

        setValues();
        initiate();
        setListeners();
        setPaint();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allView.removeView(startBtn);
                //getwidths;
                allWidth = allView.getWidth();
                needWidth = allWidth - 200;
                needHeight = needWidth;
                space = (needWidth / 3);
                for (int i = 0; i < 4; i++) {
                    lines[i] = i * space;
                }
                mBitmap = Bitmap.createBitmap(needWidth, needHeight, Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);
                gridView.setImageBitmap(mBitmap);
                mCanvas.drawRect(0, 0, needWidth, needHeight, mPaintWhite);
                mCanvas.drawLine(0, marginLine, needWidth, marginLine, mPaintGrid);
                mCanvas.drawLine(marginLine, 0, marginLine, needHeight, mPaintGrid);
                mCanvas.drawLine(needWidth - marginLine, 0, needWidth - marginLine, needHeight, mPaintGrid);
                mCanvas.drawLine(0, needHeight - marginLine, needWidth, needHeight - marginLine, mPaintGrid);

                //drawLines
                mCanvas.drawLine(space, margin, space, needHeight - margin, mPaintGrid);
                mCanvas.drawLine((2 * space), margin, (2 * space), needHeight - margin, mPaintGrid);
                mCanvas.drawLine(margin, space, needWidth - margin, space, mPaintGrid);
                mCanvas.drawLine(margin, (2 * space), needWidth - margin, (2 * space), mPaintGrid);

                turnsP = 0;
                turnsC = 0;
                //done drawing grid
            }
        });

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xTouch = (int) event.getX();
                yTouch = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        index = getIndex(xTouch, yTouch);
                        if (index[0] != -1 && index[1] != -1) {
                            Log.i("main", "onTouch: "+index[0]+" "+index[1]);
                            if (!boxes[index[0]][index[1]].isMarked()) {
                                boxes[index[0]][index[1]].setMarked(true);
                                boxes[index[0]][index[1]].setMark(0);
                                if(turnsP!=4) {
                                    player[turnsP] = boxes[index[0]][index[1]].getId();
                                }
                                turnsP++;
                                drawX(index[1], index[0]);
                                if(turnsP>=3)
                                    CheckFinish(boxes[index[0]][index[1]].getId());
                                if(turnsP<5&&!done)
                                    playO();
                            }
                        }
                }
                gridView.invalidate();
                return true;
            }
        });
    }

    private void CheckFinish(int m) {
        boolean flag = false;
        int[] checkSet = new int[3];
        for (int i = 0; i < turnsP; i++) {
            for (int j = 0; j <turnsP; j++) {
                if (i != j) {
                    checkSet[0] = player[i];
                    checkSet[1] = player[j];
                    checkSet[2] = m;
                    Arrays.sort(checkSet);
                    if (ifIn(checkSet,winIndex)) {
                        //x won
                        flag = true;
                        break;
                    }
                }
            }
        }

        if (flag) {
            //x won
            done=true;
            winner = 0;
            Log.i("MAin", "Came here "+ winner);
            goResult();
        }else if (turnsC + turnsP == 9) {
            winner = -1;
            goResult();
        }
    }

    private boolean ifIn(int[] checkSet, int[][] winIndex) {
        boolean flag=false;
        for(int i=0;i<8;i++){
            if(checkSet[0]==winIndex[i][0]&&checkSet[1]==winIndex[i][1]&&checkSet[2]==winIndex[i][2])
                flag=true;
        }
        return flag;
    }

    private void playO() {
        int dummy = 0;
        int i1;
        int i2;
        if (turnsC + turnsP < 3) {
            do {
                dummy = (int) (Math.floor(Math.random() * 9) + 1);
            }while(boxes[(dummy-1)/3][(dummy-1)%3].isMarked());
        } else {
            dummy = findBest();
        }
        i1 = (dummy - 1) / 3;
        i2 = (dummy - 1) % 3;
        comp[turnsC] = dummy;
        turnsC++;
        drawO(i1, i2);
        boxes[i1][i2].setMark(1);
        boxes[i1][i2].setMarked(true);
        //maybe wait
        if (winner == 1) {
            goResult();
        }
    }

    private int findBest() {
        int dummy;
        getRem();
        int[] checkSet = new int[3];
        for (int i = 0; i < comp.length; i++) {
            for (int j = 0; j < comp.length; j++) {
                if (i != j) {
                    for (int x = 0; x < k; x++) {
                        checkSet[0] = comp[i];
                        checkSet[1] = comp[j];
                        checkSet[2] = rem[x];
                        Arrays.sort(checkSet);
                        if (ifIn(checkSet,winIndex)) {
                            winner = 1;
                            return rem[x];   //got the 3rd for O
                        }
                    }
                }
            }
        }
        Log.i("MAin", "Came to no success ");
        for (int i = 0; i < turnsP; i++) {
            for (int j = 0; j < turnsP; j++) {
                if (i != j) {
                    for (int x = 0; x < k; x++) {
                        checkSet[0] = player[i];
                        checkSet[1] = player[j];
                        checkSet[2] = rem[x];
                        Arrays.sort(checkSet);
                        if (ifIn(checkSet,winIndex)) {
                            return rem[x];   //got the 3rd for O
                        }
                    }
                }
            }
        }
        Log.i("Main", "Came to where no come ");
        do{
            dummy=(int) Math.floor((Math.random() * 9) + 1);
        }while(boxes[(dummy-1)/3][(dummy-1)%3].isMarked());
        return dummy;
    }

    private void getRem() {
        k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!boxes[i][j].isMarked()) {
                    rem[k] = boxes[i][j].getId();
                    k++;
                }
            }
        }
    }

    private void goResult() {
        Intent result = new Intent(getApplicationContext(), result.class);
        result.putExtra("win", winner);
        Log.i("Main", "goResult: "+ winner);
        switch (winner) {
            case -1:
                result.putExtra("name", "Tie");
                break;
            case 0:
                result.putExtra("name", name1);
                break;
            case 1:
                result.putExtra("name", "Computer");
                break;
        }
        startActivity(result);
    }

    private void drawO(int i2, int i1) {
        mCanvas.drawCircle(lines[i1] + (float) (space / 2), lines[i2] + (float) (space / 2), (float) (space / 2) - 75, mPaintO);
        mCanvas.drawCircle(lines[i1] + (float) (space / 2), lines[i2] + (float) (space / 2), (float) (space / 2) - 95, mPaintWhite);
    }


    private void initiate() {
        winIndex[0] = new int[]{1, 2, 3};
        winIndex[1] = new int[]{4, 5, 6};
        winIndex[2] = new int[]{7, 8, 9};
        winIndex[3] = new int[]{1, 4, 7};
        winIndex[4] = new int[]{2, 5, 8};
        winIndex[5] = new int[]{3, 6, 9};
        winIndex[6] = new int[]{1, 5, 9};
        winIndex[7] = new int[]{3, 5, 7};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxes[i][j] = new Symbol();
                boxes[i][j].setId((i * 3) + j + 1);
            }
        }
        turnsView.setText(name1);
    }

    private void setValues() {
        startBtn = findViewById(R.id.start2);
        turnsView = findViewById(R.id.turns2);
        gridView = findViewById(R.id.gridImg2);
        allView = findViewById(R.id.all2);
    }

    private void setPaint() {
        mPaintGrid.setColor(getResources().getColor(R.color.black));
        mPaintGrid.setStrokeWidth(10);
        mPaintWhite.setColor(getResources().getColor(R.color.whiteColor));
        mPaintX.setColor(getResources().getColor(R.color.playerX));
        mPaintX.setStrokeWidth(15);
        mPaintO.setColor(getResources().getColor(R.color.playerO));
    }

    private int[] getIndex(int xTouch, int yTouch) {
        int[] indexes = new int[2];
        indexes[0] = -1;
        indexes[1] = -1;
        for (int i = 3; i > 0; i--) {
            if (xTouch < lines[i] - 30 && xTouch > lines[i - 1] + 30) {
                indexes[1] = (i - 1);
            }
            if (yTouch < lines[i] - 30 && yTouch > lines[i - 1] + 30) {
                indexes[0] = (i - 1);
            }
        }
        return indexes;
    }

    private void drawX(int iX, int iY) {
        mCanvas.drawLine(lines[iX] + 85, lines[iY] + 85, lines[iX + 1] - 85, lines[iY + 1] - 85, mPaintX);
        mCanvas.drawLine(lines[iX + 1] - 85, lines[iY] + 85, lines[iX] + 85, lines[iY + 1] - 85, mPaintX);
    }
}
