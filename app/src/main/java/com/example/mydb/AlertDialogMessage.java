package com.example.mydb;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AlertDialogMessage {
    private Note note;
    private final NoteViewModel noteViewModel;
    private final Context context;
    private Dialog dialog;

    public AlertDialogMessage(NoteViewModel noteViewModel, Context context) {
        this.noteViewModel = noteViewModel;
        this.context = context;
    }

    public void setDialog(String text){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_layout);
        TextView message = dialog.findViewById(R.id.message);
        Button yes = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);
        message.setText(text);
        switch (text){
            case "You Really Want to Delete ?":
                yes.setOnClickListener(view -> {
                    noteViewModel.delete(note);
                    Toast.makeText(context, "Note Deleted !", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                });
                no.setOnClickListener(view -> dialog.cancel());
                break;
            case "You Really Want to Delete All?":
                yes.setOnClickListener(view -> {
                    noteViewModel.deleteAllNotes();
                    Toast.makeText(context, "All Notes Deleted !", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                });
                no.setOnClickListener(view -> dialog.cancel());
                break;
        }
        dialog.show();
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
