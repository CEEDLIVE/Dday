package com.ceedlive.dday.firebase;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ceedlive.dday.BaseActivity;
import com.ceedlive.dday.R;

public class FireBaseActivity extends BaseActivity {

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);

        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ) {
            // bundle must contain all info sent in "data" field of the notification
            Log.e("CFB", "FireBaseActivity onCreate");
        }

        initialize();
    }

    @Override
    protected void initialize() {
        mImageView = findViewById(R.id.iv_frame);

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop();

        Glide.with(getApplicationContext())
                .load("https://placeimg.com/1024/768/any")
                .apply(requestOptions)
                .into(mImageView);
    }
}
