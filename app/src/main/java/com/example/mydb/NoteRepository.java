package com.example.mydb;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note){
        noteDao.insert(note);
    }

    public void update(Note note){
        noteDao.update(note);
    }

    public void delete(Note note){
        noteDao.delete(note);
    }

    public void deleteAllNotes(){
        noteDao.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

}
