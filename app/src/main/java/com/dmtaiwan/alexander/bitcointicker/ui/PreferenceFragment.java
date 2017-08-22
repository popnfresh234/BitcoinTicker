package com.dmtaiwan.alexander.bitcointicker.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;

/**
 * Created by Alexander on 8/21/2017.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {
    private static final String[] PROJECTION = new String[]{BitcoinDBContract.BitcoinEntry.COLUMN_NAME, BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Cursor cursor = BitcoinDBHelper.readDb(getActivity(), PROJECTION, null, null);


        PreferenceScreen screen = this.getPreferenceScreen();
        PreferenceCategory category = new PreferenceCategory(screen.getContext());
        category.setTitle("Coin Configuration");
        screen.addPreference(category);
        while (cursor.moveToNext()) {
            String coinName = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME));
            String coinID = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID));
            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(screen.getContext());
            checkBoxPreference.setKey(coinID);
            checkBoxPreference.setTitle(coinName);
            category.addPreference(checkBoxPreference);
        }
    }
}
