package com.example.notes;

public class Note {

    private final String id;
    private final String name;
    private final String content;

    public Note(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return name;
    }
}