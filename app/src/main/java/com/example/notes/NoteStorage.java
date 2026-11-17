package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteStorage {

    private static final String PREFERENCES_NAME = "notes_storage";
    private static final String NOTES_KEY = "notes";

    private final SharedPreferences preferences;

    public NoteStorage(Context context) {
        preferences = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public List<Note> loadNotes() throws JSONException {
        String savedNotes = preferences.getString(NOTES_KEY, "[]");
        JSONArray noteArray = new JSONArray(savedNotes);
        List<Note> notes = new ArrayList<>();

        for (int index = 0; index < noteArray.length(); index++) {
            JSONObject noteObject = noteArray.getJSONObject(index);

            notes.add(new Note(
                    noteObject.getString("id"),
                    noteObject.getString("name"),
                    noteObject.getString("content")
            ));
        }

        return notes;
    }

    public boolean addNote(String name, String content) throws JSONException {
        List<Note> notes = loadNotes();
        notes.add(new Note(UUID.randomUUID().toString(), name, content));
        return saveNotes(notes);
    }

    public boolean deleteNote(String noteId) throws JSONException {
        List<Note> notes = loadNotes();

        for (int index = 0; index < notes.size(); index++) {
            if (notes.get(index).getId().equals(noteId)) {
                notes.remove(index);
                return saveNotes(notes);
            }
        }

        return false;
    }

    private boolean saveNotes(List<Note> notes) throws JSONException {
        JSONArray noteArray = new JSONArray();

        for (Note note : notes) {
            JSONObject noteObject = new JSONObject();
            noteObject.put("id", note.getId());
            noteObject.put("name", note.getName());
            noteObject.put("content", note.getContent());
            noteArray.put(noteObject);
        }

        return preferences.edit()
                .putString(NOTES_KEY, noteArray.toString())
                .commit();
    }
}