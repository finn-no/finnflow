package no.finn.finnflow.fragmentcompat;

public abstract class FragmentViewPresenter<VIEW, DIALOG, STATE> {
    private final STATE state;
    private VIEW view = null;
    private DIALOG dialog = null;

    protected FragmentViewPresenter(STATE state) {
        this.state = state;
    }

    public final void takeView(VIEW view) {
        this.view = view;
        onLoad();
    }

    public final void dropView() {
        onSave();
        view = null;
    }

    public final VIEW getView() {
        return view;
    }

    protected void onLoad() {

    }

    protected void onSave() {

    }

    public final void takeDialog(DIALOG dialog) {
        assert this.dialog == null;
        this.dialog = dialog;
        onLoadDialog();
    }

    public final void dropDialog() {
        onSaveDialog();
        assert this.dialog != null;
        dialog = null;
    }

    protected void onLoadDialog() {

    }

    protected void onSaveDialog() {

    }

    public STATE getState() {
        return state;
    }

    public final DIALOG getDialog() {
        return dialog;
    }


    private static class FragmentViewPresenterException extends IllegalStateException {
        public FragmentViewPresenterException(String detailMessage) {
            super(detailMessage);
        }
    }
}