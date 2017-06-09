/*
 * MIT License
 *
 * Copyright (c) 2017 dianlujitao <dianlujitao@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.explosion.zhihudaily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
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
import org.explosion.zhihudaily.helper.ParseJSON;
import org.explosion.zhihudaily.helper.WebUtils;
import org.explosion.zhihudaily.support.Constants;

import okhttp3.Call;
import okhttp3.Response;

import static org.explosion.zhihudaily.helper.WebUtils.getStoryURL;

public class StoryActivity extends BaseActivity {

    private static final String TAG = "StoryActivity";

    private WebView webView;
    private ImageView headImage;
    private StoryContent storyContent;
    private net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;

    private Context mContext;

    private boolean saveCellularData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        webView = (WebView) findViewById(R.id.story_web_view);
        headImage = (ImageView) findViewById(R.id.head_image);
        // 支持多行Title的CollapsingToolbarLayout
        collapsingToolbarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view_wv);
        mContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.story_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) { // Toolbar返回按钮
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        saveCellularData = WebUtils.isCellularDataLessModeEnabled();
        if (saveCellularData) { // 省流模式下不加载图片
            webView.getSettings().setLoadsImagesAutomatically(false);
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
                    startActivity(Intent.createChooser(shareIntent, "分享到..."));
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
        OkGo.get(getStoryURL(storyId))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        storyContent = new StoryContent(ParseJSON.getStoryContent(s));
                        collapsingToolbarLayout.setTitle(storyContent.getTitle());

                        String html = WebUtils.buildHtmlWithCss(storyContent.getBody(), storyContent.getCss());
                        webView.loadDataWithBaseURL(null, html, WebUtils.MIME_TYPE, WebUtils.ENCODING, null);
                        if (storyContent.getImage() == null || saveCellularData) {
                            // 无头图或省流模式打开
                            disableAppbarCollapse();
                        } else {
                            Glide.with(mContext).load(storyContent.getImage()).into(headImage);
                        }
                    }
                });
    }

    private void disableAppbarCollapse() {
        appBarLayout.setExpanded(false);
        nestedScrollView.setNestedScrollingEnabled(false);
    }
}
