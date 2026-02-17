package com.desyd.api.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SessionParticipantId implements Serializable {

    private UUID session;
    private UUID user;

    public SessionParticipantId(){}

    public SessionParticipantId(UUID session, UUID user) {
        this.session = session;
        this.user = user;
    }

    public UUID getSession() {
        return session;
    }

    public void setSession(UUID session) {
        this.session = session;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SessionParticipantId that = (SessionParticipantId) o;
        return Objects.equals(session, that.session) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, user);
    }
}
