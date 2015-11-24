package no.finn.android.finnflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import flow.StateParceler;

public abstract class FinnFlowActivity extends Activity {
    private FinnFlowDelegate finnFlowDelegate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (finnFlowDelegate == null) {
            finnFlowDelegate = new FinnFlowDelegate(new FinnFlow(this, BuildConfig.DEBUG) {
                @Override
                public StateParceler createParcer() {
                    return FinnFlowActivity.this.createParcer();
                }

                @Override
                public Screen createDefaultScreen() {
                    return FinnFlowActivity.this.createDefaultScreen();
                }
            });
        }
        finnFlowDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        finnFlowDelegate.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finnFlowDelegate.onResume();
    }

    @Override
    protected void onPause() {
        finnFlowDelegate.onPause();
        super.onPause();
    }

    @SuppressWarnings("deprecation") // https://code.google.com/p/android/issues/detail?id=151346
    @Override
    public Object onRetainNonConfigurationInstance() {
        Object o = finnFlowDelegate.onRetainNonConfigurationInstance();
        return o != null ? o : super.onRetainNonConfigurationInstance();
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        Object o = finnFlowDelegate != null ? finnFlowDelegate.getSystemService(name) : null;
        return o != null ? o : super.getSystemService(name);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        finnFlowDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (finnFlowDelegate.goBack()) {
            return;
        }
        super.onBackPressed();
    }

    protected abstract Screen createDefaultScreen();

    protected abstract StateParceler createParcer();
}
