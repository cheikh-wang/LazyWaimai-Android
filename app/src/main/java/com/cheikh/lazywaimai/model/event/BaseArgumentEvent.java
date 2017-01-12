package com.cheikh.lazywaimai.model.event;

class BaseArgumentEvent<T> extends UiCausedEvent {

    protected final T arg;

    protected BaseArgumentEvent(int callingId, T arg) {
        super(callingId);
        this.arg = arg;
    }
}