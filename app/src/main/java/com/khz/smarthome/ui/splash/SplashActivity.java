package com.khz.smarthome.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.databinding.DataBindingUtil;

import com.github.druk.dnssd.BrowseListener;
import com.github.druk.dnssd.DNSSD;
import com.github.druk.dnssd.DNSSDException;
import com.github.druk.dnssd.DNSSDRegistration;
import com.github.druk.dnssd.DNSSDService;
import com.github.druk.dnssd.QueryListener;
import com.github.druk.dnssd.RegisterListener;
import com.github.druk.dnssd.ResolveListener;
import com.github.druk.rx2dnssd.BonjourService;
import com.google.android.material.snackbar.Snackbar;
import com.khz.smarthome.R;
import com.khz.smarthome.application.BaseActivity;
import com.khz.smarthome.databinding.ActivitySplashBinding;
import com.khz.smarthome.helper.Connectivity;
import com.khz.smarthome.helper.Constants;
import com.khz.smarthome.helper.SessionManager;
import com.khz.smarthome.ui.home.HomeActivity;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    Activity              activity = this;
    ActivitySplashBinding binding;
    private DNSSD   dnssd;
    private Handler mHandler;

    private DNSSDService browseService;
    private DNSSDService registerService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
//        dnssd   = new DNSSDBindable(this);
//
//        mHandler = new Handler(Looper.getMainLooper());
//        SessionManager.setIp(Constants.MQTT_SERVER_URI);

        VideoView videoView = binding.videoView;
        videoView.setVideoURI(Uri.parse(
                "android.resource://" + getPackageName() + "/" + R.raw.splash_video_land));
        videoView.start();
        videoView.setOnCompletionListener(mediaPlayer -> {
            if (Connectivity.isConnected(this)) {
                openActivity();
            } else {
                Snackbar.make(binding.getRoot(), "Please check Connection", Snackbar.LENGTH_LONG).setAction("OK", view -> {
                    recreate();
                }).show();
            }
        });


//        openActivity();

    }

    public void openActivity() {
//        new Handler().postDelayed(() -> {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        finish();
//        }, 500);
    }


    private void startBrowse() {
        Log.e(TAG, "start browse");
        try {
            browseService = dnssd.browse("_workstation._tcp", new BrowseListener() {
                @Override
                public void serviceFound(DNSSDService browser, int flags, int ifIndex, final String serviceName, String regType, String domain) {
                    Log.e(TAG, "Found " + serviceName);
                    startResolve(flags, ifIndex, serviceName, regType, domain);
                }

                @Override
                public void serviceLost(DNSSDService browser, int flags, int ifIndex, String serviceName, String regType, String domain) {
                }

                @Override
                public void operationFailed(DNSSDService service, int errorCode) {
                    Log.e(TAG, "error: " + errorCode);
                }
            });
        } catch (DNSSDException e) {
            e.printStackTrace();
            Log.e(TAG, "error", e);
        }
    }

    private void startResolve(int flags, int ifIndex, final String serviceName, final String regType, final String domain) {
        try {
            dnssd.resolve(flags, ifIndex, serviceName, regType, domain, new ResolveListener() {
                @Override
                public void serviceResolved(DNSSDService resolver, int flags, int ifIndex, String fullName, String hostName, int port, Map<String, String> txtRecord) {
                    Log.e(TAG, "Resolved " + hostName);
                    startQueryRecords(ifIndex, serviceName, regType, domain, hostName, port, txtRecord);
                }

                @Override
                public void operationFailed(DNSSDService service, int errorCode) {

                }
            });
        } catch (DNSSDException e) {
            e.printStackTrace();
        }
    }

    private void startQueryRecords(int ifIndex, final String serviceName, final String regType, final String domain, final String hostName, final int port, final Map<String, String> txtRecord) {
        try {
            QueryListener listener = new QueryListener() {
                @Override
                public void queryAnswered(DNSSDService query, int flags, int ifIndex, String fullName, int rrtype, int rrclass, byte[] rdata, int ttl) {
//                    Log.e(TAG, "Query address " + fullName);
                    mHandler.post(() -> {

                        BonjourService.Builder builder = new BonjourService.Builder(flags, ifIndex, serviceName, regType, domain).dnsRecords(txtRecord).port(port).hostname(hostName);
                        try {
                            InetAddress address = InetAddress.getByAddress(rdata);
                            if (address instanceof Inet4Address) {
                                builder.inet4Address((Inet4Address) address);
                            } else if (address instanceof Inet6Address) {
                                builder.inet6Address((Inet6Address) address);
                            }
                            BonjourService bonjourService = builder.build();
                            if (builder.hostname("HSI.local").build().getInet4Address() != null) {
                                Inet4Address inet4Address = bonjourService.getInet4Address();
                                String       ip           = inet4Address.getHostAddress();
                                Log.e(TAG, "builder => " + builder.hostname("HSI.local").build().getInet4Address().getHostAddress());
                                Log.e(TAG, "IF Inet4Address: " + ip + ":" + bonjourService.getPort());
                                SessionManager.setIsLoggedIn(true);
                                SessionManager.setIp(ip);
                            } else {
                                SessionManager.setIp(Constants.MQTT_SERVER_URI);
                                Log.e(TAG, "ELSE Inet4Address: " + bonjourService.getInet4Address() + ":" + bonjourService.getPort());
                                Log.e(TAG, getString(R.string.unresolved));
                            }
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }

                    });
                }

                @Override
                public void operationFailed(DNSSDService service, int errorCode) {

                }
            };
            dnssd.queryRecord(0, ifIndex, hostName, 1, 1, listener);
            dnssd.queryRecord(0, ifIndex, hostName, 28, 1, listener);
        } catch (DNSSDException e) {
            e.printStackTrace();
        }
    }

    private void stopBrowse() {
        Log.e(TAG, "Stop browsing");
        browseService.stop();
        browseService = null;
    }

    private void register() {
        Log.e(TAG, "register");
//        button.setEnabled(false);
        try {
            registerService = dnssd.register(Build.DEVICE, "_workstation._tcp", 123, new RegisterListener() {
                @Override
                public void serviceRegistered(DNSSDRegistration registration, int flags, String serviceName, String regType, String domain) {
                    Log.e(TAG, "Register successfully " + Build.DEVICE);

                    Toast.makeText(activity, "serviceRegistered " + serviceName + Build.DEVICE, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void operationFailed(DNSSDService service, int errorCode) {
                    Log.e(TAG, "error " + errorCode);
//                    button.setEnabled(true);
                }
            });
        } catch (DNSSDException e) {
            e.printStackTrace();
        }
    }
/*
    private void unregistered(final Button button) {
        Log.e(TAG, "unregister");
        registerService.stop();
        registerService = null;
        button.setText(R.string.register);
    }
*/

}