package com.twinkle.cdnubbs.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.adapter.AdminAdapter;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.utils.Init;
import com.twinkle.cdnubbs.content.LoadDialog;
import com.twinkle.cdnubbs.utils.Util;
import com.twinkle.cdnubbs.entity.User;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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

public class AdminActivity extends BaseActivity implements AdminAdapter.onQuitListener,
        BmobProxy.onInfoUpdateListener, BmobProxy.bmobUploadPicListener,BmobProxy.onPicUpdateListener {

    @ViewInject(value = R.id.lv_admin)
    private ListView lv_admin;

    @ViewInject(value = R.id.tb_admin)
    private Toolbar tb_admin;

    private AdminAdapter adminAdapter;

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
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);
    }

    @Override
    public void initData() {

        setListener();
        files = new ArrayList<>();
        //toolbar
        tb_admin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //setadapter
        adminAdapter = new AdminAdapter(AdminActivity.this, files);
        adminAdapter.setOnQuitListener(this);

        lv_admin.setAdapter(adminAdapter);
        lv_admin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        Crop.pickImage(AdminActivity.this);
                        break;
                    case 2:
                        getDialog(i);
                        break;
                    case 5:
                        AlertDialog alertDialog = new AlertDialog.Builder(AdminActivity.this).setTitle("等级")
                                .setNegativeButton("确定", null).setMessage(getString(R.string.level_tip)).create();
                        alertDialog.show();
                    default:
                        break;
                }
            }
        });
    }

    private void setListener() {
        BmobProxy.getInstance().setOnInfoUpdate(this);
        BmobProxy.getInstance().setBmobUploadPicListener(this);
        BmobProxy.getInstance().setOnPicUpdateListener(this);

    }

    private void getDialog(final int ii) {
        final EditText editText = new EditText(AdminActivity.this);
        User user = BmobUser.getCurrentUser(User.class);
        editText.setText(user.getName());
        AlertDialog messDialog = new AlertDialog.Builder(AdminActivity.this)
                .setTitle(Init.lv_admin[2])
                .setView(editText)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadDialog.getInstance().show(AdminActivity.this);
                        BmobProxy.getInstance().bmobUpdateInfo(editText.getText().toString(), ii);
                    }
                }).create();
        messDialog.show();
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
            File actualImage;
            try {
                actualImage = Util.getInstance().from(this, Crop.getOutput(result));
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
            Toast.makeText(AdminActivity.this, getString(R.string.failed_to_open), Toast.LENGTH_SHORT).show();
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
                            LoadDialog.getInstance().show(AdminActivity.this);
                            try {
                                BmobProxy.getInstance().bmobUploadPic(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Toast.makeText(AdminActivity.this, getString(R.string.failed_to_open), Toast.LENGTH_SHORT).show();
                            LoadDialog.getInstance().dismiss();
                        }
                    });
        }
    }



    //广播更新画面
    private void myUpdate() {
        Intent intent = new Intent();
        intent.setAction(Init.UpdateInfo);
        sendBroadcast(intent);
    }

    @Override
    public void onLogout() {
        finish();
    }

    @Override
    public void onInfoUpdateSuccess() {
        Toast.makeText(AdminActivity.this, getString(R.string.revise_info), Toast.LENGTH_SHORT).show();
        myUpdate();
        LoadDialog.getInstance().dismiss();
        adminAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInfoUpdateFailure(String tip) {
        Toast.makeText(AdminActivity.this, tip, Toast.LENGTH_SHORT).show();
        LoadDialog.getInstance().dismiss();
    }

    @Override
    public void onUploadPicSuccess(BmobFile bmobFile) {
       BmobProxy.getInstance().bmobUpdatePic(bmobFile.getFileUrl());
    }

    @Override
    public void onUploadPicFailure(String tip) {
        Toast.makeText(AdminActivity.this, tip, Toast.LENGTH_SHORT).show();
        LoadDialog.getInstance().dismiss();
    }

    @Override
    public void onPicUpdateSuccess() {
        adminAdapter.notifyDataSetChanged();
        myUpdate();
        LoadDialog.getInstance().dismiss();
    }

    @Override
    public void onPicUpdateFailure(String tip) {
        Toast.makeText(AdminActivity.this, tip, Toast.LENGTH_SHORT).show();
        LoadDialog.getInstance().dismiss();
    }
}
