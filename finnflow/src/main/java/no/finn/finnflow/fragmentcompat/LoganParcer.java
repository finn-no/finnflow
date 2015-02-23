package no.finn.finnflow.fragmentcompat;

import java.io.IOException;

import android.os.Parcel;
import android.os.Parcelable;

import no.finn.finnflow.Screen;

import com.bluelinelabs.logansquare.LoganSquare;
import flow.Parcer;

class LoganParcer<T> implements Parcer<T> {
    public LoganParcer() {
    }

    @Override
    public Parcelable wrap(T t) {
        try {
            Screen screen = (Screen) t;
            String screenClass = screen.getClass().getName();
            String dialogClass = screen.dialogState != null ? screen.dialogState.getClass().getName() : null;
            String serializedScreen = LoganSquare.serialize(screen);
            String serializedDialog = screen.dialogState != null ? LoganSquare.serialize(screen.dialogState) : null;
            return new Wrapper(screenClass, serializedScreen, dialogClass, serializedDialog);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public T unwrap(Parcelable parcelable) {
        Wrapper wrapper = (Wrapper) parcelable;
        try {
            Class<T> screenType = (Class<T>) Class.forName(wrapper.screenClass);
            Screen screen = (Screen) LoganSquare.parse(wrapper.serializedScreen, screenType);
            if (wrapper.dialogClass != null) {
                Class<T> dialogType = (Class<T>) Class.forName(wrapper.dialogClass);
                Screen.DialogState dialogState = (Screen.DialogState) LoganSquare.parse(wrapper.serializedDialog, dialogType);
                screen.dialogState = dialogState;
            }
            return (T) screen;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static class Wrapper implements Parcelable {
        private final String screenClass;
        private final String serializedScreen;
        private final String dialogClass;
        private final String serializedDialog;

        public Wrapper(String screenClass, String serializedScreen, String dialogClass, String serializedDialog) {
            this.screenClass = screenClass;
            this.serializedScreen = serializedScreen;
            this.dialogClass = dialogClass;
            this.serializedDialog = serializedDialog;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(screenClass);
            out.writeString(serializedScreen);
            if (dialogClass != null) {
                out.writeInt(1);
                out.writeString(dialogClass);
                out.writeString(serializedDialog);
            } else {
                out.writeInt(0);
            }
        }

        public static final Creator<Wrapper> CREATOR = new Creator<Wrapper>() {
            @Override
            public Wrapper createFromParcel(Parcel in) {
                String screenClass = in.readString();
                String serializedScreen = in.readString();
                String dialogClass = null;
                String serializedDialog = null;
                if (in.readInt() == 1) {
                    dialogClass = in.readString();
                    serializedDialog = in.readString();
                }
                return new Wrapper(screenClass, serializedScreen, dialogClass, serializedDialog);
            }

            @Override
            public Wrapper[] newArray(int size) {
                return new Wrapper[size];
            }
        };
    }
}
