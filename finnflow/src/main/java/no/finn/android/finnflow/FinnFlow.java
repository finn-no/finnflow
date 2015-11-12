package no.finn.android.finnflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import flow.Flow;
import flow.History;

public abstract class FinnFlow implements Flow.Dispatcher {
    private static final String FINNFLOWDELEGATE = "FinnFlowDelegate";
    private static final String HISTORY_KEY = "BaseFlowActivity.HISTORY_KEY";
    private static final LoganStateParceler parcer = new LoganStateParceler();

    private final Activity activity;
    private final boolean debugBuild;
    private final ContainerView container;

    Screen destinationScreen = null;
    boolean isResumed = false;

    public static Intent getIntent(Context context, Screen screen, Class<?> cls) {
        return getIntent(context, History.single(screen), cls);
    }

    public static Intent getIntent(Context context, History history, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        setHistoryExtra(intent, history);
        return intent;
    }

    public static void setHistoryExtra(Intent intent, History history) {
        //@hack : https://github.com/square/flow/issues/99
        intent.putExtra(HISTORY_KEY, history.getParcelable(parcer));
    }

    public FinnFlow(Activity activity, boolean debugBuild) {
        this.activity = activity;
        this.debugBuild = debugBuild;
        container = new ContainerView(activity, this);
    }

    public LoganStateParceler getParcer() {
        return parcer;
    }

    public abstract Screen createDefaultScreen();

    public boolean isResumed() {
        return isResumed;
    }

    public Screen getDestinationScreen() {
        return destinationScreen;
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public void dispatch(Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        destinationScreen = traversal.destination.top();
        container.dispatch(traversal, callback);
    }

    @SuppressWarnings("ResourceType")
    public static FinnFlow get(Context context) {
        return (FinnFlow) context.getSystemService(FINNFLOWDELEGATE);
    }

    static boolean isFinnFlowSystemService(String name) {
        return FINNFLOWDELEGATE.equals(name);
    }

    public View getContainerView() {
        return container;
    }

    public History createHistory(Bundle savedInstanceState) {
        History defaultHistory = History.single(createDefaultScreen());
        if (savedInstanceState == null && activity.getIntent().hasExtra(FinnFlow.HISTORY_KEY)) {
            defaultHistory = History.from(activity.getIntent().getParcelableExtra(FinnFlow.HISTORY_KEY), FinnFlow.parcer);
        }
        return defaultHistory;
    }

    boolean isDebugBuild() {
        return debugBuild;
    }
}
