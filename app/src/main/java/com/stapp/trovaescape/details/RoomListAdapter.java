package com.stapp.trovaescape.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Room;
import com.stapp.trovaescape.utils.Utils;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private ArrayList<Room> roomList;
    private Context ctx;
    View.OnClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, prices, avails, ptitle, atitle;
        public ImageButton book, done;
        public LinearLayout room_layout;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.room_name);
            prices = v.findViewById(R.id.room_prices);
            avails = v.findViewById(R.id.room_avails);
            ptitle = v.findViewById(R.id.title_prices);
            atitle = v.findViewById(R.id.title_avails);
            book = v.findViewById(R.id.book_btn);
            done = v.findViewById(R.id.escape_tick);
            room_layout = v.findViewById(R.id.ll_room);
        }
    }

    public RoomListAdapter(ArrayList<Room> roomList) {
        this.roomList = roomList;
    }

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RoomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View row = inflater.inflate(R.layout.roomlist_row, parent, false);
        RoomListAdapter.ViewHolder viewHolder = new RoomListAdapter.ViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RoomListAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(clickListener);
        if(roomList.size() > 0){
            if(roomList.get(position).getAvailabilities().equals(""))
                holder.room_layout.setBackground(ctx.getDrawable(R.drawable.room_background_red));
            else
                holder.room_layout.setBackground(ctx.getDrawable(R.drawable.room_background_green));
            holder.name.setText(roomList.get(position).getName());

            String prc = Utils.getPricesFormatted(ctx, roomList.get(position).getPrices());
            if(prc.equals("")) {
                holder.prices.setVisibility(View.GONE);
                holder.ptitle.setVisibility(View.GONE);
            }else {
                holder.prices.setText(prc);
            }

            String avl = Utils.getAvailsFormatted(ctx, roomList.get(position).getAvailabilities());
            if(avl.equals("")) {
                holder.avails.setVisibility(View.GONE);
                holder.atitle.setVisibility(View.GONE);
            }else {
                holder.avails.setText(avl);
            }

            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSite(roomList.get(position).getWebsite());
                }
            });

            holder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.done.setBackground(ctx.getDrawable(R.drawable.room_done));
                }
            });
        }
    }

    private void goToSite(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        ctx.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

}
