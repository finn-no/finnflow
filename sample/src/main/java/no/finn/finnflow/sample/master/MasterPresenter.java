package no.finn.finnflow.sample.master;

import no.finn.finnflow.FlowContext;
import no.finn.finnflow.ViewPresenter;
import no.finn.finnflow.sample.detail.DetailScreen;

import flow.Flow;

public class MasterPresenter extends ViewPresenter<MasterView, MasterScreen> {
    protected MasterPresenter(MasterScreen screen) {
        super(screen);
    }

    public void detailButtonClicked() {
        Flow flow = FlowContext.getFlow(getView().getContext());
        flow.goTo(new DetailScreen(123));
    }
}
