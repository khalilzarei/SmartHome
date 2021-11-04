package com.khz.smarthome.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khz.smarthome.R;
import com.khz.smarthome.databinding.ActivityLoginBinding;

//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    LoginViewModel       viewModel;
    //qr code scanner object
//    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding   = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new LoginViewModel();
        binding.setViewModel(viewModel);
//        qrScan = new IntentIntegrator(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
//                String[] content  = result.getContents().split(",");
//                int      code     = Integer.parseInt(content[0]);
//                int      result01 = code * 1234;
//                String secret = new StringBuilder(String.valueOf(result01)).reverse()
//                                                                           .toString();
//                String ip = content[1];
//                SessionManager.setIp(ip);
//                SessionManager.setSecret(secret);
//                SessionManager.setIsLoggedIn(true);
//                new Handler().postDelayed(() -> {
//                    startActivity(new Intent(this, MainActivity.class));
//                    finish();
//                }, 1000);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
    }

    public void scanBarcode(View view) {
//        qrScan.initiateScan();
    }
}