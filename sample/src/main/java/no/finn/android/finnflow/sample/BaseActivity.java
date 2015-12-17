package no.finn.android.finnflow.sample;

import flow.StateParceler;
import no.finn.android.finnflow.FinnFlowActivity;
import no.finn.android.finnflow.Screen;
import no.finn.android.finnflow.sample.master.MasterScreen;

public class BaseActivity extends FinnFlowActivity {
    // You can do this or implement the delegate methods yourself.
    @Override
    protected Screen createDefaultScreen() {
        return new MasterScreen();
    }

    @Override
    protected StateParceler getParcer() {
        return JacksonStateParceler.instance();
    }
}
