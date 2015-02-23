package no.finn.finnflow.sample;

import android.os.Bundle;

import no.finn.finnflow.fragmentcompat.FlowFragment;
import no.finn.finnflow.fragmentcompat.FlowRootFragmentActivity;
import no.finn.finnflow.sample.master.MasterScreen;

public class BaseActivity extends FlowRootFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseactivity);
        if (savedInstanceState == null) {
            FlowFragment.goToScreen(getSupportFragmentManager(), R.id.fragment_root, new MasterScreen(), false);
        }
    }
}
