package com.stapp.trovaescape.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Escape;

import java.util.ArrayList;

public class EscapeListAdapter extends RecyclerView.Adapter<EscapeListAdapter.ViewHolder> {

    private ArrayList<Escape> escapeList;
    private Context ctx;
    View.OnClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address, tags;
        public ImageView freeRooms;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            address = v.findViewById(R.id.address);
            tags = v.findViewById(R.id.tags);
            freeRooms = v.findViewById(R.id.free_img);
        }
    }

    public EscapeListAdapter(ArrayList<Escape> escapeList) {
        this.escapeList = escapeList;
    }

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public EscapeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View row = inflater.inflate(R.layout.escapelist_row, parent, false);
        EscapeListAdapter.ViewHolder viewHolder = new EscapeListAdapter.ViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EscapeListAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(clickListener);
        if(escapeList.size() > 0){
            if(escapeList.get(position).getFree())
                holder.freeRooms.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.icon_lock_green_24dp));
            else
                holder.freeRooms.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.icon_lock_red_24dp));
            holder.name.setText(escapeList.get(position).getName());
            holder.address.setText(escapeList.get(position).getAddress());
            holder.tags.setText(escapeList.get(position).getAllTagsFormatted());
        }
    }

    @Override
    public int getItemCount() {
        return escapeList.size();
    }

}
