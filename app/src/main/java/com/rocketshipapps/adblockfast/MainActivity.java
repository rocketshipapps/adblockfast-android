package com.rocketshipapps.adblockfast;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rocketshipapps.adblockfast.utils.Rule;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    boolean animating = false;

    ImageButton btnAdblock;
    TextView txtStatus;
    TextView txtTap;

    String packageName;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/AvenirNextLTPro-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        packageName = getApplicationContext().getPackageName();
        version = BuildConfig.VERSION_NAME;

        btnAdblock = (ImageButton) findViewById(R.id.btn_adblock);
        txtStatus = (TextView) findViewById(R.id.txt_status);
        txtTap = (TextView) findViewById(R.id.txt_tap);

        if (!Rule.exists(this)) {
            Rule.enable(this);
            enableAnimtaion();
        } else if (Rule.active(this)) {
            enableAnimtaion();
        } else {
            disableAnimtaion();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //region OnClick

    public void onAdBlockPressed(View v) {
        if (animating) return;

        if (Rule.active(this)) {
            Rule.disable(this);
            disableAnimtaion();
        } else {
            Rule.enable(this);
            enableAnimtaion();
        }

        Intent intent = new Intent();
        intent.setAction("com.samsung.android.sbrowser.contentBlocker.ACTION_UPDATE");
        intent.setData(Uri.parse("package:" + packageName));
        sendBroadcast(intent);
    }

    public void onAboutPressed(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_about);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView link = (TextView) dialog.findViewById(R.id.link);
        link.setText(Html.fromHtml(link.getText().toString()));
        link.setMovementMethod(LinkMovementMethod.getInstance());

        dialog.show();

        ((TextView)dialog.findViewById(R.id.txt_version)).setText(version);

        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void onHelpPressed(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_help);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView link = (TextView) dialog.findViewById(R.id.link);
        link.setText(Html.fromHtml(link.getText().toString()));
        link.setMovementMethod(LinkMovementMethod.getInstance());

        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void onSettingsPressed(View v) {
        Intent intent = new Intent();
        intent.setAction("com.samsung.android.sbrowser.contentBlocker.ACTION_SETTING");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (list.size() > 0) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Samsung Browser not found on your system", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    //region Block Animation

    void disableAnimtaion() {
        animator(new int[]{
            R.drawable.blocked_0,
            R.drawable.blocked_1,
            R.drawable.blocked_2,
            R.drawable.blocked_3,
            R.drawable.blocked_4,
            R.drawable.blocked_5,
            R.drawable.blocked_6,
            R.drawable.blocked_7,
            R.drawable.blocked_8,
            R.drawable.blocked_9,
            R.drawable.blocked_10,
            R.drawable.blocked_11,
            R.drawable.blocked_12,
            R.drawable.blocked_13,
            R.drawable.blocked_14,
            R.drawable.blocked_15
        }, R.string.unblocked_status, R.string.unblocked_action);
    }

    void enableAnimtaion() {
        animator(new int[]{
            R.drawable.unblocked_0,
            R.drawable.unblocked_1,
            R.drawable.unblocked_2,
            R.drawable.unblocked_3,
            R.drawable.unblocked_4,
            R.drawable.unblocked_5,
            R.drawable.unblocked_6,
            R.drawable.unblocked_7,
            R.drawable.unblocked_8,
            R.drawable.unblocked_9,
            R.drawable.unblocked_10,
            R.drawable.unblocked_11,
            R.drawable.unblocked_12,
            R.drawable.unblocked_13,
            R.drawable.unblocked_14,
            R.drawable.unblocked_15
        }, R.string.blocked_status, R.string.blocked_action);
    }

    void animator(final int[] res, final int resTxtStatus, final int resTxtTap) {
        animating = true;

        int delay = 100;

        for (int i=0; i<res.length; ++i) {
            if (i==0) {
                btnAdblock.setImageResource(res[i]);
            } else {
                Handler handler = new Handler();
                final int finalI = i;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnAdblock.setImageResource(res[finalI]);

                                if (finalI == res.length-1) {
                                    animating = false;
                                    txtStatus.setText(resTxtStatus);
                                    txtTap.setText(resTxtTap);
                                }
                            }
                        });
                    }
                }, delay * i);
            }
        }
    }

    //endregion
}
