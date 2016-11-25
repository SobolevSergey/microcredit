package ru.simplgroupp.webapp.controller;

public interface IEventListener {

    void eventFired(String eventName, Object eventSource) throws Exception;
}
