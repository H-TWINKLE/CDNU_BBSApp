package com.twinkle.cdnubbs.content;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

import org.xutils.x;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        x.image().bind(imageView,path.toString());
    }

}
