package com.khz.smarthome.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.khz.smarthome.BR;
import com.khz.smarthome.R;
import com.khz.smarthome.application.App;
import com.khz.smarthome.helper.SessionManager;
import com.khz.smarthome.model.LoginData;
import com.khz.smarthome.model.User;
import com.khz.smarthome.network.APIService;
import com.khz.smarthome.network.RetroClass;
import com.khz.smarthome.ui.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends BaseObservable {

    //region variable
    String email    = "";
    String password = "";

    //endregion


    //region Getter Setter
    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
    //endregion

    //region OnClick
    public void checkLogin(View view) {
        Activity activity = (Activity) view.getContext();
        if (email == null || email.isEmpty()) {
            App.showErrorSnackBar(view, activity.getString(R.string.error_text_email) + "");
            return;
        }
        if (password == null || password.isEmpty()) {
            App.showErrorSnackBar(view, activity.getString(R.string.error_text_password) + "");
            return;
        }
        login(view);
    }
    //endregion

    //region Method
    private void login(View view) {
        Activity       activity       = (Activity) view.getContext();
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        APIService          apiService   = RetroClass.getAPIService();
        Call<LoginResponse> responseCall = apiService.login(email, password);
        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("login", response.body() + "");
                    String  message = response.body().getMessage();
                    boolean status  = response.body().getStatus();
                    if (status) {
                        App.showSuccessSnackBar(view, message);
                        LoginData loginData = response.body().getLoginData();
                        User      user      = loginData.getUser();

                        SessionManager.setUser(user);
                        SessionManager.setIsLoggedIn(true);
                        SessionManager.setToken(loginData.getToken().getAccessToken());
                        SessionManager.setExpireToken(loginData.getToken().getExpiresAt());
                        new Handler().postDelayed(() -> {
                            progressDialog.dismiss();
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        }, 1000);
                    } else {
                        App.showErrorSnackBar(view, message);
                        new Handler().postDelayed(progressDialog::dismiss, 1000);
                    }

                }
            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage() + " ");
                new Handler().postDelayed(progressDialog::dismiss, 1000);
            }
        });

    }

    //endregion


}
