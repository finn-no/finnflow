package no.finn.finnflow.sample.master;

import no.finn.finnflow.Screen;
import no.finn.finnflow.sample.R;

import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class MasterScreen extends Screen<MasterView, MasterScreen, MasterPresenter> {
    @Override
    public int getViewResource() {
        return R.layout.master_screen;
    }

    @Override
    public MasterPresenter createPresenter() {
        return new MasterPresenter(this);
    }
}
