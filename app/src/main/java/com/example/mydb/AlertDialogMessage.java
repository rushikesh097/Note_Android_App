package com.example.mydb;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class AlertDialogMessage {
    private Note note;
    private final NoteViewModel noteViewModel;
    private final Context context;
    private final AlertDialog.Builder builder;

    public AlertDialogMessage(NoteViewModel noteViewModel, Context context) {
        this.noteViewModel = noteViewModel;
        this.context = context;
        builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_round_delete_24);
    }

    public void setNote(Note note) {
        this.note = note;
    }

    protected void alertDialogOptionOne(){
        builder.setMessage("You Really Want to Delete ?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    noteViewModel.delete(note);
                    Toast.makeText(context, "Note Deleted !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                    Toast.makeText(context, "Deletion Canceled !", Toast.LENGTH_SHORT).show();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Delete");
        alertDialog.show();
    }

    protected void alertDialogOptionAll(){
        builder.setMessage("You Really Want to Delete All?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    noteViewModel.deleteAllNotes();
                    Toast.makeText(context, "All Notes Deleted !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                    Toast.makeText(context, "Deletion Canceled !", Toast.LENGTH_SHORT).show();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Delete");
        alertDialog.show();
    }
}
