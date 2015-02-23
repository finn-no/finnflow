package no.finn.finnflow;

import android.os.Bundle;

import flow.Backstack;
import flow.Flow;
import flow.Parcer;

public abstract class FlowBundler {
    private static final String FLOW_KEY = "flow";
    private final Parcer parcer;
    private final Flow.Listener listener;

    private Flow flow;

    public abstract Screen createDefault();

    public FlowBundler(Parcer parcer, Flow.Listener listener) {
        this.parcer = parcer;
        this.listener = listener;
    }

    public void saveInstanceState(Bundle outState) {
        if (flow.getBackstack() == null) {
            return;
        }
        outState.putParcelable(FLOW_KEY, flow.getBackstack().getParcelable(parcer));
    }

    public void onCreate(Bundle savedInstanceState) {
        Backstack backstack;
        if (savedInstanceState == null) {
            backstack = Backstack.fromUpChain(createDefault());
        } else {
            backstack = Backstack.from(savedInstanceState.getParcelable(FLOW_KEY), parcer);
        }
        flow = new Flow(backstack, listener);
    }

    public Flow getFlow() {
        return flow;
    }
}
