package no.finn.finnflow.fragmentcompat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


// @hack : This class exists as a hack to work with getParentFragment() in dialogs until we remove the fragments altogether.
public class StateFragmentLink {
    public FragmentManager fragmentManager;

    public void takeFragment(Fragment fragment) {
        fragmentManager = fragment.getChildFragmentManager();
    }

    public void dropFragment() {
        fragmentManager = null;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
