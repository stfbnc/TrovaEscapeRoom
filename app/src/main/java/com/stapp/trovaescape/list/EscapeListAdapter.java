package com.stapp.trovaescape.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.utils.Utils;

import java.util.ArrayList;

public class EscapeListAdapter extends RecyclerView.Adapter<EscapeListAdapter.ViewHolder> {

    private ArrayList<Escape> escapeList;
    private Context ctx;
    View.OnClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address, count;
        public ArrayList<ImageView> tags = new ArrayList<>();
        public ImageView freeRooms;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            address = v.findViewById(R.id.address);
            //tags = v.findViewById(R.id.tags);
            count = v.findViewById(R.id.room_count);
            freeRooms = v.findViewById(R.id.free_img);
            tags.add((ImageView) v.findViewById(R.id.t1));
            tags.add((ImageView) v.findViewById(R.id.t2));
            tags.add((ImageView) v.findViewById(R.id.t3));
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
                holder.freeRooms.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.escape_icon_green));
            else
                holder.freeRooms.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.escape_icon_red));

            holder.name.setText(escapeList.get(position).getName());

            holder.address.setText(escapeList.get(position).getAddress());

            //holder.tags.setText(escapeList.get(position).getAllTagsFormatted());

            holder.count.setText(Utils.getRoomsDoneRatio(escapeList.get(position)));

            String[] tg = Utils.getTagsFormatted(escapeList.get(position).getTags());
            for(int i = 0; i < tg.length; i++) {
                if(i >= Constants.MAX_TAGS)
                    break;
                ImageView iv = holder.tags.get(i);
                iv.setVisibility(View.VISIBLE);
                Bitmap t = Utils.getTagItem(ctx, tg[i]);
                iv.setImageBitmap(t);
            }
        }
    }

    @Override
    public int getItemCount() {
        return escapeList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

}
