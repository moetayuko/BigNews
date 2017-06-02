package org.explosion.zhihudaily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        toolbar = (Toolbar) findViewById(R.id.story_toolbar);
        webView = (WebView) findViewById(R.id.story_web_view);
        headImage = (ImageView) findViewById(R.id.head_image);
        collapsingToolbarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mContext = getApplicationContext();

        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        loadStory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.toolbar_share:
                if (storyContent != null) {
                    String shareText = "";
                    shareText += storyContent.getTitle() + " （分享自@知乎日报）" + storyContent.getShareUrl();
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    shareIntent.setType("text/plain");
                    startActivity(shareIntent);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadStory() {
        int storyId = getIntent().getIntExtra(Constants.KEY.STORY_ID, -1);
        if (storyId == -1)
            return;
        OkGo.get(Constants.URL.STORY_CONTENT_PREFIX + Integer.toString(storyId))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        storyContent = new StoryContent(parseJSON.getStoryContent(s));
                        collapsingToolbarLayout.setTitle(storyContent.getTitle());

                        String html = WebUtils.buildHtmlWithCss(storyContent.getBody(), storyContent.getCss());
                        webView.loadDataWithBaseURL(null, html, WebUtils.MIME_TYPE, WebUtils.ENCODING, null);
                        Glide.with(mContext).load(storyContent.getImage()).into(headImage);
                    }
                });
    }

}
