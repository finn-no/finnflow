package no.finn.android.finnflow;

import android.content.Context;
import android.view.View;

public abstract class ViewPresenter<V extends View, S extends Screen> {
    private final S screen;
    private V view = null;
    private FlowAlertDialogBuilder dialog;

    protected ViewPresenter(S screen) {
        this.screen = screen;
    }

    public S getScreen() {
        return screen;
    }

    public final void takeView(V view) {
        if (this.view != null) {
            // Fragments can cause some iffy behavior here. We've seen logs where onAttachedToWindow is triggered twice in a row...
            dropView();
        }

        this.view = view;
        onLoad();

        if (getScreen().dialogState != null) {
            showDialog(view.getContext(), getScreen().dialogState);
        }
    }

    public final void dropView() {
        if (view == null) {
            //dropView can happen twice during screen moving/animations, we only trigger callbacks once.
            return;
        }
        if (dialog != null) {
            dialog.onSave();
            dialog.dismissQuietly();
        }
        onSave();
        view = null;
    }

    public final void showDialog(Context context, Screen.DialogState dialogState) {
        if (dialog != null) {
            // if we already have a dialog, toss it
            dialog.dismissQuietly();
        }
        getScreen().dialogState = dialogState;
        dialog = dialogState.createDialog(context, this);
        dialog.create().show();
    }

    public void onDialogClosed() {
        dialog = null;
        getScreen().dialogState = null;
    }

    public V getView() {
        return view;
    }

    protected void onLoad() {
    }

    protected void onSave() {
    }

    public FlowAlertDialogBuilder getDialog() {
        return dialog;
    }
}