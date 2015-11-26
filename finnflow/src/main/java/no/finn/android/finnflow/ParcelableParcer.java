package no.finn.android.finnflow;

import android.os.Parcelable;

public class ParcelableParcer implements flow.StateParceler {
    @Override
    public Parcelable wrap(Object instance) {
        return (Parcelable) instance;
    }

    @Override
    public Object unwrap(Parcelable parcelable) {
        return parcelable;
    }
}
