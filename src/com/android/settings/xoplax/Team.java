package com.android.settings.xoplax;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.util.Helpers;


public class Team extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "XoplaXTeam";

    Preference mEstiko;
    Preference mMahameru;
    Preference mJoe;
    Preference mStanley;
    Preference mYosef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentResolver resolver = getActivity().getContentResolver();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.xoplax_team);

        PreferenceScreen prefSet = getPreferenceScreen();

        mEstiko = prefSet.findPreference("xoplax_estiko");
        mMahameru = prefSet.findPreference("xoplax_mahameru");
        mJoe = prefSet.findPreference("xoplax_joe");
        mStanley = prefSet.findPreference("xoplax_stanley");
        mYosef = prefSet.findPreference("xoplax_yosef");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        ContentResolver resolver = getActivity().getContentResolver();
        boolean value;
        if (preference == mEstiko) {
            Toast.makeText(getActivity(), "Coder and Server Admin",
                    Toast.LENGTH_LONG).show();
        } else if (preference == mMahameru) {
            Toast.makeText(getActivity(), "Designer",
                    Toast.LENGTH_LONG).show();
        } else if (preference == mJoe) {
            Toast.makeText(getActivity(), "Designer",
                    Toast.LENGTH_LONG).show();
        } else if (preference == mStanley) {
            Toast.makeText(getActivity(), "Coder and UX Designer",
                    Toast.LENGTH_LONG).show();
        } else if (preference == mYosef) {
            Toast.makeText(getActivity(), "Coder and Server Admin",
                    Toast.LENGTH_LONG).show();
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        final String key = preference.getKey();

        return true;
    }
}

