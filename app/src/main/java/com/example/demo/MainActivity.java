package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_theme;
    private Button btn_customize_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        btn_theme = findViewById(R.id.btn_theme);
        btn_customize_recyclerview=findViewById(R.id.btn_customize_recyclerview);
    }

    private void initEvent() {
        btn_theme.setOnClickListener(this);
        btn_customize_recyclerview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_theme:
                Intent in_one= new Intent(MainActivity.this, ThemeActivity.class);
                startActivity(in_one);
                break;
            case R.id.btn_customize_recyclerview:
                Intent in_two = new Intent(MainActivity.this, CustomizeRecyclerActivity.class);
                startActivity(in_two);
                break;
        }
    }
}
