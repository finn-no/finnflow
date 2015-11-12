package no.finn.android.finnflow.sample.detail;

import no.finn.android.finnflow.Screen;
import no.finn.android.finnflow.sample.R;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class DetailScreen extends Screen<DetailView, DetailScreen, DetailPresenter> {

    @JsonField
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
