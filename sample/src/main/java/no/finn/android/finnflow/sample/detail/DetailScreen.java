package no.finn.android.finnflow.sample.detail;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.finn.android.finnflow.Screen;
import no.finn.android.finnflow.sample.R;

public class DetailScreen extends Screen<DetailView, DetailScreen, DetailPresenter> {

    @JsonProperty
    int whatever_id; // will be persisted on rotate/saveinstancestate.

    public DetailScreen() {
        // Needed for serialization
    }

    public DetailScreen(int whatever_id) {
        this.whatever_id = whatever_id;
    }

    @Override
    public int getViewResource() {
        return R.layout.detail_screen;
    }

    @Override
    public DetailPresenter createPresenter() {
        return new DetailPresenter(this);
    }
}
