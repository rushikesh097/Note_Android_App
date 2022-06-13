package com.example.mydb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteHolder> {

    private OnItemClickListener onItemClickListener;
    private TextView addText;

    public NoteAdapter(OnItemClickListener onItemClickListener, TextView addText){
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
        this.addText = addText;
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
                    oldItem.getPriority() == newItem.getPriority() &&
                    oldItem.getDate().equals(newItem.getDate());
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
        if(getItemCount() > 0){
            addText.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected Note getItem(int position) {
        return super.getItem(position);
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

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(onItemClickListener != null && position != RecyclerView.NO_POSITION){
                    onItemClickListener.onItemClick(getItem(position));
                }
            });

            itemView.setOnLongClickListener(view -> {
                int position = getAdapterPosition();
                if(onItemClickListener != null && position != RecyclerView.NO_POSITION){
                    onItemClickListener.onItemLongClick(getItem(position),view);
                }
                return false;
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
        void onItemLongClick(Note note, View view);
    }

}


