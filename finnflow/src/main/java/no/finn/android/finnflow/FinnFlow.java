package no.finn.android.finnflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import flow.Flow;
import flow.History;
import flow.StateParceler;

public abstract class FinnFlow implements Flow.Dispatcher {
    private static final String FINNFLOWDELEGATE = "FinnFlowDelegate";
    private static final String HISTORY_KEY = "BaseFlowActivity.HISTORY_KEY";

    private final Activity activity;
    private final StateParceler parceler;
    private final boolean debugBuild;
    private final ContainerView container;

    Screen destinationScreen = null;
    boolean isResumed = false;

    public FinnFlow(Activity activity, StateParceler parceler, boolean debugBuild) {
        this.activity = activity;
        this.parceler = parceler;
        this.debugBuild = debugBuild;
        container = new ContainerView(activity, this);
    }

    public static Intent getIntent(Context context, Screen screen, Class<?> cls, StateParceler parceler) {
        return getIntent(context, History.single(screen), cls, parceler);
    }

    public static Intent getIntent(Context context, History history, Class<?> cls, StateParceler parceler) {
        Intent intent = new Intent(context, cls);
        setHistoryExtra(intent, history, parceler);
        return intent;
    }

    public static void setHistoryExtra(Intent intent, History history, StateParceler parceler) {
        //@hack : https://github.com/square/flow/issues/99
        intent.putExtra(HISTORY_KEY, history.getParcelable(parceler));
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
            defaultHistory = History.from(activity.getIntent().getParcelableExtra(FinnFlow.HISTORY_KEY), getParcer());
        }
        return defaultHistory;
    }

    boolean isDebugBuild() {
        return debugBuild;
    }

    StateParceler getParcer() {
        return parceler;
    }
}
