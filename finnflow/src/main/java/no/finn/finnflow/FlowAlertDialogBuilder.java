package no.finn.finnflow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

public class FlowAlertDialogBuilder extends AlertDialog.Builder implements DialogInterface.OnDismissListener {
    private ViewPresenter viewPresenter;
    private AlertDialog dialog;
    private DialogInterface.OnDismissListener onDismissListener = null;

    public FlowAlertDialogBuilder(Context context, ViewPresenter presenter) {
        super(context);
        this.viewPresenter = presenter;
        setOnDismissListener(this);
    }

    @NonNull
    @Override
    public AlertDialog create() {
        dialog = super.create();
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

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = null;
    }

    void dismissQuietly() {
        viewPresenter = null;
        dismiss();
    }

    public void onSave() {
        // save state, afterwards we dismiss the dialog (without getting a callback that it was closed, because it's going to be restored)
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (viewPresenter != null) {
            viewPresenter.onDialogClosed();
        }
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }
}
