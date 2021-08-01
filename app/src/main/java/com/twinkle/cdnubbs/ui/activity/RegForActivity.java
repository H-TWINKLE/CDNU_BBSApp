package com.twinkle.cdnubbs.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.utils.Init;
import com.twinkle.cdnubbs.entity.User;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegForActivity extends BaseActivity {

    @ViewInject(value = R.id.llt_register1)
    private LinearLayout lly1;

    @ViewInject(value = R.id.llt_register2)
    private LinearLayout lly2;

    @ViewInject(value = R.id.llt_register3)
    private LinearLayout lly3;

    @ViewInject(value = R.id.tbr_register)
    private Toolbar tbr_register;

    @ViewInject(value = R.id.tvw_register_title)
    private TextView tvw_register_title;

    @ViewInject(value = R.id.tvw_register_phone)
    private TextView tvw_register_phone;

    @ViewInject(value = R.id.ett_register_number)
    private EditText ett_register_number;

    @ViewInject(value = R.id.ett_register_code)
    private EditText ett_register_code;

    @ViewInject(value = R.id.ett_register_pass1)
    private EditText ett_register_pass1;

    @ViewInject(value = R.id.ett_register_pass2)
    private EditText ett_register_pass2;

    private int flag = 0;

    private Boolean issend = false;

    @ViewInject(value = R.id.btn_register_code)
    private Button btn_register_code;

    @ViewInject(value = R.id.btn_register_check)
    private Button btn_register_check;

    private String object_id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reg_for;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);

    }

    @Override
    public void initData() {

        Intent intent = RegForActivity.this.getIntent();
        flag = intent.getIntExtra("flag", 0);
        if (flag == 1) {
            tbr_register.setTitle(getString(R.string.register));
            lly2.setVisibility(View.GONE);
            lly3.setVisibility(View.GONE);
        } else if (flag == 2) {
            tbr_register.setTitle(getString(R.string.forget_pass));
            lly2.setVisibility(View.GONE);
            lly3.setVisibility(View.GONE);
            btn_register_code.setVisibility(View.GONE);

        }

        tbr_register.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Event(value = R.id.btn_register_check)
    private void onBtClick(View view) {
        if (flag == 1 || flag == 3) {
            btn_register();
        } else if (flag == 2 || flag == 4) {
            btn_forgetpass();
        }
    }

    @Event(value = R.id.btn_register_code)
    private void onCodeClick(View view) {
        SMSSDK.getVerificationCode("86", ett_register_number.getText().toString().trim());
    }

    private void btn_register() {
        if (!issend) {
            String tel = ett_register_number.getText().toString().trim();
            if (TextUtils.isEmpty(tel) || tel.length() != 11) {
                toast(getString(R.string.phone_11));
            } else {
                check_num_is_alive(tel);
            }
        } else {
            if (flag == 3) {
                check_pass1andpass2();
            } else {
                SMSSDK.submitVerificationCode("86", tvw_register_phone.getText().toString(), ett_register_code.getText().toString().trim());
            }
        }

    }

    private void btn_forgetpass() {
        if (!issend) {
            String tel = ett_register_number.getText().toString().trim();
            Log.i("pass", tel);
            if (TextUtils.isEmpty(tel) || tel.length() != 11) {
                toast(getString(R.string.phone_11));
            } else {
                check_num_is_register(tel);
            }
        } else {
            if (flag == 4) {
                update_pass();
            } else {
                SMSSDK.submitVerificationCode("86", tvw_register_phone.getText().toString(), ett_register_code.getText().toString().trim());
            }
        }
    }

    private void check_num_is_register(final String tel) {
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", tel);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 0) {
                        ett_register_number.setError(getString(R.string.phone_not_alive));
                    } else {
                        object_id = object.get(0).getObjectId();
                        SMSSDK.getVerificationCode("86", tel);
                        btn_register_code.setVisibility(View.VISIBLE);
                    }
                } else {
                    toast(getString(R.string.internet_is_gone));
                }
            }
        });

    }

    private void check_num_is_alive(final String tel) {
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", tel);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 0) {
                        SMSSDK.getVerificationCode("86", tel);
                        btn_register_code.setVisibility(View.VISIBLE);
                    } else {
                        ett_register_number.setError(getString(R.string.phone_is_alive));
                    }

                } else {
                    toast(getString(R.string.internet_is_gone));
                }
            }
        });

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
                            //  Util.toast(RegForActivity.this, getString(R.string.register_success));
                            lly1.setVisibility(View.GONE);
                            lly2.setVisibility(View.GONE);
                            lly3.setVisibility(View.VISIBLE);
                            tvw_register_title.setText(getString(R.string.pass_input));
                            if (flag == 1) {
                                btn_register_check.setText(getString(R.string.register));
                                flag = 3;
                            } else if (flag == 2) {
                                btn_register_check.setText(getString(R.string.pass_revise));
                                flag = 4;
                            }

                        }
                    });

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功
                    toast(getString(R.string.success_send_code));
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
                    toast(
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
        @SuppressLint("SetTextI18n")
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

    private void update_pass() {
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

            User user = new User();
            user.setPass(pass2);
            user.setPassword(pass2);
            user.setMobilePhoneNumber(phone);
            user.setMobilePhoneNumberVerified(true);
            user.update(object_id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        toast(getString(R.string.pass_revise_succcess));
                        finish();
                    } else {
                        toast(e.toString());
                    }
                }
            });
        }
    }


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

            User user = new User();
            user.setUsername(tvw_register_phone.getText().toString());
            user.setName(Init.new_admin + phone.substring(phone.length() - 6));
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
                        toast(getString(R.string.register_success));
                        finish();
                    } else {
                        toast(e.toString());
                    }
                }
            });
        }
    }

    private void toast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegForActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
