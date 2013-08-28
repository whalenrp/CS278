package com.example.sodacloudsmsexampleclient;

import org.magnum.soda.proxy.ObjRef;

/**
 * Created by Richard on 8/25/13.
 */
public class ExternalObjRefImpl implements ExternalObjRef {
    private ObjRef objRef;
    private String pubSubHost;

    public ExternalObjRefImpl(ObjRef objRef, String pubSubHost){
        this.objRef = objRef;
        this.pubSubHost = pubSubHost;
    }
    public ObjRef getObjRef(){
        return objRef;
    }

    public String getPubSubHost(){
        return pubSubHost;
    }

    /**
     * The toString() implementation should return
     * a String in the following format:
     *
     * getPubSubHost()+"|"+getObjRef().getUri()
     *
     *
     * @return
     */
    public String toString(){
        return getPubSubHost()+"|"+getObjRef().getUri();
    }
}
