package no.finn.finnflow.sample.detail;

import android.os.AsyncTask;

import no.finn.finnflow.ViewPresenter;

public class DetailPresenter extends ViewPresenter<DetailView, DetailScreen> {

    /* You have a choice between sticking networkresponses here (will be kept on rotate, but will not be serialized if
    you leave the app and it get's GC'd) or in the screen as a @JsonField, in which case it will be serialized.*/

    private String response = null;

    protected DetailPresenter(DetailScreen screen) {
        super(screen);
        /* We can safely start this networkrequest before the view has been created. This allows cool things like
        loading data while running "screen switch" animations. If the load is completed before the animation is
        done the user will never see any loading.*/
        exampleNetworkRequest();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (response != null) {
            getView().renderNetworkResponse(response);
        }
    }

    private void exampleNetworkRequest() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return "Test response from fake network";
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                DetailPresenter.this.response = response;
                if (getView() != null) {
                    /* if the view isn't attached the user might be rotating for example, once the rotation is
                    completed a new view will be attached through onload. */
                    getView().renderNetworkResponse(response);
                }
            }
        }.execute();
    }


}
