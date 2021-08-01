package com.twinkle.cdnubbs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.content.LoadDialog;
import com.twinkle.cdnubbs.utils.Util;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.bmob.v3.BmobUser;

public class LoginActivity extends BaseActivity implements BmobProxy.toLoginListener {


    @ViewInject(value = R.id.btn_login)
    private Button btn_login;

    @ViewInject(value = R.id.tvw_login_register)
    private TextView tvw_register;

    @ViewInject(value = R.id.tvw_login_forget)
    private TextView tvw_forgetpass;

    @ViewInject(value = R.id.ett_login_admin)
    private EditText ett_admin;

    @ViewInject(value = R.id.ett_login_pass)
    private EditText ett_pass;


    @Event(value = R.id.btn_login)
    private void toLogin(View view) {
        check_text();
    }

    @Event(value = R.id.tvw_login_forget)
    private void toForget(View view) {
        toActivity(2);
    }

    @Event(value = R.id.tvw_login_register)
    private void toRegister(View view) {
        toActivity(1);
    }

    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏显示
        x.view().inject(this);
    }


    @Override
    public void initData() {
        checkAdmin();
        BmobProxy.getInstance().setToLoginListener(this);
    }

    private void toActivity(int flag) {
        Intent intent;
        switch (flag) {
            case 1:
                intent = new Intent(LoginActivity.this, RegForActivity.class);
                intent.putExtra("flag", 1);
                break;
            case 2: {
                intent = new Intent(LoginActivity.this, RegForActivity.class);
                intent.putExtra("flag", 2);
                break;
            }
            case 3: {
                intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                break;
            }
            default:
                intent = null;
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
        LoadDialog.getInstance().show(LoginActivity.this);
        BmobProxy.getInstance().bmobLogin(admin, pass);
    }


    private void checkAdmin() {
        BmobUser userInfo = BmobUser.getCurrentUser();
        if (userInfo != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else {
            setLogin();
        }
    }

    private void setLogin(){
        String[] str = Util.getInstance().getAdmin(LoginActivity.this);
        ett_admin.setText(str[0]);
        ett_pass.setText(str[1]);
    }

    @Override
    public void onLoginSuccess() {
        Util.getInstance().saveAdmin(LoginActivity.this,
                ett_admin.getText().toString().trim(), ett_pass.getText().toString().trim());
        toActivity(3);
    }

    @Override
    public void onLoginFailure(String tip) {
        Toast.makeText(LoginActivity.this, tip, Toast.LENGTH_SHORT).show();
        LoadDialog.getInstance().dismiss();
    }
}
