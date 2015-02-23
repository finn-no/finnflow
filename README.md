# FINNFlow

FINNFlow is FINN's additions to square's flow. It's built on top of flow, and adds whatever we've needed.

**Warning 1** : This is currently fairly hackish, and will remain so until square releases flow 0.9. There are some API breaking changes in there, and we're not going to clean this up until then. At that point we'll also consider PR's to get any features we need back into flow.

**Warning 2** : The sample does not do things in an optimal way. It's a demo of how we currently use flow to keep a flow back stack inside a fragment. This allows migration of part of the app, rather than doing the entire thing in one go. It's considerably better to not use any of the compat packages once migration is complete!

What this package adds on to of flow:
* A mortar like Presenter pattern without using dagger 1x injection. (Which we thought was easier than mortar and blueprints.).
* A "Screen" that can be serialized with dialog information. (remember both dialog state and whether they were open). This can also be serialized easily, so packing an intent from a notification that opens a dialog with a preset state is a few lines of code.
* SharedScreen concept that allows moving part of a screen to another screen. (Allows shared elements in Master/Detail layouts to basically be the same element)
* fragmentcompat package that adds a lot of compat code we used during our migration to flow.

Why we use flow:
* We used to have a ton of requests triggered onResume and canceled onPause, with presenters we don't need that. The presenter will stay alive aslong as the network requests are.
* We got rid of a TON of async logic (is the activity there? is the view ready? Is the actionbar ready?). Everything in the view is now ready after onFinishInflate.
* No problems with sync logic between fragments, we often needed to keep scroll positions of child layouts and such.
* Serializing state from notifications is a ton easier. We used to have logic in a lot of places to restore fragment state / reopen dialogs from notifications and such (if you send a message through a dialog and the message fails to send we want to reopen the dialog with the message).
* Ever tried doing cool animations when you have multiple childfragments? Ever tried crossing data from one fragmentmanager to another?...

### Our flow migration

#### Step 1
Migrate most of our fragments to be extremtly minimal. Most of our fragments looked like this:
```
public class ExampleFragment extends Fragment {
    private FragmentStateObject state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            state = new ObjectPageState(adId, adLink, contactLink, contactMessage, verificationState, verificationUrl, phoneNumber);
        } else {
            state = savedInstanceState.getParcelable("STATE");
        }
        state.takeFragment(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("STATE", state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PresenterContext<ObjectPagePresenter> context = new PresenterContext<>(inflater.getContext(), state.getPresenter());
        return LayoutInflater.from(context).inflate(R.layout.layout, container, false);
    }

    @Override
    public void onDestroy() {
        state.dropFragment();
        super.onDestroy();
    }
}
```
This allowed us to use the ObjectPageState basically as a flow screen, and it allowed us to move to a presenter like pattern, so we could seperate logic out of the fragments in the same step.

#### Step 2
We didn't want to migrate the "root" fragment/activity right away. That was too much work. Instead of that we created several fragments under the main activity and migrated them one at the time. This allowed us to use fragment back logic everywhere, except from the branches of the app where we had a flow implemented. For this see FlowFragment.

#### Step 3
Tear out all our FlowFragments and use a flow in the activity instead.
