package no.finn.android.finnflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import flow.Flow;
import flow.FlowDelegate;
import flow.History;

public class FinnFlowDelegate {
    private final FinnFlow finnFlow;
    private FlowDelegate flowSupport;
    private final Activity activity;

    public FinnFlowDelegate(FinnFlow finnFlow) {
        this.finnFlow = finnFlow;
        activity = finnFlow.getActivity();
    }

    public void onCreate(Bundle savedInstanceState) {
        activity.setContentView(finnFlow.getContainerView());
        @SuppressWarnings("deprecation") FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) activity.getLastNonConfigurationInstance();

        History defaultHistory = finnFlow.createHistory(savedInstanceState);
        flowSupport = FlowDelegate.onCreate(nonConfig, activity.getIntent(), savedInstanceState, finnFlow.getParcer(), defaultHistory, finnFlow);
    }

    public void onNewIntent(Intent intent) {
        flowSupport.onNewIntent(intent);
    }

    public void onResume() {
        finnFlow.isResumed = true;
        flowSupport.onResume();
    }

    public void onPause() {
        finnFlow.isResumed = false;
        flowSupport.onPause();
    }

    @SuppressWarnings("deprecation") // https://code.google.com/p/android/issues/detail?id=151346
    public Object onRetainNonConfigurationInstance() {
        //NB : this is optional
        return flowSupport.onRetainNonConfigurationInstance();
    }

    public Object getSystemService(String name) {
        if (FinnFlow.isFinnFlowSystemService(name)) {
            return finnFlow;
        }
        Object service = null;
        if (flowSupport != null) {
            service = flowSupport.getSystemService(name);
        }
        return service;
    }

    public void onSaveInstanceState(Bundle outState) {
        flowSupport.onSaveInstanceState(outState);
    }

    public boolean goBack() {
        Flow flow = Flow.get(activity);
        Screen currentScreen = flow.getHistory().top();
        if (currentScreen instanceof HandlesBack) {
            if (((HandlesBack) currentScreen).onBackPressed()) {
                return true;
            }
        }
        return flow.goBack();
    }
}
