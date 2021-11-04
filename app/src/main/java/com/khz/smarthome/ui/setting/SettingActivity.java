package com.khz.smarthome.ui.setting;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.khz.smarthome.R;
import com.khz.smarthome.application.BaseActivity;
import com.khz.smarthome.databinding.ActivitySettingBinding;
import com.khz.smarthome.helper.SessionManager;

public class SettingActivity extends BaseActivity {

    ActivitySettingBinding binding;
    SettingViewModel       viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding   = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        viewModel = new SettingViewModel(this, SessionManager.isNightModeOn() ? R.drawable.ic_night : R.drawable.ic_day);
        binding.setViewModel(viewModel);
        viewModel.setIpStatus(SessionManager.getIpStatus());
        binding.checkBoxSound.setChecked(SessionManager.getPlaySound());
        binding.checkBoxSound.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    SessionManager.setPlaySound(isChecked);
                }
        );
        binding.checkBoxVibration.setChecked(SessionManager.getVibration());
        binding.checkBoxVibration.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    SessionManager.setVibration(isChecked);
                }
        );
    }
}