package com.bruce.note.greennote.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bruce.note.greennote.R;
import com.zhouwei.blurlibrary.EasyBlur;

/**
 * Created by liq on 2017/7/17.
 */

public class ActivityBlur extends BaseActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_blur);
        mImageView = (ImageView) findViewById(R.id.iv_blur);
        final ViewGroup root_main = (ViewGroup) findViewById(R.id.root_main);
        root_main.setBackgroundResource(R.drawable.bg_blue_boy);

        root_main.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                root_main.getViewTreeObserver().removeOnPreDrawListener(this);
                root_main.buildDrawingCache();
                Bitmap drawingCache = root_main.getDrawingCache();
                Bitmap overLay = Bitmap.createBitmap(mImageView.getMeasuredWidth(), mImageView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(overLay);
                canvas.translate(-mImageView.getLeft(), -mImageView.getTop());
                canvas.drawBitmap(drawingCache, 0, 0, null);

                Bitmap finalBitmap = EasyBlur.with(ActivityBlur.this).bitmap(overLay).radius(10).policy(EasyBlur.BlurPolicy.FAST_BLUR).blur();

                mImageView.setBackground(new BitmapDrawable(getResources(), finalBitmap));
                return false;
            }
        });
    }


}
