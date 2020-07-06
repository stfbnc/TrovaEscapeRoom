package com.stapp.trovaescape.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Room;
import com.stapp.trovaescape.utils.Utils;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private ArrayList<Room> roomList;
    private Context ctx;
    View.OnClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, prices, ptitle, atitle, hourInfo, roomColor;
        public ArrayList<ImageView> avails = new ArrayList<>();
        public ImageButton book, done;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.room_name);
            prices = v.findViewById(R.id.room_prices);
            avails.add((ImageView) v.findViewById(R.id.h1));
            avails.add((ImageView) v.findViewById(R.id.h2));
            avails.add((ImageView) v.findViewById(R.id.h3));
            avails.add((ImageView) v.findViewById(R.id.h4));
            avails.add((ImageView) v.findViewById(R.id.h5));
            avails.add((ImageView) v.findViewById(R.id.h6));
            avails.add((ImageView) v.findViewById(R.id.h7));
            avails.add((ImageView) v.findViewById(R.id.h8));
            ptitle = v.findViewById(R.id.title_prices);
            atitle = v.findViewById(R.id.title_avails);
            hourInfo = v.findViewById(R.id.hour_info);
            roomColor = v.findViewById(R.id.room_color);
            book = v.findViewById(R.id.book_btn);
            done = v.findViewById(R.id.escape_tick);
        }
    }

    public RoomListAdapter(ArrayList<Room> roomList) {
        setHasStableIds(true);
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View row = inflater.inflate(R.layout.roomlist_row, parent, false);
        RoomListAdapter.ViewHolder viewHolder = new RoomListAdapter.ViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomListAdapter.ViewHolder holder, int position) {
        if(roomList.size() > 0){
            if(roomList.get(position).getFree()) {
                holder.roomColor.setBackgroundColor(ctx.getResources().getColor(R.color.available));
            } else {
                if(roomList.get(position).getUncertain()) {
                    holder.roomColor.setBackgroundColor(ctx.getResources().getColor(R.color.not_retrievable_hours));
                }else {
                    holder.roomColor.setBackgroundColor(ctx.getResources().getColor(R.color.not_available));
                }
            }

            holder.name.setText(roomList.get(position).getName());

            String prc = Utils.getPricesFormatted(ctx, roomList.get(position).getPrices());
            holder.prices.setText(prc);

            if(roomList.get(position).getFree()){
                String[] avl = Utils.getAvailsFormatted(ctx, roomList.get(position).getAvailabilities());
                for(int i = 0; i < avl.length; i++) {
                    ImageView iv = holder.avails.get(i);
                    iv.setVisibility(View.VISIBLE);
                    Bitmap h = Utils.getHourItem(ctx, avl[i]);
                    iv.setImageBitmap(h);
                }
                for(int i = avl.length; i < Constants.MAX_HOURS; i++) {
                    ImageView iv = holder.avails.get(i);
                    iv.setVisibility(View.GONE);
                }
            }else{
                holder.hourInfo.setVisibility(View.VISIBLE);
                if(roomList.get(position).getUncertain())
                    holder.hourInfo.setText(ctx.getString(R.string.no_avails));
                else
                    holder.hourInfo.setText(ctx.getString(R.string.no_avails_today));
            }

            if(!roomList.get(position).getWebsite().equals("")) {
                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToSite(roomList.get(holder.getAdapterPosition()).getWebsite());
                    }
                });
            }else{
                holder.book.setVisibility(View.GONE);
            }

            if(roomList.get(position).getDone())
                holder.done.setBackground(ctx.getDrawable(R.drawable.room_done));
            else
                holder.done.setBackground(ctx.getDrawable(R.drawable.room_not_done));

            holder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
                    String code = roomList.get(holder.getAdapterPosition()).getCode();
                    if(prefs.getString(code, Constants.DOESNT_EXIST_STR).equals(Constants.DOESNT_EXIST_STR)){
                        holder.done.setBackground(ctx.getDrawable(R.drawable.room_done));
                        prefs.edit().putString(code, Constants.EXISTS).apply();
                        roomList.get(holder.getAdapterPosition()).setDone(true);
                    }else{
                        holder.done.setBackground(ctx.getDrawable(R.drawable.room_not_done));
                        prefs.edit().remove(code).apply();
                        roomList.get(holder.getAdapterPosition()).setDone(false);
                    }
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

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

}
