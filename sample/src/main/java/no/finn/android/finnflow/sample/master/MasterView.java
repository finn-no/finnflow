package no.finn.android.finnflow.sample.master;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import no.finn.android.finnflow.PresenterContext;
import no.finn.android.finnflow.sample.R;

public class MasterView extends LinearLayout {
    private final MasterPresenter presenter;

    public MasterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        presenter = PresenterContext.getPresenter(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Button button = (Button) findViewById(R.id.detail_button);
        presenter.takeView(this);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.detailButtonClicked();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView();
        super.onDetachedFromWindow();
    }
}
