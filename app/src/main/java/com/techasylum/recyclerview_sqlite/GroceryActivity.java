package com.techasylum.recyclerview_sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;




import java.util.ArrayList;
import java.util.List;

import static com.techasylum.recyclerview_sqlite.GroceryAdapter.*;

public class GroceryActivity extends AppCompatActivity {

    private EditText editText_item_Name;
    private TextView textView_amount;
    private SQLiteDatabase mDatabase;
    ImageButton Btn_decrease, Btn_increase, Btn_add;
    RecyclerView recyclerView_Grocery;
    int mAmout;
    private TextView textView_RemoveAll;
    private GroceryAdapter mAdapter;

    int BufferId = 1;
    ArrayList<Product> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        GroceryDbHelper groceryDbHelper = new GroceryDbHelper(this);
        mDatabase = groceryDbHelper.getWritableDatabase();
        recyclerView_Grocery = findViewById(R.id.recyclerView_Grocery);
        recyclerView_Grocery.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this, getAllItems());

        recyclerView_Grocery.setAdapter(mAdapter);


        //resourses id
        editText_item_Name = findViewById(R.id.Edit_txt_item);
        textView_amount = findViewById(R.id.textView_Amount);
        Btn_increase = findViewById(R.id.button_increment);
        Btn_decrease = findViewById(R.id.button_decrement);
        Btn_add = findViewById(R.id.button_add_item);
        textView_RemoveAll = findViewById(R.id.textView_RemoveAll);

        Btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAmout < 5) {
                    mAmout++;
                    textView_amount.setText(String.valueOf(mAmout));
                }
            }
        });
        Btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAmout > 0) {
                    mAmout--;
                    textView_amount.setText(String.valueOf(mAmout));
                }
            }
        });

        Btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_update_Item_Grocery();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag(R.string.KEY1));
                String id = viewHolder.itemView.getTag(R.string.KEY1).toString();
                String name = viewHolder.itemView.getTag(R.string.KEY2).toString();

                Toast.makeText(GroceryActivity.this, "delete item " + id + "\n name" + name, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView_Grocery);

        textView_RemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllItem();
            }
        });

        mAdapter.setmListener(new onItemClickListener() {
            @Override
            public void onItemClick(int position, Cursor cursor) {

                if (cursor.moveToPosition(position)) {
                    BufferId = cursor.getInt(cursor.getColumnIndex(GroceryContract.GroceryEntry._ID));
                    String BuferrName = cursor.getString(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME));
                    String BufferAmount = cursor.getString(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT));
                    mAmout = Integer.parseInt(BufferAmount);
                    editText_item_Name.setText(BuferrName);
                    textView_amount.setText(BufferAmount);


                }


            }
        });
    }


    private void updateItem_Grocery() {
        String name = editText_item_Name.getText().toString();
        if (name.trim().length() == 0 || mAmout == 0) {
            Toast.makeText(this, "fill all detail", Toast.LENGTH_SHORT).show();

            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmout);
        Toast.makeText(this, "id " + BufferId, Toast.LENGTH_SHORT).show();


        //continue from herere


    }

    private void add_update_Item_Grocery() {

        String name = editText_item_Name.getText().toString();
        if (name.trim().length() == 0 || mAmout == 0) {
            Toast.makeText(this, "fill all detail", Toast.LENGTH_SHORT).show();

            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmout);

        if (BufferId == 1) {
            mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        } else {
            String[] arg = new String[]{String.valueOf(BufferId)};
            mDatabase.update(GroceryContract.GroceryEntry.TABLE_NAME,
                    cv, GroceryContract.GroceryEntry._ID + "=?", arg);
        }

        mAdapter.swapCurser(getAllItems());
        editText_item_Name.getText().clear();
        mAmout = 0;
        textView_amount.setText(String.valueOf(mAmout));
        Toast.makeText(this, "table updated", Toast.LENGTH_SHORT).show();
    }

    private void removeItem(long id) {
        String[] arg = new String[]{String.valueOf(id)};
        mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                GroceryContract.GroceryEntry._ID + "=?", arg);
        mAdapter.swapCurser(getAllItems());
    }

    //all delete
    private void removeAllItem() {

        mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                null, null);
        mAdapter.swapCurser(getAllItems());
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIME + " DESC"
        );
    }
        //firebase  list update code

    /*public void saveDataList() {
        dataList = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + GroceryContract.GroceryEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                dataList.add(new Product(cursor.getString(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (dataList.size() > 0) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Grocery");
            for (Product d : dataList) {
                ref.push().setValue(d);
                Toast.makeText(this, "Uploading", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

}
