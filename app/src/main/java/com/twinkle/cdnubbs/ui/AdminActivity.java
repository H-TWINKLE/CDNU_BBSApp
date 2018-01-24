package com.twinkle.cdnubbs.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.java.adapter.AdminAdapter;
import com.twinkle.cdnubbs.java.content.BaseActivity;
import com.twinkle.cdnubbs.java.utils.Init;
import com.twinkle.cdnubbs.java.content.LoadDialog;
import com.twinkle.cdnubbs.java.content.MessDialog;
import com.twinkle.cdnubbs.java.utils.Util;
import com.twinkle.cdnubbs.user.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AdminActivity extends BaseActivity {

    private ListView lv_admin;
    private Toolbar tb_admin;
    private AdminAdapter adminAdapter;
    private LoadDialog loadDialog;
    private MessDialog messDialog;
    private List<File> files;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_admin;
    }

    @Override
    public void findView() {
        lv_admin = (ListView) findViewById(R.id.lv_admin);
        tb_admin = (Toolbar) findViewById(R.id.tb_admin);
    }


    @Override
    public void initView() {
        //toolbar
        tb_admin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //setadapter
        adminAdapter = new AdminAdapter(AdminActivity.this, files);
        lv_admin.setAdapter(adminAdapter);
        lv_admin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        Crop.pickImage(AdminActivity.this);
                        break;
                    case 2:
                        get_dialog(Init.lv_admin[2], i);
                        break;
                    case 5:
                        messDialog = new MessDialog(AdminActivity.this, Init.lv_admin[5], getString(R.string.level_tip)) {
                            @Override
                            public void then() {

                            }
                        };
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        files = new ArrayList<>();
    }

    private void get_dialog(String title, final int ii) {
        final EditText editText = new EditText(AdminActivity.this);
        User user = Util.getUser();
        editText.setText(user.getName());
        loadDialog = new LoadDialog(AdminActivity.this);
        messDialog = new MessDialog(AdminActivity.this, title, editText) {
            @Override
            public void then() {
                loadDialog.show();
                update_info(editText.getText().toString(), ii);
            }
        };
    }

    private void update_info(String text, int ii) {
        User user = new User();
        if (ii == 2) {
            user.setName(text);
        } else {
            loadDialog.diss();
            return;
        }

        user.update(Util.getUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Util.UiToast(AdminActivity.this, getString(R.string.revise_info));
                    myUpdate();
                    loadDialog.diss();
                    adminAdapter.notifyDataSetChanged();
                } else {
                    Util.UiToast(AdminActivity.this, e.toString());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            File actualImage = null;
            try {
                actualImage = Util.from(this, Crop.getOutput(result));
                compressImage(actualImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void compressImage(File actualImage) {
        if (actualImage == null) {
            Util.UiToast(AdminActivity.this, getString(R.string.failed_to_open));
        } else {
            new Compressor(this)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            files.add(file);
                            adminAdapter.notifyDataSetChanged();
                            loadDialog = new LoadDialog(AdminActivity.this);
                            loadDialog.show();
                            try {
                                upload_pic(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            Util.UiToast(AdminActivity.this, getString(R.string.failed_to_open));
                            loadDialog.diss();
                        }
                    });
        }
    }

    private void upload_pic(File file) throws Exception {
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    update_pic(bmobFile.getFileUrl());
                } else {
                    Util.UiToast(AdminActivity.this, e.getMessage());
                    loadDialog.diss();
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    private void update_pic(String text) {
        User bmobUser = BmobUser.getCurrentUser(User.class);
        bmobUser.setPic(text);
        bmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Util.UiToast(AdminActivity.this, getString(R.string.upload_pic_success));
                    adminAdapter.notifyDataSetChanged();
                    myUpdate();
                    loadDialog.diss();
                } else {
                    Util.UiToast(AdminActivity.this, e.getMessage());
                }
            }
        });
    }

    //广播更新画面
    private void myUpdate() {
        Intent intent = new Intent();
        intent.setAction(Init.UpdateInfo);
        sendBroadcast(intent);
    }

}
