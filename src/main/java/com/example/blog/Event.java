package com.example.blog;

public class Event {
	private String name;
    private String id;

    public Event(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
