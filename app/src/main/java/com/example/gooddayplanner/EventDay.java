package com.example.gooddayplanner;

public class EventDay {

    private String eventName;
    private String eventTime;
    private String eventLocation;
    private String eventDecs;

    public EventDay(String eName,String eTime,String eLocation,String eDecs){
        eventName = eName;
        eventTime = eTime;
        eventLocation = eLocation;
        eventDecs = eDecs;
    }

    public void setLocation(String location) {
        eventLocation = location;
    }

    public void setEventDecs(String eventDecs) {
        this.eventDecs = eventDecs;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getLocation() {
        return eventLocation;
    }

    public String getEventDecs() {
        return eventDecs;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventTime() {
        return eventTime;
    }
}
