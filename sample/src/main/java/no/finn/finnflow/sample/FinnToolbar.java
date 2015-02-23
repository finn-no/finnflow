package no.finn.finnflow.sample;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import no.finn.finnflow.PresenterContext;

public class FinnToolbar extends Toolbar {
    public FinnToolbar(Context context) {
        this(context, null);
    }

    public FinnToolbar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PresenterContext.getActivity(context).onBackPressed();
            }
        });
    }
}
