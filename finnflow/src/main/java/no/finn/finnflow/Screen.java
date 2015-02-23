package no.finn.finnflow;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class Screen<VIEW extends View, SCREEN extends Screen, PRESENTER extends ViewPresenter<VIEW, SCREEN>> {
    private SparseArray<Parcelable> viewState = null;
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

    public void setViewState(SparseArray<Parcelable> viewState) {
        this.viewState = viewState;
    }

    public SparseArray<Parcelable> getViewState() {
        return viewState;
    }

    public abstract static class DialogState {
        public abstract FlowAlertDialogBuilder createDialog(Context context, ViewPresenter presenter);
    }

    public boolean saveViewState() {
        return false;
    }
}