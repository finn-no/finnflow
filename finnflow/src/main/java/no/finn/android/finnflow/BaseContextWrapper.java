package no.finn.android.finnflow;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;

public class BaseContextWrapper<T> extends ContextWrapper {
    private final String key;
    private final T wrapped;
    private LayoutInflater inflater;

    public BaseContextWrapper(Context base, String key, T wrapped) {
        super(base);
        this.key = key;
        this.wrapped = wrapped;
    }

    @Override
    public Object getSystemService(String name) {
        if (key.equals(name)) {
            return wrapped;
        }
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (inflater == null) {
                inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return inflater;
        }
        return super.getSystemService(name);
    }

    @SuppressWarnings("ResourceType")
    protected static <P> P getWrapped(Context context, String key) {
        return (P) context.getSystemService(key);
    }

    public static Activity getActivity(View view) {
        return getActivity(view.getContext());
    }

    public static Activity getActivity(Context context) {
        if (!(context instanceof Activity)) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return (Activity) context;
    }
}
