package com.example.gooddayplanner;

public class Event {
    private long id;
    private String name;
    private long date;
    private long start;
    private long end;
    private String description;
    private String location;

    public Event() {
        this.id = -1;
        this.name = "";
        this.date = 0;
        this.start = 0;
        this.end = 0;
        this.description = "";
        this.location = "";
    }

    public Event(long id, String name, long date , long start, long end, String location) {
        if (id >= 0) {
            this.id = id;
        } else {
            this.id = -1;
        }
        this.name = name;
        if (date >= 0) {
            this.date = date;
        } else {
            this.date = 0;
        }
        if (start >= 0) {
            this.start = start;
        } else {
            this.id = 0;
        }
        if (end >= 0) {
            this.end = end;
        } else {
            this.end = 0;
        }
        this.location = location;
    }

    public Event(long id, String name, long date , long start, long end,String location, String description) {
        if (id >= 0) {
            this.id = id;
        } else {
            this.id = -1;
        }
        this.name = name;
        if (date >= 0) {
            this.date = date;
        } else {
            this.date = 0;
        }
        if (start >= 0) {
            this.start = start;
        } else {
            this.id = 0;
        }
        if (end >= 0) {
            this.end = end;
        } else {
            this.end = 0;
        }
        this.location = location;
        this.description = description;
    }

    public Event(String name, long date , long start, long end, String location) {
        this.id = -1;
        this.name = name;
        if (date >= 0) {
            this.date = date;
        } else {
            this.date = 0;
        }
        if (start >= 0) {
            this.start = start;
        } else {
            this.id = 0;
        }
        if (end >= 0) {
            this.end = end;
        } else {
            this.end = 0;
        }
        this.location = location;
    }

    public Event(String name, long date, long start , long end,String location, String description) {
        this.id = -1;
        this.name = name;
        if (date >= 0) {
            this.date = date;
        } else {
            this.date = 0;
        }
        if (start >= 0) {
            this.start = start;
        } else {
            this.id = 0;
        }
        if (end >= 0) {
            this.end = end;
        } else {
            this.end = 0;
        }
        this.location = location;
        this.description = description;
    }

    public long getID() {
        return this.id;
    }

    public void setID(long id) {
        if (id >= 0) {
            this.id = id;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
