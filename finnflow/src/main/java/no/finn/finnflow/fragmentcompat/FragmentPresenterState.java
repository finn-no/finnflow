package no.finn.finnflow.fragmentcompat;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class FragmentPresenterState<P extends FragmentViewPresenter> extends StateFragmentLink implements Parcelable {
    private P presenter = null;

    protected abstract P createPresenter();

    public P getPresenter() {
        if (presenter == null) {
            presenter = createPresenter();
        }
        return presenter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // needs to be implemented if the child is an actual state, and not just a link to the fragment.
    }
}
