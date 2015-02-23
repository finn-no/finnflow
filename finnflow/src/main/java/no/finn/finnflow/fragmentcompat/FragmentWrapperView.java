package no.finn.finnflow.fragmentcompat;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import no.finn.finnflow.PresenterContext;

public abstract class FragmentWrapperView extends FrameLayout {

    private final int fragmentResourceId;
    private Fragment fragment;

    public FragmentWrapperView(Context context, AttributeSet attrs, int fragmentResourceId) {
        super(context, attrs);
        this.fragmentResourceId = fragmentResourceId;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FragmentActivity activity = PresenterContext.getActivity(getContext());
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragment = createFragment();
        Fragment oldFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragmentResourceId, fragment, fragment.getClass().getName());
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onDetachedFromWindow() {
        FlowRootFragmentActivity activity = (FlowRootFragmentActivity) PresenterContext.getActivity(getContext());
        if (!activity.isDestroying()) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
        fragment = null;
        super.onDetachedFromWindow();
    }

    public abstract Fragment createFragment();
}
