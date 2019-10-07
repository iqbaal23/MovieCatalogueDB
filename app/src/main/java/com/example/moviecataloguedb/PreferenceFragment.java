package com.example.moviecataloguedb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.moviecataloguedb.alarm.AlarmReceiver;

public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String DAILY_REMINDER;
    private String RELEASE_REMINDER;

    private SwitchPreference dailyPreference;
    private SwitchPreference releasePreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();
    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        dailyPreference.setChecked(sh.getBoolean(DAILY_REMINDER, false));
        releasePreference.setChecked(sh.getBoolean(RELEASE_REMINDER, false));
    }

    private void init() {
        DAILY_REMINDER = getResources().getString(R.string.key_daily);
        RELEASE_REMINDER = getResources().getString(R.string.key_release);

        dailyPreference = (SwitchPreference) findPreference(DAILY_REMINDER);
        releasePreference = (SwitchPreference) findPreference(RELEASE_REMINDER);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        String repeatTime = "07:00";
        String releaseTime = "08:00";
        String repeatMessage = getString(R.string.repeatMessage);
        String releaseMessage = getString(R.string.releaseMessage);
        if (key.equals(DAILY_REMINDER)){
            dailyPreference.setChecked(sharedPreferences.getBoolean(DAILY_REMINDER, false));
            if (dailyPreference.isChecked()){
                alarmReceiver.setDailyReminder(getContext(), AlarmReceiver.TYPE_DAILY_REMINDER, repeatTime, repeatMessage);
            } else{
                alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.TYPE_DAILY_REMINDER);
            }
        } else if (key.equals(RELEASE_REMINDER)){
            releasePreference.setChecked(sharedPreferences.getBoolean(RELEASE_REMINDER, false));
            if (releasePreference.isChecked()){
                alarmReceiver.setReleaseToday(getContext(), AlarmReceiver.TYPE_RELEASE_TODAY, releaseTime, releaseMessage);
            } else{
                alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.TYPE_RELEASE_TODAY);
            }
        }
    }
}
