package no.finn.android.finnflow.sample.master;

import no.finn.android.finnflow.ViewPresenter;
import no.finn.android.finnflow.sample.detail.DetailScreen;

import flow.Flow;

public class MasterPresenter extends ViewPresenter<MasterView, MasterScreen> {
    protected MasterPresenter(MasterScreen screen) {
        super(screen);
    }

    public void detailButtonClicked() {
        Flow flow = Flow.get(getView().getContext());
        flow.set(new DetailScreen(123));
    }
}
