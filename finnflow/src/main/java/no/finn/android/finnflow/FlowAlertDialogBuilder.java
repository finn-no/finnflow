package no.finn.android.finnflow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

public class FlowAlertDialogBuilder extends AlertDialog.Builder implements DialogInterface.OnDismissListener {
    private ViewPresenter viewPresenter;
    private AlertDialog dialog;
    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnShowListener onShowListener;

    public FlowAlertDialogBuilder(Context context, ViewPresenter presenter) {
        super(context);
        this.viewPresenter = presenter;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        hideKeyboard();
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        }
        if (viewPresenter != null) {
            viewPresenter.onDialogClosed();
        }
        dialog = null;
    }

    @NonNull
    @Override
    public AlertDialog create() {
        dialog = super.create();
        dialog.setOnDismissListener(this);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (getDialog() != null) {
                    if (onShowListener != null) {
                        onShowListener.onShow(dialog);
                    }
                    // dialog == null if dialog has been quietly dismissed
                    FlowAlertDialogBuilder.this.onShow(getDialog());
                }
            }
        });
        return dialog;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    @NonNull
    @Override
    public AlertDialog.Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    @NonNull
    public FlowAlertDialogBuilder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }

    public void dismiss() {
        if (dialog != null) {
            hideKeyboard();
            dialog.dismiss();
        }
        dialog = null;
    }

    void dismissQuietly() {
        viewPresenter = null;
        if (dialog != null) {
            dialog.setOnShowListener(null);
        }
        dismiss();
    }

    public void onSave() {
        // save state, afterwards we dismiss the dialog (without getting a callback that it was closed, because it's going to be restored)
    }

    private void hideKeyboard() {
        if (getDialog() != null) {
            ContainerView.hideKeyboard(getDialog().getWindow().getDecorView());
        }
        Activity activity = PresenterContext.getActivity(getContext());
        if (activity != null) {
            ContainerView.hideKeyboard(activity.getWindow().getDecorView());
        }
    }

    public void onShow(AlertDialog dialog) {

    }
}
