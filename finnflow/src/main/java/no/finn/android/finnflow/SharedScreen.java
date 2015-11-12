package no.finn.android.finnflow;

public interface SharedScreen {
    // screen has shared views with another view
    void moveTo(SharedScreen other);
}
