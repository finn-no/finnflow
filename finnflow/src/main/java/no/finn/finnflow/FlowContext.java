package no.finn.finnflow;

import android.content.Context;

import flow.Flow;

public class FlowContext extends BaseContextWrapper<Flow> {
    private static final String KEY = "flow";

    public FlowContext(Context base, Flow wrapped) {
        super(base, KEY, wrapped);
    }

    @SuppressWarnings("ResourceType")
    public static Flow getFlow(Context context) {
        return getWrapped(context, KEY);
    }
}
