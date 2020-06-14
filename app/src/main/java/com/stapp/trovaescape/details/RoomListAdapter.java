package com.stapp.trovaescape.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        public TextView name, prices, ptitle, atitle;
        public ArrayList<ImageView> avails = new ArrayList<>();
        public ImageButton book, done;
        public LinearLayout room_layout, hours;
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
            book = v.findViewById(R.id.book_btn);
            done = v.findViewById(R.id.escape_tick);
            room_layout = v.findViewById(R.id.roomlist_row);
        }
    }

    public RoomListAdapter(ArrayList<Room> roomList) {
        setHasStableIds(true);
        this.roomList = roomList;
    }

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
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
        //holder.itemView.setOnClickListener(clickListener);
        if(roomList.size() > 0){
            if(roomList.get(position).getFree())//.getAvailabilities().equals(""))
                holder.room_layout.setBackground(ctx.getDrawable(R.drawable.room_background_green));
            else
                holder.room_layout.setBackground(ctx.getDrawable(R.drawable.room_background_red));

            holder.name.setText(roomList.get(position).getName());

            String prc = Utils.getPricesFormatted(ctx, roomList.get(position).getPrices());
            if(prc.equals("")) {
                holder.prices.setVisibility(View.GONE);
                holder.ptitle.setVisibility(View.GONE);
            }else {
                holder.prices.setText(prc);
            }

            /*String avl = Utils.getAvailsFormatted(ctx, roomList.get(position).getAvailabilities());
            if(avl.equals("")) {
                holder.avails.setVisibility(View.GONE);
                holder.atitle.setVisibility(View.GONE);
            }else {
                holder.avails.setText(avl);
            }*/
            //String avl = Utils.getAvailsFormatted(ctx, roomList.get(position).getAvailabilities());
            //Log.d("ROOM", roomList.get(position).getName()+" --- "+avl+" --- "+roomList.get(position).getFree());
            if(roomList.get(position).getFree()){
                String[] avl = Utils.getAvailsFormatted(ctx, roomList.get(position).getAvailabilities());
                //holder.avails.setText(avl);
                for(int i = 0; i < avl.length; i++) {
                    ImageView iv = holder.avails.get(i);
                    iv.setVisibility(View.VISIBLE);
                    //Bitmap h = Utils.getHourItem(ctx, avl[i]);
                    Bitmap h = Utils.getHourItem(ctx, avl[i]);
                    iv.setImageBitmap(h);
                }
                for(int i = avl.length; i < Constants.MAX_HOURS; i++) {
                    ImageView iv = holder.avails.get(i);
                    iv.setVisibility(View.GONE);
                }
                //holder.avails.setImageBitmap(h);
            }else{
                //holder.avails.setVisibility(View.GONE);
                holder.atitle.setVisibility(View.GONE);
            }

            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSite(roomList.get(holder.getAdapterPosition()).getWebsite());
                }
            });

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
