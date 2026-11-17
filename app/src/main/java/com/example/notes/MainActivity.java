package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_ADD_NOTE = 1;
    private static final int MENU_DELETE_NOTE = 2;

    private NoteStorage noteStorage;
    private ArrayAdapter<Note> noteAdapter;
    private final List<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.main);
        int left = root.getPaddingLeft();
        int top = root.getPaddingTop();
        int right = root.getPaddingRight();
        int bottom = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars());

            view.setPadding(
                    left + bars.left,
                    top + bars.top,
                    right + bars.right,
                    bottom + bars.bottom
            );

            return windowInsets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.notes_title);
        toolbar.setTitleTextColor(android.graphics.Color.rgb(30, 58, 95));
        setSupportActionBar(toolbar);

        noteStorage = new NoteStorage(this);

        ListView listNotes = findViewById(R.id.listNotes);
        listNotes.setEmptyView(findViewById(R.id.txtEmptyNotes));

        noteAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                notes
        );
        listNotes.setAdapter(noteAdapter);

        listNotes.setOnItemClickListener((parent, view, position, id) -> {
            Note note = notes.get(position);

            new AlertDialog.Builder(this)
                    .setTitle(note.getName())
                    .setMessage(note.getContent())
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        try {
            List<Note> savedNotes = noteStorage.loadNotes();
            notes.clear();
            notes.addAll(savedNotes);
            noteAdapter.notifyDataSetChanged();
        } catch (JSONException exception) {
            Toast.makeText(this, R.string.load_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ADD_NOTE, Menu.NONE, R.string.create_note)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        menu.add(Menu.NONE, MENU_DELETE_NOTE, Menu.NONE, R.string.delete_note)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ADD_NOTE) {
            startActivity(new Intent(this, AddNoteActivity.class));
            return true;
        }

        if (item.getItemId() == MENU_DELETE_NOTE) {
            startActivity(new Intent(this, DeleteNoteActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}