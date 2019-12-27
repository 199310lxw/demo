package com.example.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.utils.SystemUtil;

public class ThemeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        SystemUtil.initSystemBarTint(this,getResources().getColor(R.color.default_color));//状态栏颜色
        SystemUtil.setAndroidNativeLightStatusBar(this,true);//状态栏字体颜色,true为黑色，false为白色
    }
}
