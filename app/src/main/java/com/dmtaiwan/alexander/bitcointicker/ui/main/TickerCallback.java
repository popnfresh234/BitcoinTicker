package com.dmtaiwan.alexander.bitcointicker.ui.main;

import com.dmtaiwan.alexander.bitcointicker.model.Coin;

import java.util.ArrayList;

/**
 * Created by Alexander on 8/20/2017.
 */

public interface TickerCallback {
    void returnResults(ArrayList<Coin> coins);

}




