package no.finn.finnflow;

public interface SharedScreen {
    /**
    This interface allows screens to share elements. For example if you have a master
     detail flow the master can move one of it's subviews directly over to the detail (and back).
     This lets you keep scroll positions and such in sync very easily, as it's the same state.
     */
    void moveTo(SharedScreen other);
}
