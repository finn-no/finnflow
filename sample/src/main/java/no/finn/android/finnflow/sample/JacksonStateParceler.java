package no.finn.android.finnflow.sample;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

import flow.StateParceler;
import no.finn.android.finnflow.Screen;

class JacksonStateParceler implements StateParceler {
    private static ObjectMapper objectMapper = new ObjectMapper() {{
        enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        disable(MapperFeature.AUTO_DETECT_CREATORS,
                MapperFeature.AUTO_DETECT_FIELDS,
                MapperFeature.AUTO_DETECT_GETTERS,
                MapperFeature.AUTO_DETECT_IS_GETTERS);
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }};

    private static StateParceler INSTANCE = new JacksonStateParceler();

    public static StateParceler instance() {
        return INSTANCE;
    }

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
                String serializedScreen = objectMapper.writeValueAsString(screen);
                String serializedDialog = screen.dialogState != null ? objectMapper.writeValueAsString(screen.dialogState) : null;

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

        public static final Creator<Wrapper> CREATOR = new Creator<Wrapper>() {
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
                    Screen screen = objectMapper.readerFor(screenType).readValue(serializedScreen);
                    if (dialogClass != null) {
                        Class<Object> dialogType = (Class<Object>) Class.forName(dialogClass);
                        Screen.DialogState dialogState = objectMapper.readerFor(dialogType).readValue(serializedDialog);
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
