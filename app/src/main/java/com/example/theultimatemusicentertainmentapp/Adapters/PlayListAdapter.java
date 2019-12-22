package com.example.theultimatemusicentertainmentapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theultimatemusicentertainmentapp.Model.Audio;
import com.example.theultimatemusicentertainmentapp.Model.PlayList;
import com.example.theultimatemusicentertainmentapp.R;

import java.util.List;

public class PlayListAdapter  extends  RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {
    private Context cx;
    private List<PlayList> playList;


    private OnSongClickListener mOnSongClickListener;


    public PlayListAdapter(Context cx,List<PlayList> audioList,OnSongClickListener onSongClickListener){
        this.cx = cx;
        this.playList = audioList;
        this.mOnSongClickListener = onSongClickListener;
    }


    //-----
    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(cx);
        View view =  inflator.inflate(R.layout.recyclerview_list_row,null);
        PlayListAdapter.PlayListViewHolder holder = new PlayListAdapter.PlayListViewHolder(view,mOnSongClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListAdapter.PlayListViewHolder holder, int position) {
        PlayList playlist = playList.get(position);
        holder.txtId.setText(playlist.getPlayListName());
        holder.txtTite.setText(playlist.getPlayListName().toUpperCase().substring(0,1));
        holder.txtName.setText("");
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }
    //-----




    class PlayListViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtId,txtName,txtTite;
        OnSongClickListener onSongClickListener;

        public PlayListViewHolder(@NonNull View itemView, PlayListAdapter.OnSongClickListener onSongClickListener)
        {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtSongId);
            txtName = itemView.findViewById(R.id.txtSongName);
            txtTite = itemView.findViewById(R.id.txtTitle);
            this.onSongClickListener = onSongClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSongClickListener.onSongClick(getAdapterPosition());
        }
    }

    public  interface OnSongClickListener{
        void onSongClick(int position);
    }
}
