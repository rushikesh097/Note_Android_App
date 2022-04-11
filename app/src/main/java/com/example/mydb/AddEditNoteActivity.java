package com.example.mydb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.mydb.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.mydb.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.mydb.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.mydb.EXTRA_PRIORITY";
    public static final String EXTRA_DATE = "com.example.mydb.EXTRA_DATE";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private String date;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ImageView save = findViewById(R.id.save_note);
        ImageView close = findViewById(R.id.back);
        TextView name = findViewById(R.id.header_title);
        ImageView send = findViewById(R.id.share_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            name.setText("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        }
        else{
            name.setText("Add Note");
        }

        save.setOnClickListener(view -> saveNote());

        close.setOnClickListener(view -> finish());

        send.setOnClickListener(view -> {
            if(editTextTitle.getText().toString().length()!=0){
                shareNote(editTextTitle.getText().toString()+"\n"+editTextDescription.getText().toString());
            }
            else {
                Toast.makeText(AddEditNoteActivity.this, "Add Title and Text", Toast.LENGTH_SHORT).show();
            }
        });

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
        int PRIORITY = 10;
        intent.putExtra(EXTRA_PRIORITY, PRIORITY);
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

    private void shareNote(String note)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT,note)
                .setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share Note as Text");
        startActivity(shareIntent);
    }

}