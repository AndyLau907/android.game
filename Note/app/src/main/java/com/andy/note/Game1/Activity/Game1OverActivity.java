package com.andy.note.Game1.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.andy.note.MyApplication;
import com.andy.note.R;

/**
 * Created by andy on 2018/9/12.
 */

public class Game1OverActivity extends AppCompatActivity {

    private TextView scoreText, highText, newHighText;
    private Button restartBtn, backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.game1_gameover);

        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        getWindow().setEnterTransition(fade);
        getWindow().setReturnTransition(explode);
        getWindow().setReenterTransition(fade);

        initView();
    }

    private void initView() {
        scoreText = (TextView) findViewById(R.id.game1_gameover_score_tv);
        highText = (TextView) findViewById(R.id.game1_gameover_high_tv);
        newHighText = (TextView) findViewById(R.id.game1_gameover_newhigh_tv);
        backBtn = (Button) findViewById(R.id.game1_gameover_back_btn);
        restartBtn = (Button) findViewById(R.id.game1_gameover_restart_btn);

        int score = getIntent().getExtras().getInt("score");
        scoreText.setText("你的分数:" + score);
        highText.setText("最高纪录:" + MyApplication.game1HighScore + "");
        float scoref = (float) score;
        float highf = (float) MyApplication.game1HighScore;
        float temp = scoref / highf;
        if (temp <= 0.25f) {
            newHighText.setText("你是猪吗这么菜!");
        } else if (temp > 0.25 && temp <= 0.5) {
            newHighText.setText("离记录还差得远哦");
        } else if (temp > 0.5 && temp <= 0.75) {
            newHighText.setText("好气啊，还差一些分");
        } else if (temp > 0.75 && temp <= 1) {
            newHighText.setText("嗨呀这是最气的，就差一丢丢哦");
        } else {
            newHighText.setText("新纪录!大佬嚯冰阔落");
        }

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Game1OverActivity.this, Game1Activity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isRestart", false);
                intent.putExtras(bundle);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Game1OverActivity.this, restartBtn, "game1").toBundle());
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
