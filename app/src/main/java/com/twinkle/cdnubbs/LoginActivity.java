package com.twinkle.cdnubbs;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.twinkle.cdnubbs.java.content.BaseActivity;
import com.twinkle.cdnubbs.java.content.LoadDialog;
import com.twinkle.cdnubbs.java.utils.Util;
import com.twinkle.cdnubbs.user.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private LoadDialog load;
    private TextView tvw_forgetpass, tvw_register;
    private EditText ett_admin, ett_pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        findView();
        initView();
    }


    public int getLayoutId() {
        return R.layout.activity_login;
    }


    public void findView() {
        check_admin();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏显示
        btn_login = (Button) findViewById(R.id.btn_login);
        tvw_forgetpass = (TextView) findViewById(R.id.tvw_login_forget);
        tvw_register = (TextView) findViewById(R.id.tvw_login_register);
        ett_admin = (EditText) findViewById(R.id.ett_login_admin);
        ett_pass = (EditText) findViewById(R.id.ett_login_pass);
    }


    public void initView() {
        btn_login.setOnClickListener(this);
        tvw_register.setOnClickListener(this);
        tvw_forgetpass.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                check_text();
                break;
            case R.id.tvw_login_register:
                check(1);
                break;
            case R.id.tvw_login_forget:
                check(2);
                break;
            default:
                break;
        }
    }

    private void check(int flag) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        if (flag == 1) {
            intent.putExtra("flag", 1);
        } else if (flag == 2) {
            intent.putExtra("flag", 2);
        }
        startActivity(intent);

    }

    private void check_text() {
        String admin = ett_admin.getText().toString().trim();
        String pass = ett_pass.getText().toString().trim();

        if (TextUtils.isEmpty(admin)) {
            ett_admin.setError(getString(R.string.tip_input));
            ett_admin.setFocusable(true);

        } else if (TextUtils.isEmpty(pass)) {
            ett_pass.setError(getString(R.string.tip_input));
            ett_pass.setFocusable(true);
        } else {
            to_login(admin, pass);
        }
    }

    private void to_login(String admin, String pass) {
        load = new LoadDialog(LoginActivity.this);
        load.show();
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(admin);
        bmobUser.setPassword(pass);
        bmobUser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {

                if (e == null) {

                    Util.toast(LoginActivity.this, getString(R.string.login_sucess));
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                    load.diss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {
                    Util.toast(LoginActivity.this, e.toString());
                    Log.i("error", e.toString());
                    load.diss();
                }
            }
        });
    }


    private void check_admin() {
        User userInfo = Util.getUser();
        if (userInfo != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

}
