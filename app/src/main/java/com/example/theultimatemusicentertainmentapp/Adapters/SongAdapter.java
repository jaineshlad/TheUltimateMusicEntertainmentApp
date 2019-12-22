package com.example.theultimatemusicentertainmentapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theultimatemusicentertainmentapp.Model.Audio;
import com.example.theultimatemusicentertainmentapp.R;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends  RecyclerView.Adapter<SongAdapter.SongViewHolder>{


    private Context cx;
    private List<Audio> audioList;


    private  OnSongClickListener mOnSongClickListener;

    public SongAdapter(Context cx,List<Audio> audioList,OnSongClickListener onSongClickListener){
        this.cx = cx;
        this.audioList = audioList;
        this.mOnSongClickListener = onSongClickListener;
    }


    //get custom view for recycler view

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(cx);
        View view =  inflator.inflate(R.layout.recyclerview_list_row,null);
        SongViewHolder holder = new SongViewHolder(view,mOnSongClickListener);
        return holder;
    }


    //bind custome view t recycler view
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        Audio sng = audioList.get(position);
        holder.txtTitle.setText(sng.getTitle().toUpperCase().substring(0,1));
        holder.txtId.setText(sng.getTitle());
        holder.txtName.setText(sng.getArtist());
    }


    //get item coutn for recycler vier
    @Override
    public int getItemCount() {
        return audioList.size();
    }




    class SongViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtId,txtName,txtTitle;

        OnSongClickListener onSongClickListener;

        public SongViewHolder(@NonNull View itemView,OnSongClickListener onSongClickListener) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtSongId);
            txtName = itemView.findViewById(R.id.txtSongName);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            this.onSongClickListener = onSongClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSongClickListener.onSongClick(getAdapterPosition());
        }
    }


    //creating interface for click event
    public  interface OnSongClickListener{
        void onSongClick(int position);
    }
}
