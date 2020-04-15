package com.stapp.trovaescape.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
        public TextView name, prices, avails;
        public Button book;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.room_name);
            prices = v.findViewById(R.id.room_prices);
            avails = v.findViewById(R.id.room_avails);
            book = v.findViewById(R.id.book_btn);
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
            holder.name.setText(roomList.get(position).getName());
            holder.prices.setText(Utils.getPricesFormatted(roomList.get(position).getPrices()));
            holder.avails.setText(Utils.getAvailsFormatted(roomList.get(position).getAvailabilities()));
            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSite(roomList.get(position).getWebsite());
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
