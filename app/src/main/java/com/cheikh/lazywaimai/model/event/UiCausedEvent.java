package com.cheikh.lazywaimai.model.event;
/**
 * author：cheikh.wang on 16/1/10 12:01
 * email：wanghonghi@126.com
 */
public class UiCausedEvent {

    public final int callingId;

    public UiCausedEvent(int callingId) {
        this.callingId = callingId;
    }
}