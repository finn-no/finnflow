package no.finn.android.finnflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class Screen<VIEW extends View, SCREEN extends Screen, PRESENTER extends ViewPresenter<VIEW, SCREEN>> {
    public DialogState dialogState;
    private PRESENTER presenter = null;

    public final View createView(ViewGroup container) {
        Context context = new PresenterContext<>(container.getContext(), getPresenter());
        return LayoutInflater.from(context).inflate(getViewResource(), container, false);
    }

    public abstract int getViewResource();

    public PRESENTER getPresenter() {
        if (presenter == null) {
            presenter = createPresenter();
        }
        return presenter;
    }

    public abstract PRESENTER createPresenter();

    public abstract static class DialogState {
        public abstract FlowAlertDialogBuilder createDialog(Context context, ViewPresenter presenter);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && (o == this || o.getClass() == getClass());
    }

    public boolean saveViewState() {
        return false;
    }
}