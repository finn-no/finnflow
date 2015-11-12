package no.finn.android.finnflow;

import java.io.IOException;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.LoganSquare;
import flow.StateParceler;

class LoganStateParceler implements StateParceler {
    @Override
    public Parcelable wrap(Object instance) {
        Screen screen = (Screen) instance;
        return new Wrapper(screen);
    }

    @Override
    public Object unwrap(Parcelable parcelable) {
        return ((Wrapper) parcelable).screen;
    }

    private static class Wrapper implements Parcelable {
        private final Screen screen;

        public Wrapper(Screen screen) {
            this.screen = screen;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            try {
                String screenClass = screen.getClass().getName();
                String dialogClass = screen.dialogState != null ? screen.dialogState.getClass().getName() : null;
                String serializedScreen = LoganSquare.serialize(screen);
                String serializedDialog = screen.dialogState != null ? LoganSquare.serialize(screen.dialogState) : null;

                out.writeString(screenClass);
                out.writeString(serializedScreen);
                if (dialogClass != null) {
                    out.writeInt(1);
                    out.writeString(dialogClass);
                    out.writeString(serializedDialog);
                } else {
                    out.writeInt(0);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public static final Parcelable.Creator<Wrapper> CREATOR = new Parcelable.Creator<Wrapper>() {
            @Override
            public Wrapper createFromParcel(Parcel in) {
                try {
                    String screenClass = in.readString();
                    String serializedScreen = in.readString();
                    String dialogClass = null;
                    String serializedDialog = null;
                    if (in.readInt() == 1) {
                        dialogClass = in.readString();
                        serializedDialog = in.readString();
                    }

                    Class<Object> screenType = (Class<Object>) Class.forName(screenClass);
                    Screen screen = (Screen) LoganSquare.parse(serializedScreen, screenType);
                    if (dialogClass != null) {
                        Class<Object> dialogType = (Class<Object>) Class.forName(dialogClass);
                        Screen.DialogState dialogState = (Screen.DialogState) LoganSquare.parse(serializedDialog, dialogType);
                        screen.dialogState = dialogState;
                    }
                    return new Wrapper(screen);
                } catch (IOException | ClassNotFoundException e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            public Wrapper[] newArray(int size) {
                return new Wrapper[size];
            }
        };
    }
}
