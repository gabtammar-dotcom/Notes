package com.example.notes;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

public class AddNoteActivity extends AppCompatActivity {

    private EditText edtNoteName;
    private EditText edtNoteContent;
    private NoteStorage noteStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);

        View root = findViewById(R.id.main);
        int left = root.getPaddingLeft();
        int top = root.getPaddingTop();
        int right = root.getPaddingRight();
        int bottom = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.ime());

            view.setPadding(
                    left + insets.left,
                    top + insets.top,
                    right + insets.right,
                    bottom + insets.bottom
            );

            return windowInsets;
        });

        edtNoteName = findViewById(R.id.edtNoteName);
        edtNoteContent = findViewById(R.id.edtNoteContent);
        noteStorage = new NoteStorage(this);

        findViewById(R.id.btnSaveNote).setOnClickListener(view -> saveNote());
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
    }

    private void saveNote() {
        String name = edtNoteName.getText().toString().trim();
        String content = edtNoteContent.getText().toString().trim();

        if (name.isEmpty()) {
            edtNoteName.setError(getString(R.string.required_name));
            edtNoteName.requestFocus();
            return;
        }

        if (content.isEmpty()) {
            edtNoteContent.setError(getString(R.string.required_content));
            edtNoteContent.requestFocus();
            return;
        }

        try {
            if (noteStorage.addNote(name, content)) {
                findViewById(R.id.btnSaveNote).setEnabled(false);
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.storage_error, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException exception) {
            Toast.makeText(this, R.string.load_error, Toast.LENGTH_LONG).show();
        }
    }
}