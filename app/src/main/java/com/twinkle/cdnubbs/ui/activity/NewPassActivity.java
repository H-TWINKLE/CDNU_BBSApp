package com.twinkle.cdnubbs.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.utils.Util;
import com.twinkle.cdnubbs.entity.User;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class NewPassActivity extends BaseActivity implements BmobProxy.onPassReviseListener {

    @ViewInject(value = R.id.et_new_pass)
    private EditText et_new_pass;

    @ViewInject(value = R.id.tb_new_pass)
    private Toolbar tb_new_pass;

    @ViewInject(value = R.id.iv_new_pass_next)
    private ImageView iv_new_pass_next;

    private boolean is_check = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_pass;
    }


    @Event(value = R.id.iv_new_pass_next)
    private void checkPass(View view) {
        check_pass();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);
    }


    @Override
    public void initData() {

        tb_new_pass.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void check_pass() {
        String pass = et_new_pass.getText().toString().trim();
        if (TextUtils.isEmpty(pass)) {
            et_new_pass.setError(getString(R.string.tip_input));
            et_new_pass.setFocusable(true);
        } else {
            if (!is_check) {
                if (!pass.equals(BmobUser.getCurrentUser(User.class).getPass())) {
                    et_new_pass.setError(getString(R.string.pass_not_correct));

                } else {
                    is_check = true;
                    et_new_pass.getText().clear();
                    et_new_pass.setHint(getString(R.string.new_pass));
                }
            } else {
                BmobProxy.getInstance().setOnPassReviseListener(this);
                BmobProxy.getInstance().bmobRevisePass(pass);
            }

        }

    }


    @Override
    public void onPassReviseSuccess() {
        Toast.makeText(NewPassActivity.this, getString(R.string.pass_revise_succcess), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPassReviseFailure(String tip) {
        Toast.makeText(NewPassActivity.this, tip, Toast.LENGTH_SHORT).show();
    }
}
