package com.twinkle.cdnubbs.ui;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.java.content.BaseActivity;
import com.twinkle.cdnubbs.java.utils.Util;
import com.twinkle.cdnubbs.user.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class NewPassActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_new_pass;
    private Toolbar tb_new_pass;
    private ImageView iv_new_pass_next;
    private boolean is_check = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_pass;
    }

    @Override
    public void findView() {
        et_new_pass = (EditText) findViewById(R.id.et_new_pass);
        tb_new_pass = (Toolbar) findViewById(R.id.tb_new_pass);
        iv_new_pass_next = (ImageView) findViewById(R.id.iv_new_pass_next);

    }

    @Override
    public void initView() {
        //settoolbar
        tb_new_pass.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_new_pass_next.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_new_pass_next:
                check_pass();
                break;
            default:
                break;
        }
    }

    private void check_pass() {
        String pass = et_new_pass.getText().toString().trim();
        if (TextUtils.isEmpty(pass)) {
            et_new_pass.setError(getString(R.string.tip_input));
            et_new_pass.setFocusable(true);
        } else {
            if (!is_check) {
                if (!pass.equals(Util.getUser().getPass())) {
                    et_new_pass.setError(getString(R.string.pass_not_correct));

                } else {
                    is_check = true;
                    et_new_pass.getText().clear();
                    et_new_pass.setHint(getString(R.string.new_pass));
                }
            } else {
                User bmobUser = BmobUser.getCurrentUser(User.class);
                bmobUser.setPassword(pass);
                bmobUser.setPass(pass);
                bmobUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Util.UiToast(NewPassActivity.this, getString(R.string.pass_revise_succcess));
                            finish();
                        } else {
                            Util.UiToast(NewPassActivity.this, e.toString());
                        }
                    }
                });
            }

        }

    }


}
