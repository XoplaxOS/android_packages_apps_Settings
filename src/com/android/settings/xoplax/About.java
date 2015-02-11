/*
 * Copyright (C) 2015 The XoplaxOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.xoplax;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class About extends SettingsPreferenceFragment {

    public static final String TAG = "About";

    private static final String KEY_XOPLAX_SHARE = "share";

    Preference mSiteUrl;
    Preference mSourceUrl;
    Preference mFacebookUrl;
    Preference mKaskusUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.xoplax_about);

        mSiteUrl = findPreference("xoplax_website");
        mSourceUrl = findPreference("xoplax_source");
        mFacebookUrl = findPreference("xoplax_facebook");
        mKaskusUrl = findPreference("xoplax_kaskus");
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mSiteUrl) {
            launchUrl("http://os.xoplaxdev.org");
        } else if (preference == mSourceUrl) {
            launchUrl("https://github.com/XoplaxOS");
        } else if (preference == mFacebookUrl) {
            launchUrl("http://facebook.com/xoplaxos");
        } else if (preference == mKaskusUrl) {
            launchUrl("http://kaskus.co.id/thread/560759f8e05227795c8b4569/official-xoplax-os/1");
        } else if (preference.getKey().equals(KEY_XOPLAX_SHARE)) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, String.format(
                getActivity().getString(R.string.share_message), Build.MODEL));
        startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share_chooser_title)));
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent donate = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(donate);
    }
}
