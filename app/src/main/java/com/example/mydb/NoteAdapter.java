package com.example.mydb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteHolder> {

    private OnItemClickListener onItemClickListener;

    public NoteAdapter(OnItemClickListener onItemClickListener){
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item,parent,false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.title.setText(currentNote.getTitle());
        holder.description.setText(currentNote.getDescription());
        holder.date.setText(currentNote.getDate());
    }


    public Note getNoteAt(int position){
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView description;
        private TextView date;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            description = itemView.findViewById(R.id.text_view_description);
            date = itemView.findViewById(R.id.text_view_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(onItemClickListener != null && position != RecyclerView.NO_POSITION){
                        onItemClickListener.onItemClick(getItem(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if(onItemClickListener != null && position != RecyclerView.NO_POSITION){
                        onItemClickListener.onItemLongClick(getItem(position));
                    }
                    return false;
                }
            });
        }
    }

}

interface OnItemClickListener {
    void onItemClick(Note note);
    void onItemLongClick(Note note);
}

