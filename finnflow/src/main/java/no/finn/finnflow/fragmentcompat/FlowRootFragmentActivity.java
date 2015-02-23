package no.finn.finnflow.fragmentcompat;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class FlowRootFragmentActivity extends FragmentActivity {
    private boolean destroying = false;

    public FlowRootFragmentActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        destroying = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        //@hack : needed due to fragment wrapping in screens.
        destroying = true;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                if (fragment instanceof FlowFragment && ((FlowFragment) fragment).popBackStack()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    public boolean isDestroying() {
        return destroying;
    }
}
