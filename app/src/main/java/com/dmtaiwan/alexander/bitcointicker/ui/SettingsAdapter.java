package com.dmtaiwan.alexander.bitcointicker.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;

/**
 * Created by Alexander on 8/25/2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;


    public SettingsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.list_item_pref, parent, false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //set alternating colors
        if (position % 2 == 0) {
            holder.prefRootView.setBackgroundColor(context.getResources().getColor(R.color.colorListViewAlternate));
        } else holder.prefRootView.setBackgroundColor(Color.TRANSPARENT);
        //get shared prefs for binding vales
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        cursor.moveToPosition(position);
        holder.prefTextView.setText(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME)));
        //Setup checkbox
        final String coinId = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID));
        prefs.getBoolean(coinId, false);
        holder.prefCheckBox.setChecked(prefs.getBoolean(coinId,false));
        holder.prefCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move cursor to correct position
                cursor.moveToPosition(position);
                //get id
                if (holder.prefCheckBox.isChecked()) {
                    editor.putBoolean(coinId, true).commit();
                } else editor.remove(coinId).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout prefRootView;
        public TextView prefTextView;
        public CheckBox prefCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            prefRootView = (RelativeLayout) itemView.findViewById(R.id.pref_root_view);
            prefTextView = (TextView) itemView.findViewById(R.id.pref_name);
            prefCheckBox = (CheckBox) itemView.findViewById(R.id.pref_check_box);
        }
    }
}
