package com.techasylum.recyclerview_sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.zip.Inflater;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {
        Context mcontext;
        Cursor mCurser;
        public static final int KEY1=1;
    public static final int KEY2=2;
    public onItemClickListener mListener;



    public interface onItemClickListener{
        void onItemClick(int position,Cursor cursor);
    }

    public void setmListener(onItemClickListener mListener) {
        this.mListener = mListener;

    }

    public GroceryAdapter(Context context, Cursor cursor) {
        this.mcontext=context;
        this.mCurser=cursor;
    }


    @NonNull
    @Override
    public GroceryViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        View itemView=inflater.inflate(R.layout.item_grocery,parent,false);

        return new GroceryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroceryViewHolder holder, final int position) {
          if(!mCurser.moveToPosition(position)){
              Toast.makeText(mcontext, "please fill all detail", Toast.LENGTH_SHORT).show();
              return ;
          }
         final String name = mCurser.getString(mCurser.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME));
        int amount = mCurser.getInt(mCurser.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT));
        String time=mCurser.getString(mCurser.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_TIME));
        final long id=mCurser.getLong(mCurser.getColumnIndex(GroceryContract.GroceryEntry._ID));


        holder.nameText.setText(name);
        holder.countText.setText(String.valueOf(amount));
        holder.timeText.setText(time);
        holder.itemView.setTag(R.string.KEY1,id);
        holder.itemView.setTag(R.string.KEY2,name);
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurser.moveToPosition(position)){
                    int name = mCurser.getInt(mCurser.getColumnIndex(GroceryContract.GroceryEntry._ID));
                    Toast.makeText(mcontext, "postion"+position+"\n naame "+name, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(mcontext, "no value", Toast.LENGTH_SHORT).show();
                }


            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mCurser.getCount();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder{

        public TextView nameText;
        public TextView countText;
        public TextView timeText;
        public GroceryViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textView_itemName);
            countText = itemView.findViewById(R.id.textView_count);
            timeText=itemView.findViewById(R.id.time_stamp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position,mCurser);
                        //Toast.makeText(mContext, "djjdjddk", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void swapCurser(Cursor newCurser){
        if (mCurser!=null){
            mCurser.close();
        }
        mCurser=newCurser;
        if(newCurser!=null){

            notifyDataSetChanged();
        }


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
