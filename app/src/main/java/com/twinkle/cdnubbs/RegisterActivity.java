package com.twinkle.cdnubbs;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.cdnubbs.java.Init;
import com.twinkle.cdnubbs.java.Util;
import com.twinkle.cdnubbs.user.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lly1, lly2, lly3;
    private Toolbar tbr_register;
    private TextView tvw_register_title, tvw_register_phone;
    private EditText ett_register_number, ett_register_code, ett_register_pass1, ett_register_pass2;
    private int flag = 0;
    private Boolean issend = false;
    private Button btn_register_code, btn_register_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        apply();
    }

    private void init() {
        tbr_register = (Toolbar) findViewById(R.id.tbr_register);
        lly1 = (LinearLayout) findViewById(R.id.llt_register1);
        lly2 = (LinearLayout) findViewById(R.id.llt_register2);
        lly3 = (LinearLayout) findViewById(R.id.llt_register3);
        tvw_register_phone = (TextView) findViewById(R.id.tvw_register_phone);
        tvw_register_title = (TextView) findViewById(R.id.tvw_register_title);
        ett_register_number = (EditText) findViewById(R.id.ett_register_number);
        ett_register_code = (EditText) findViewById(R.id.ett_register_code);
        ett_register_pass1 = (EditText) findViewById(R.id.ett_register_pass1);
        ett_register_pass2 = (EditText) findViewById(R.id.ett_register_pass2);
        btn_register_code = (Button) findViewById(R.id.btn_register_code);
        btn_register_check = (Button) findViewById(R.id.btn_register_check);

    }

    private void apply() {

        //toobar title
        Intent intent = RegisterActivity.this.getIntent();
        flag = intent.getIntExtra("flag", 0);
        if (flag == 1) {
            tbr_register.setTitle(getString(R.string.register));
            lly2.setVisibility(View.GONE);
            lly3.setVisibility(View.GONE);
        } else if (flag == 2) {
            tbr_register.setTitle(getString(R.string.forget_pass));
            lly1.setVisibility(View.GONE);
            lly3.setVisibility(View.GONE);
            btn_register_code.setVisibility(View.GONE);

        }

        //toolbar finsh
        tbr_register.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //button listener
        btn_register_check.setOnClickListener(this);
        btn_register_code.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register_check:
                btn_register();
                break;
            case R.id.btn_register_code:
                SMSSDK.getVerificationCode("86", ett_register_number.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    private void btn_register() {
        if (!issend) {
            String tel = ett_register_number.getText().toString().trim();
            if (TextUtils.isEmpty(tel) || tel.length() != 11) {
                Util.toast(RegisterActivity.this, getString(R.string.phone_11));
            } else {
                SMSSDK.getVerificationCode("86", tel);
                btn_register_code.setVisibility(View.VISIBLE);
            }
        } else {
            if (flag == 3) {
                check_pass1andpass2();
            } else {
                SMSSDK.submitVerificationCode("86", tvw_register_phone.getText().toString(), ett_register_code.getText().toString().trim());
            }
        }

    }

    private EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                Log.i("reslut", result + "   " + data);
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    ct.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.toast(RegisterActivity.this, getString(R.string.register_success));
                            lly1.setVisibility(View.GONE);
                            lly2.setVisibility(View.GONE);
                            lly3.setVisibility(View.VISIBLE);
                            tvw_register_title.setText(getString(R.string.pass_input));
                            btn_register_check.setText(getString(R.string.register));
                            flag = 3;
                        }
                    });

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功
                    Util.toast(RegisterActivity.this, getString(R.string.success_send_code));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lly2.setVisibility(View.VISIBLE);
                            lly1.setVisibility(View.GONE);
                            btn_register_code.setVisibility(View.VISIBLE);
                            tvw_register_title.setText(getString(R.string.send_to_phone));
                            tvw_register_phone.setText(ett_register_number.getText().toString());
                            btn_register_check.setText(getString(R.string.check));
                            ett_register_code.setHint(getString(R.string.code_input));
                            issend = true;
                            ct.start();
                        }
                    });

                }
            } else {//错误等在这里（包括验证失败）
                ((Throwable) data).printStackTrace();
                try {
                    // data = "["+data.toString()+"]";
                    Util.toast(RegisterActivity.this,
                            JSONObject.parseObject(data.toString().
                                    replace("java.lang.Throwable: ", "")).get("detail").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);// 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
    }

    @Override
    protected void onStop() {
        super.onStop();
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eh);// 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
    }

    @Override
    protected void onStart() {
        super.onStart();
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）

    }

    private CountDownTimer ct = new CountDownTimer(1000 * 60, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_register_code.setEnabled(false);
            btn_register_code.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            btn_register_code.setEnabled(true);
            btn_register_code.setText(getString(R.string.send_code));
        }
    };

    private void check_pass1andpass2() {
        String pass1 = ett_register_pass1.getText().toString().trim();
        String pass2 = ett_register_pass2.getText().toString().trim();
        String phone = tvw_register_phone.getText().toString();
        if (TextUtils.isEmpty(pass1)) {
            ett_register_pass1.setError(getString(R.string.tip_input));
            ett_register_pass1.setFocusable(true);
        } else if (TextUtils.isEmpty(pass2)) {
            ett_register_pass2.setError(getString(R.string.tip_input));
            ett_register_pass2.setFocusable(true);
        } else if (!pass1.equals(pass2)) {
            ett_register_pass1.setError(getString(R.string.pass1pass2));
        } else {
            Bmob.initialize(RegisterActivity.this, "a0084a6493c91624bb96851013e23a28");
            User user = new User();
            user.setUsername(tvw_register_phone.getText().toString());
            user.setName("新用户" + phone.substring(phone.length() - 6));
            user.setLevel(0);
            user.setPass(pass2);
            user.setPassword(pass2);
            user.setTime(Init.now_time);
            user.setMobilePhoneNumber(phone);
            user.setMobilePhoneNumberVerified(true);
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        Util.toast(RegisterActivity.this, getString(R.string.register_success));
                        finish();
                    } else {
                        Util.toast(RegisterActivity.this, e.toString());
                    }
                }
            });
        }
    }
}
