# FINNFlow

FINNFlow is FINN's additions to square's flow. It's built on top of flow, and adds whatever we've needed.

**Warning** : The sample does not do things in an optimal way. It's a demo of how we currently use flow to keep a flow back stack inside a fragment. This allows migration of part of the app, rather than doing the entire thing in one go. It's considerably better to not use any of the compat packages once migration is complete!

What this package adds on to of flow:
* A mortar like Presenter pattern without using dagger 1x injection. (Which we thought was easier than mortar and blueprints.).
* A "Screen" that can be serialized with dialog information. (remember both dialog state and whether they were open). This can also be serialized easily, so packing an intent from a notification that opens a dialog with a preset state is a few lines of code.
* SharedScreen concept that allows moving part of a screen to another screen. (Allows shared elements in Master/Detail layouts to basically be the same element)

Why we use flow:
* We used to have a ton of requests triggered onResume and canceled onPause, with presenters we don't need that. The presenter will stay alive aslong as the network requests are.
* We got rid of a TON of async logic (is the activity there? is the view ready? Is the actionbar ready?). Everything in the view is now ready after onFinishInflate.
* No problems with sync logic between fragments, we often needed to keep scroll positions of child layouts and such.
* Serializing state from notifications is a ton easier. We used to have logic in a lot of places to restore fragment state / reopen dialogs from notifications and such (if you send a message through a dialog and the message fails to send we want to reopen the dialog with the message).
* Ever tried doing cool animations when you have multiple childfragments? Ever tried crossing data from one fragmentmanager to another?...
