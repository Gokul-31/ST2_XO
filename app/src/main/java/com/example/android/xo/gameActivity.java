package com.example.android.xo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class gameActivity extends AppCompatActivity {

    //playing
    private String[] names = new String[2];
    private int winner=-1;
    //views
    private Button startBtn;
    private ImageView gridView;
    private ConstraintLayout all;
    private TextView turnsView;
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
    private Paint mPaintO=new Paint();
    private int xTouch;
    private int yTouch;
    private int[] lines = new int[4];
    private int index[] = new int[2];
    //resources
    private Drawable[] picsBack = new Drawable[2];
    //working
    private int pTurn;
    private int doneTotal=0;
    private Symbol[][] boxes = new Symbol[3][3];
    private Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get data from previous activity
        Intent homeScreenGet = getIntent();
        names[0] = homeScreenGet.getStringExtra("name1");
        names[1] = homeScreenGet.getStringExtra("name2");

        setValues();
        initiateView();
        setListeners();
        setPaint();

    }

    private void setValues() {

        vib=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        picsBack[0] = getDrawable(R.drawable.x_back);
        picsBack[1] = getDrawable(R.drawable.o_back);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3 ; j++){
                boxes[i][j]=new Symbol();
            }
        }
        //winValues
    }

    private void initiateView() {
        all = findViewById(R.id.all);
        startBtn = findViewById(R.id.start);
        gridView = findViewById(R.id.gridImg);
        turnsView = findViewById(R.id.turns);
    }

    private void setPaint() {
        mPaintGrid.setColor(getResources().getColor(R.color.black));
        mPaintGrid.setStrokeWidth(10);
        mPaintWhite.setColor(getResources().getColor(R.color.whiteColor));
        mPaintX.setColor(getResources().getColor(R.color.playerX));
        mPaintX.setStrokeWidth(15);
        mPaintO.setColor(getResources().getColor(R.color.playerO));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.removeView(startBtn);
                //start and set canvas
                allWidth = all.getWidth();
                needWidth = allWidth - 200;
                needHeight = needWidth;
                space = (needWidth / 3);
                for (int i = 0; i < 4; i++) {
                    lines[i] = i * space;
                }
                Log.i("main", "onClick: Line0"+lines[0]);
                Log.i("main", "onClick: Line1"+lines[1]);
                Log.i("main", "onClick: Line2"+lines[2]);
                Log.i("main", "onClick: Line3"+lines[3]);
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
                //set Background
                pTurn = 0;
                turnsView.setText(names[pTurn]);
                all.setBackground(picsBack[pTurn]);
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
                            if(!boxes[index[0]][index[1]].isMarked()){
                                boxes[index[0]][index[1]].setMarked(true);
                                boxes[index[0]][index[1]].setMark(pTurn);
                                drawSymbol(pTurn,index[0],index[1]);
                                doneTotal++;
                                if(!CheckFinish()) {
                                    changePlayer();
                                }
                                else{
                                    //after done
                                    Intent result = new Intent(getApplicationContext(),result.class);
                                    if(winner!=-1)
                                        result.putExtra("name",names[winner]);
                                    else
                                        result.putExtra("name","Tie");
                                    result.putExtra("win",winner);
                                    startActivity(result);
                                }
                            }
                        }
                }
                gridView.invalidate();
                return true;
            }
        });
    }

    private boolean CheckFinish() {
        if (doneTotal < 5) {
            return false;
        } else {
            //check and return
            if (checkCombo(pTurn)) {
                winner = pTurn;
                return true;
            } else if (doneTotal == 9) {
                winner = -1;
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean checkCombo (int pTurn){
        if(boxes[0][0].getMark()==pTurn){
            if(boxes[0][1].getMark()==pTurn&&boxes[0][2].getMark()==pTurn){
                return true;
            }
            else if(boxes[1][0].getMark()==pTurn&&boxes[2][0].getMark()==pTurn){
                return true;
            }
            else if(boxes[1][1].getMark()==pTurn&&boxes[2][2].getMark()==pTurn){
                return true;
            }
        }
        else if(boxes[1][1].getMark()==pTurn){
            if(boxes[1][0].getMark()==pTurn&&boxes[1][2].getMark()==pTurn){
                return true;
            }
            else if(boxes[0][1].getMark()==pTurn&&boxes[2][1].getMark()==pTurn){
                return true;
            }
            else if(boxes[0][2].getMark()==pTurn&&boxes[2][0].getMark()==pTurn)   {
                return true;
            }
        }
        else if(boxes[2][2].getMark()==pTurn){
            if(boxes[0][2].getMark()==pTurn&&boxes[1][2].getMark()==pTurn){
                return true;
            }
            else if(boxes[2][0].getMark()==pTurn&&boxes[2][1].getMark()==pTurn){
                return true;
            }
        }
            return false;
    }

    private void changePlayer() {
        pTurn=(pTurn==0)?1:0;
        turnsView.setText(names[pTurn]);
        all.setBackground(picsBack[pTurn]);
    }

    private void drawSymbol(int pTurn, int iX, int iY) {
        switch (pTurn) {
            case 0:
                //draw x:
                mCanvas.drawLine(lines[iX]+85,lines[iY]+85,lines[iX+1]-85,lines[iY+1]-85,mPaintX);
                mCanvas.drawLine(lines[iX+1]-85,lines[iY]+85,lines[iX]+85,lines[iY+1]-85,mPaintX);
                break;
            case 1:
                //draw o:
                mCanvas.drawCircle(lines[iX]+(float) (space/2),lines[iY]+(float) (space/2),(float)(space/2)-75,mPaintO);
                mCanvas.drawCircle(lines[iX]+(float) (space/2),lines[iY]+(float) (space/2),(float)(space/2)-95,mPaintWhite);
                break;
        }
    }


    private int[] getIndex(int xTouch, int yTouch) {
        int[] indexes = new int[2];
        indexes[0]=-1;
        indexes[1]=-1;
        for (int i = 3; i > 0; i--) {
            if (xTouch < lines[i] - 30 && xTouch > lines[i-1] + 30) {
                indexes[0] = (i-1);
            }
            if (yTouch < lines[i] - 30 && yTouch > lines[i-1] + 30) {
                indexes[1] = (i-1);
            }
        }
        return indexes;
    }
}
