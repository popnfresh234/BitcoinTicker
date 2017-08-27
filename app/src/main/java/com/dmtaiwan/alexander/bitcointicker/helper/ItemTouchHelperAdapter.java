package com.dmtaiwan.alexander.bitcointicker.helper;

/**
 * Created by Alexander on 8/27/2017.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onSwipe(int position);

}
