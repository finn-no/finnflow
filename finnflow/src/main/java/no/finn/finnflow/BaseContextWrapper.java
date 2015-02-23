package no.finn.finnflow;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

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

    public static FragmentActivity getActivity(Context context) {
        if (!(context instanceof FragmentActivity)) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return (FragmentActivity) context;
    }
}
