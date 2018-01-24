package com.twinkle.cdnubbs.java.content;

import android.content.Context;
import android.widget.ImageView;

import com.loopj.android.image.SmartImageView;
import com.youth.banner.loader.ImageLoader;

import org.xutils.x;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        x.image().bind(imageView,path.toString());
    }

    @Override
    public ImageView createImageView(Context context) {
        SmartImageView smartImageView = new SmartImageView(context);
        return smartImageView;
    }
}
