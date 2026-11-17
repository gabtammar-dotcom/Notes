package com.example.notes;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoteActivity extends AppCompatActivity {

    private Spinner spinnerNotes;
    private Button btnDeleteNote;
    private TextView txtEmptyNotes;
    private NoteStorage noteStorage;

    private final List<Note> notes = new ArrayList<>();
    private ArrayAdapter<Note> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_note);

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

        spinnerNotes = findViewById(R.id.spinnerNotes);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);
        txtEmptyNotes = findViewById(R.id.txtEmptyNotes);
        noteStorage = new NoteStorage(this);

        noteAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                notes
        );
        noteAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerNotes.setAdapter(noteAdapter);

        btnDeleteNote.setOnClickListener(view -> deleteSelectedNote());
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
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

            boolean hasNotes = !notes.isEmpty();
            spinnerNotes.setVisibility(hasNotes ? View.VISIBLE : View.GONE);
            txtEmptyNotes.setVisibility(hasNotes ? View.GONE : View.VISIBLE);
            btnDeleteNote.setEnabled(hasNotes);
        } catch (JSONException exception) {
            btnDeleteNote.setEnabled(false);
            Toast.makeText(this, R.string.load_error, Toast.LENGTH_LONG).show();
        }
    }

    private void deleteSelectedNote() {
        int position = spinnerNotes.getSelectedItemPosition();

        if (position < 0 || position >= notes.size()) {
            return;
        }

        Note selectedNote = notes.get(position);

        try {
            if (noteStorage.deleteNote(selectedNote.getId())) {
                btnDeleteNote.setEnabled(false);
                Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.storage_error, Toast.LENGTH_LONG).show();
                loadNotes();
            }
        } catch (JSONException exception) {
            Toast.makeText(this, R.string.load_error, Toast.LENGTH_LONG).show();
        }
    }
}