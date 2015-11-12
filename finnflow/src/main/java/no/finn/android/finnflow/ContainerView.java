package no.finn.android.finnflow;

import java.io.IOException;

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.bluelinelabs.logansquare.LoganSquare;
import flow.Flow;
import flow.ViewState;

public class ContainerView extends FrameLayout {
    private final FinnFlow finnFlow;

    public ContainerView(Context context, FinnFlow finnFlow) {
        super(context);
        this.finnFlow = finnFlow;
        setSaveEnabled(false);
        setSaveFromParentEnabled(false);
    }

    public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
        final Screen originScreen = traversal.origin.top();
        final Screen destinationScreen = traversal.destination.top();

        if (originScreen == destinationScreen && getChildCount() == 1) {
            // we're moving to the same screen we're on and we've already added the view - shortciruit.
            // (can happen if we modify the back stack to still be on the same page we're on)
            callback.onTraversalCompleted();
            return;
        }

        saveState(originScreen, traversal.origin.currentViewState());

        if (originScreen instanceof SharedScreen && destinationScreen instanceof SharedScreen) {
            removeAllViews();
            ((SharedScreen) originScreen).moveTo((SharedScreen) destinationScreen);
            addNewScreen(destinationScreen, traversal.destination.currentViewState());
        } else {
            removeAllViews();
            addNewScreen(destinationScreen, traversal.destination.currentViewState());
        }
        callback.onTraversalCompleted();
    }

    private void saveState(Screen oldScreen, ViewState viewState) {
        if (oldScreen != null && oldScreen.getPresenter().getView() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT <= 22) {
                requestFocus(); // @hack : improves (but does not fix) memory leak https://code.google.com/p/android/issues/detail?id=171190
            }
            View view = getChildAt(0);
            if (view != null) {
                hideKeyboard(view);
                if (oldScreen.saveViewState()) {
                    viewState.save(view);
                }
            }
            if (finnFlow.isDebugBuild()) {
                try {
                    LoganSquare.serialize(oldScreen);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to serialize " + oldScreen + " did you forget @JsonObject?...");
                }
            }
        }
    }

    View addNewScreen(Screen newScreen, ViewState viewState) {
        View view = newScreen.createView(this);
        addView(view, 0);
        if (newScreen.saveViewState()) {
            viewState.restore(view);
        }
        return view;
    }

    @Override
    public void restoreHierarchyState(SparseArray<Parcelable> container) {

    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}