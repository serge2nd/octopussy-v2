package ru.serge2nd.octopussy.service.ex;

import lombok.Getter;

public class GenericTrackException extends RuntimeException {
    private final @Getter String trackId;

    public static Closed   errTrackClosed(String id)   { return new Closed(id); }
    public static Exists   errTrackExists(String id)   { return new Exists(id); }
    public static NotFound errTrackNotFound(String id) { return new NotFound(id); }

    public GenericTrackException(String msg, String trackId) {
        super(msg.replace(ID, trackId));
        this.trackId = trackId;
    }

    public static class Closed extends GenericTrackException {
        public Closed(String id) { super("the track " + ID + " is closed", id); }
    }
    public static class Exists extends GenericTrackException {
        public Exists(String id) { super("the track " + ID + " already exists", id); }
    }
    public static class NotFound extends GenericTrackException {
        public NotFound(String id) { super("the track " + ID + " not found", id); }
    }

    static final String ID = "#";
}
