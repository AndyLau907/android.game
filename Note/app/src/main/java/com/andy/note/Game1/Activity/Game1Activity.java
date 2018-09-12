package com.andy.note.Game1.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.note.Game1.View.Game1ViewGroup;
import com.andy.note.MyApplication;
import com.andy.note.R;

/**
 * Created by andy on 2018/8/21.
 */

public class Game1Activity extends AppCompatActivity implements Game1ViewGroup.ITextMargin, Game1ViewGroup.IGameControl {
    private Game1ViewGroup viewGroup;

    private TextView scoreText, rangeText;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        initViews();
    }

    private void initViews() {
        viewGroup = (Game1ViewGroup) findViewById(R.id.gameviewgroup);
        scoreText = (TextView) findViewById(R.id.score_tv);
        rangeText = (TextView) findViewById(R.id.range_tv);

        viewGroup.setITextMargin(this);
        viewGroup.setIGameControl(this);
    }

    @Override
    public void setTextMargin(int margin) {
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) scoreText.getLayoutParams();
        lp1.leftMargin = margin;
        scoreText.setLayoutParams(lp1);

        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) rangeText.getLayoutParams();
        lp2.rightMargin = margin;
        rangeText.setLayoutParams(lp2);
    }

    @Override
    public void scoreChanged(int score) {
        scoreText.setText("分数:" + score);
        if (MyApplication.sound) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.game1_sound);
            }
            mediaPlayer.start();
        }
    }

    @Override
    public void gameOver() {
        MyApplication.needSave = false;
        Intent intent = new Intent(this, Game1OverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("score",viewGroup.getScore());
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @Override
    public void rangeChanged(int range) {
        rangeText.setText("难度:" + range);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyApplication.needSave) {
            saveGameImfo();
        }else{
            SharedPreferences sharedPreferences =getSharedPreferences("game1",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasData",false);
            if(viewGroup.getScore()>MyApplication.game1HighScore){
                editor.putInt("high",viewGroup.getScore());
            }
            editor.apply();
        }
    }

    private void saveGameImfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("game1", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int high = sharedPreferences.getInt("high", 0);
        if (high < viewGroup.getScore()) {
            editor.putInt("high", viewGroup.getScore());
        }
        editor.putInt("score", viewGroup.getScore());
        editor.putString("road", getStr(viewGroup.getRoad()));
        editor.putString("gameRes", getStr(viewGroup.getGameRes()));
        editor.putInt("time", viewGroup.getTime());
        editor.putInt("range", viewGroup.getRange());
        editor.putInt("deleteCount", viewGroup.getDeleteCount());
        editor.putBoolean("hasData", true);
        editor.apply();

    }


    private String getStr(int[][] src) {
        String str = "";
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                str += src[i][j] + "-";
            }
        }
        return str;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MyApplication.needSave) {
            saveGameImfo();
        }else{
            //如果游戏正常结束 则不需要保存本局游戏数据
            SharedPreferences sharedPreferences =getSharedPreferences("game1",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasData",false);
            if(viewGroup.getScore()>MyApplication.game1HighScore){
                editor.putInt("high",viewGroup.getScore());
            }
            editor.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.needSave = true;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle.getBoolean("isRestart")) {
            //复现上局游戏保存数据
            reStart();
        }
    }

    private void reStart() {
        SharedPreferences sharedPreferences = getSharedPreferences("game1", MODE_PRIVATE);
        int gameRes[][] = getInt(sharedPreferences.getString("gameRes", ""));
        int road[][] = getInt(sharedPreferences.getString("road", ""));
        int score = sharedPreferences.getInt("score", 0);
        int deleteCount = sharedPreferences.getInt("deleteCount", 0);
        int time = sharedPreferences.getInt("time", 100);
        int range = sharedPreferences.getInt("range", 1);

        viewGroup.setTime(time);
        viewGroup.setDeleteCount(deleteCount);
        viewGroup.setGameRes(gameRes);
        viewGroup.setRange(range);
        viewGroup.setRoad(road);
        viewGroup.setScore(score);
        viewGroup.setViewsImage();

        scoreText.setText("分数:" + score);
        rangeText.setText("难度:" + range);

    }

    private int[][] getInt(String str) {
        int temp[][] = new int[14][14];
        String tempStr[] = str.split("-");
        int k = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++, k++) {
                temp[i][j] = Integer.valueOf(tempStr[k]);
            }
        }
        return temp;
    }
}
