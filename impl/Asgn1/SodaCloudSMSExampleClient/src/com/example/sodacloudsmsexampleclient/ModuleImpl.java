package com.example.sodacloudsmsexampleclient;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Richard on 8/25/13.
 */
public class ModuleImpl implements Module {
    private Map<Class<?>, Object> classMap = new HashMap<Class<?>, Object>();

    /**
     *
     * This method returns the component that is bound to a given
     * type.
     *
     * @param type - type type of component to retrieve
     * @return
     */
    public <T> T getComponent(Class<T> type){
        return (T)classMap.get(type);
    }

    /**
     *
     * Bind a component to a type.
     *
     * @param type - the type to bind the component to
     * @param component - the object instance to associate with the type key
     */
    public <T> void setComponent(Class<T> type, T component){
        classMap.put(type, component);
    }
}
