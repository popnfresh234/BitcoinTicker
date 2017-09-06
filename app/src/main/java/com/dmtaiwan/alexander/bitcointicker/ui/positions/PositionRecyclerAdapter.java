package com.dmtaiwan.alexander.bitcointicker.ui.positions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;
import com.dmtaiwan.alexander.bitcointicker.model.Position;
import com.dmtaiwan.alexander.bitcointicker.utility.Utils;

import java.util.ArrayList;

/**
 * Created by Alexander on 8/25/2017.
 */

public class PositionRecyclerAdapter extends RecyclerView.Adapter<PositionRecyclerAdapter.PositionHolder> {

    private Context context;
    private ArrayList<Position> positions;
    private int secondaryCurrency;
    private AdapterCallback callback;

    public PositionRecyclerAdapter(Context context, ArrayList<Position> positions, int secondaryCurrency, AdapterCallback callback) {
        this.context = context;
        this.positions = positions;
        this.secondaryCurrency = secondaryCurrency;
        this.callback = callback;
    }

    @Override
    public PositionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.list_item_position, parent, false);
        PositionHolder viewHolder = new PositionHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PositionHolder holder, final int position) {
        //Bind views
        holder.position.setText(positions.get(position).getPosition());
        holder.price.setText(Utils.formatCurrency(positions.get(position).getPrice(), secondaryCurrency));

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitcoinDBHelper.removePositionData(context, positions.get(position).getId());
                callback.onDataUpdated();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (positions == null) {
            return 0;
        }
        return positions.size();
    }


    public class PositionHolder extends RecyclerView.ViewHolder {

        public TextView position;
        public TextView price;
        public Button removeButton;

        public PositionHolder(View itemView) {
            super(itemView);
            position = (TextView) itemView.findViewById(R.id.text_view_position_recycler_position);
            price = (TextView) itemView.findViewById(R.id.text_view_position_recycler_price);
            removeButton = (Button) itemView.findViewById(R.id.button_position_recycler_remove);
        }
    }


    public void swapData(ArrayList<Position> positions) {
        this.positions = positions;
        notifyDataSetChanged();
    }

    public interface AdapterCallback {
        void onDataUpdated();
    }

}
