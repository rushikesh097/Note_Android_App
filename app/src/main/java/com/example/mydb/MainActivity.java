package com.example.mydb;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnItemClickListener{
    private NoteViewModel noteViewModel;

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private boolean flag = false;

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && !Objects.requireNonNull(result.getData()).hasExtra(AddEditNoteActivity.EXTRA_ID)) {
                        Intent data = result.getData();
                        String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                        String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                        int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);
                        String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);

                        Note note = new Note(title,description,priority,date);
                        noteViewModel.insert(note);

                        Toast.makeText(MainActivity.this, "Note Saved !", Toast.LENGTH_SHORT).show();
                    }
                    else if(result.getData() != null) {

                        Intent data = result.getData();
                        int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
                        String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                        String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                        int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);
                        String date = data.getStringExtra(AddEditNoteActivity.EXTRA_DATE);


                        if(id == -1){
                            Toast.makeText(MainActivity.this, "Not can't be updated !", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Note note = new Note(title,description,priority,date);
                        note.setId(id);
                        noteViewModel.update(note);

                        Toast.makeText(MainActivity.this, "Note Updated !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Note Not Saved !", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.button_add_note);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            launchSomeActivity.launch(intent);
        });

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView = findViewById(R.id.recycler_view);
        changeLayout();
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter(this);
        recyclerView.setAdapter(noteAdapter);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {
            //update RecyclerView
            noteAdapter.submitList(notes);
            changeLayout();
            changeLayout();
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted !", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                new AlertDialogMessage(noteViewModel,this)
                        .alertDialogOptionAll();
                return true;
            case R.id.layout_change:
                if(flag){
                    changeLayout();
                    item.setIcon(R.drawable.ic_baseline_grid_on_24);
                    item.setTitle("Grid Layout");
                }
                else{
                    changeLayout();
                    item.setIcon(R.drawable.ic_baseline_view_list_24);
                    item.setTitle("Linear Layout");
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeLayout(){
        if(flag){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            flag = false;
        }
        else{
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            flag = true;
        }

    }

    @Override
    public void onItemClick(Note note) {
        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
        intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
        intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
        launchSomeActivity.launch(intent);
    }


    @Override
    public void onItemLongClick(Note note) {
        AlertDialogMessage alertDialogMessage  = new AlertDialogMessage(noteViewModel,this);
        alertDialogMessage.setNote(note);
        alertDialogMessage.alertDialogOptionOne();
    }
}