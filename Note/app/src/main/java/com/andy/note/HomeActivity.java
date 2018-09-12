package com.andy.note;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.andy.note.Game1.Activity.Game1HomeActivity;

/**
 * Created by andy on 2018/9/10.
 */

public class HomeActivity extends AppCompatActivity {

    private ImageButton game1ImgBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView() {
        game1ImgBtn = (ImageButton) findViewById(R.id.home_game1_imgbtn);
        game1ImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,Game1HomeActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, game1ImgBtn, "game1").toBundle());
            }
        });
    }
}
