package no.finn.android.finnflow;

import android.content.Context;

public class PresenterContext<P> extends BaseContextWrapper<P> {
    private static final String KEY = "presenter";

    public PresenterContext(Context base, P presenter) {
        super(base, KEY, presenter);
    }

    @SuppressWarnings("ResourceType")
    public static <P> P getPresenter(Context context) {
        return getWrapped(context, KEY);
    }
}