package org.explosion.zhihudaily.helper;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.explosion.zhihudaily.R;

/**
 * Created by dianlujitao on 17-6-2.
 */

public final class UIUtils {
    public static void notifyNetworkError(View view, final Context context) {
        Snackbar.make(view, R.string.network_error, Snackbar.LENGTH_LONG)
                .setAction(R.string.network_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    }
                }).show();
    }
}
