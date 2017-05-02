package org.explosion.zhihudaily.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.explosion.zhihudaily.R;
import org.explosion.zhihudaily.bean.StoryContent;
import org.explosion.zhihudaily.helper.WebUtils;
import org.explosion.zhihudaily.helper.parseJSON;
import org.explosion.zhihudaily.support.Constants;

import okhttp3.Call;
import okhttp3.Response;

public class StoryActivity extends AppCompatActivity {

    private static final String TAG = "StoryActivity";

    WebView webView;
    ImageView headImage;
    StoryContent storyContent;
    Toolbar toolbar;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        toolbar = (Toolbar) findViewById(R.id.story_toolbar);
        webView = (WebView) findViewById(R.id.story_web_view);
        headImage = (ImageView) findViewById(R.id.head_image);
        mContext = getApplicationContext();
        loadStory();
    }

    private void loadStory() {
        int storyId = getIntent().getIntExtra(Constants.KEY.STORY_ID, -1);
        if (storyId == -1)
            return;
        OkGo.get(Constants.URL.NEWS_CONTENT_PREFIX + Integer.toString(storyId))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        storyContent = new StoryContent(parseJSON.parseStoryContent(s));
                        String html = WebUtils.buildHtmlWithCss(storyContent.getBody(), storyContent.getCss());
                        webView.loadDataWithBaseURL(null, html, WebUtils.MIME_TYPE, WebUtils.ENCODING, null);
                        Glide.with(mContext).load(storyContent.getImage()).into(headImage);
                        toolbar.setTitle(storyContent.getTitle());
                        setSupportActionBar(toolbar);
                    }
                });
    }

}
