package com.example.mydb;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.internal.ManufacturerUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.mydb.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.mydb.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.mydb.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.mydb.EXTRA_PRIORITY";
    public static final String EXTRA_DATE = "com.example.mydb.EXTRA_DATE";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private static int PRIORITY = 10;
    private String date;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        }
        else{
            setTitle("Add Note");
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("HH:mm");
        date = localDateTime.getDayOfMonth()+" "+localDateTime.getMonth().toString().substring(0,3)+" "+ftf.format(localDateTime);
    }

    private void saveNote(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if(title.trim().isEmpty()){
            Toast.makeText(this, "Please Insert A Title", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE,title);
        intent.putExtra(EXTRA_DESCRIPTION,description);
        intent.putExtra(EXTRA_PRIORITY,PRIORITY);
        intent.putExtra(EXTRA_DATE,date);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if(id != -1){
             intent.putExtra(EXTRA_ID,id);
            setResult(RESULT_OK,intent);
            finish();
        }
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}