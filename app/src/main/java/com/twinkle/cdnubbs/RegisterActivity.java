package com.twinkle.cdnubbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout lly1, lly2, lly3;
    private Toolbar tbr_register;
    private TextView tvw_register_title,tvw_register_phone;
    private EditText ett_register_number,ett_register_code,ett_register_pass1,ett_register_pass2;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        apply();
    }

    private void init() {
        tbr_register = (Toolbar)findViewById(R.id.tbr_register);
        lly1 = (LinearLayout) findViewById(R.id.llt_register1);
        lly2 = (LinearLayout) findViewById(R.id.llt_register2);
        lly3 = (LinearLayout) findViewById(R.id.llt_register3);
        tvw_register_phone = (TextView)findViewById(R.id.tvw_register_phone);
        tvw_register_title = (TextView)findViewById(R.id.tvw_register_title);
        ett_register_number = (EditText)findViewById(R.id.ett_register_number);
        ett_register_code = (EditText)findViewById(R.id.ett_register_code);
        ett_register_pass1 = (EditText)findViewById(R.id.ett_register_pass1);
        ett_register_pass2 = (EditText)findViewById(R.id.ett_register_pass2);

    }

    private void apply() {
        Intent intent = RegisterActivity.this.getIntent();
        flag = intent.getIntExtra("flag",0);
        if(flag==1){
            tbr_register.setTitle(getString(R.string.register));
        }
        else  if(flag==2){
            tbr_register.setTitle(getString(R.string.forget_pass));
        }

    }
}
