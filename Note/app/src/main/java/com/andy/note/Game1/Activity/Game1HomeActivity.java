package com.andy.note.Game1.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.andy.note.MyApplication;
import com.andy.note.R;

/**
 * Created by andy on 2018/9/10.
 */

public class Game1HomeActivity extends AppCompatActivity {

    private final static String LOG_TAG = "Game1HomeActivity";

    private Button startBtn, restartBtn, soundBtn, highBtn;

    private boolean reStartBtnCanClick = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game1_home);

        initView();
    }

    private void initView() {
        startBtn = (Button) findViewById(R.id.game1_start_btn);
        restartBtn = (Button) findViewById(R.id.game1_restart_btn);
        soundBtn = (Button) findViewById(R.id.game1_sound_btn);
        highBtn = (Button) findViewById(R.id.game1_high_btn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Game1HomeActivity.this, Game1Activity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isRestart", false);
                intent.putExtras(bundle);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Game1HomeActivity.this, startBtn, "game1").toBundle());
            }
        });

        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.sound) {
                    soundBtn.setText("游戏音效:关");
                } else {
                    soundBtn.setText("游戏音效:开");
                }
                MyApplication.sound = !MyApplication.sound;
            }
        });

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reStartBtnCanClick) {
                    Intent intent = new Intent(Game1HomeActivity.this, Game1Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isRestart", true);
                    intent.putExtras(bundle);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Game1HomeActivity.this, startBtn, "game1").toBundle());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("game1", MODE_PRIVATE);
        if (highBtn != null) {
            highBtn.setText("最高分数:" + sharedPreferences.getInt("high", 0));
            MyApplication.game1HighScore = sharedPreferences.getInt("high", 0);
        }
        if (sharedPreferences.getBoolean("hasData", false)) {
            //如果有之前的游戏数据
            reStartBtnCanClick = true;
            restartBtn.setTextColor(Color.parseColor("#000000"));
        } else {
            reStartBtnCanClick = false;
            restartBtn.setTextColor(Color.parseColor("#dedede"));
        }
    }
}
