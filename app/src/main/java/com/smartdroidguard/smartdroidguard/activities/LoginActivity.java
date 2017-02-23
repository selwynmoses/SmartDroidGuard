package com.smartdroidguard.smartdroidguard.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.smartdroidguard.smartdroidguard.PreyConfig;
import com.smartdroidguard.smartdroidguard.PreyVerify;
import com.smartdroidguard.smartdroidguard.services.PreyDisablePowerOptionsService;

public class LoginActivity extends Activity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Delete notifications (in case Activity was started by one of them)

        startup();


        boolean disablePowerOptions = PreyConfig.getPreyConfig(getApplicationContext()).isDisablePowerOptions();
        if (disablePowerOptions) {
            startService(new Intent(getApplicationContext(), PreyDisablePowerOptionsService.class));
        } else {
            stopService(new Intent(getApplicationContext(), PreyDisablePowerOptionsService.class));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        startup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startup();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startup();
    }

    private void startup() {
        Intent intent = null;
        boolean ready = PreyConfig.getPreyConfig(this).getProtectReady();
        if (isThisDeviceAlreadyRegisteredWithPrey()) {
            PreyVerify.getInstance(this);
        }
        if (isThereBatchInstallationKey() && !ready) {
            showLoginBatch();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        Intent intent = null;
        intent = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void showLoginBatch() {
        Intent intent = null;
        intent = new Intent(LoginActivity.this, WelcomeBatchActivity.class);
        startActivity(intent);
        finish();
    }


    private boolean isThisDeviceAlreadyRegisteredWithPrey() {
        return PreyConfig.getPreyConfig(LoginActivity.this).isThisDeviceAlreadyRegisteredWithPrey(false);
    }

    private void showFeedback(Context ctx) {
        Intent popup = new Intent(ctx, FeedbackActivity.class);
        popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(popup);
    }

    private boolean isThereBatchInstallationKey() {
        String apiKeyBatch = PreyConfig.getPreyConfig(LoginActivity.this).getApiKeyBatch();
        return (apiKeyBatch != null && !"".equals(apiKeyBatch));
    }

}

