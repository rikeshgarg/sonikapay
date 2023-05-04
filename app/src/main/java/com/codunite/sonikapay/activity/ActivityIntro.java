package com.codunite.sonikapay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.codunite.commonutility.GlobalData;
import com.codunite.sonikapay.ActivityMain;
import com.codunite.sonikapay.R;

public class ActivityIntro extends AppCompatActivity {
    LinearLayout ll_get_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalData.Fullscreen(ActivityIntro.this);
        setContentView(R.layout.act_intro);
        ll_get_start=findViewById(R.id.ll_get_start);
        ll_get_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityIntro.this, ActivityLogin.class);
                startActivity(intent);
            }
        });

    }
}