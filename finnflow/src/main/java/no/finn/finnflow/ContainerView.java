package no.finn.finnflow;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

public class ContainerView extends FrameLayout {
    private Screen currentScreen = null;

    public ContainerView(FlowContext context) {
        super(context);
        setSaveEnabled(false);
        setSaveFromParentEnabled(false);
    }

    public void showScreen(Screen newScreen) {
        Screen oldScreen = currentScreen;
        if (oldScreen != null) {
            View view = getChildAt(0);
            SparseArray<Parcelable> viewState = new SparseArray<>();
            view.saveHierarchyState(viewState);
            if (oldScreen.saveViewState()) {
                oldScreen.setViewState(viewState);
            }
        }
        removeAllViews();

        if (oldScreen instanceof SharedScreen && newScreen instanceof SharedScreen) {
            ((SharedScreen) oldScreen).moveTo((SharedScreen) newScreen);
        }

        View view = newScreen.createView(this);
        addView(view);
        if (newScreen.getViewState() != null && newScreen.saveViewState()) {
            view.restoreHierarchyState(newScreen.getViewState());
        }

        this.currentScreen = newScreen;
    }

    @Override
    public void restoreHierarchyState(SparseArray<Parcelable> container) {

    }
}