package no.finn.finnflow.fragmentcompat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.finn.finnflow.ContainerView;
import no.finn.finnflow.FlowBundler;
import no.finn.finnflow.FlowContext;
import no.finn.finnflow.Screen;

import flow.Backstack;
import flow.Flow;

public class FlowFragment extends Fragment implements Flow.Listener {
    private static final LoganParcer<Object> parcer = new LoganParcer<>();
    private FlowBundler bundler;
    private ContainerView container;
    private Flow flow;

    public static void goToScreen(FragmentManager fragmentManager, int fragment_resid, Screen screen) {
        goToScreen(fragmentManager, fragment_resid, screen, true);
    }

    public static void goToScreen(FragmentManager fragmentManager, int fragment_resid, Screen screen, boolean addToBackStack) {
        FlowFragment fragment = (FlowFragment) fragmentManager.findFragmentByTag(FlowFragment.class.getName());
        if (fragment != null) {
            fragment.getFlow().goTo(screen);
        } else {
            fragment = new FlowFragment();
            fragment.setArguments(getScreenBundle(screen));
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragment_resid, fragment, fragment.getClass().getName());
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
    }

    public static Bundle getScreenBundle(Screen screen) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("screen", parcer.wrap(screen));
        return bundle;
    }

    public FlowFragment() {
        bundler = new FlowBundler(parcer, this) {
            @Override
            public Screen createDefault() {
                return (Screen) parcer.unwrap(getArguments().getParcelable("screen"));
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundler.onCreate(savedInstanceState);
        flow = bundler.getFlow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        FlowContext context = new FlowContext(inflater.getContext(), flow);
        container = new ContainerView(context);
        flow.resetTo(flow.getBackstack().current().getScreen());
        return container;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        bundler.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void go(Backstack nextBackstack, Flow.Direction direction, Flow.Callback callback) {
        Screen screen = (Screen) nextBackstack.current().getScreen();
        container.showScreen(screen);
        callback.onComplete(); // call after animations
    }

    public Flow getFlow() {
        return flow;
    }

    public boolean popBackStack() {
        return flow.goBack();
    }
}
