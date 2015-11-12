package no.finn.android.finnflow.sample.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import no.finn.android.finnflow.PresenterContext;
import no.finn.android.finnflow.sample.R;

public class DetailView extends LinearLayout {
    private final DetailPresenter presenter;
    private TextView networkResponse;

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        presenter = PresenterContext.getPresenter(context);
    }

    @Override
    protected void onFinishInflate() {
        networkResponse = (TextView) findViewById(R.id.network_response);
        super.onFinishInflate();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView();
        super.onDetachedFromWindow();
    }

    public void renderNetworkResponse(String response) {
        networkResponse.setText(response);
    }
}
