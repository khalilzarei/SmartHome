package com.khz.smarthome.mdns;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


import com.github.druk.dnssd.BrowseListener;
import com.github.druk.dnssd.DNSSD;
import com.github.druk.dnssd.DNSSDBindable;
import com.github.druk.dnssd.DNSSDException;
import com.github.druk.dnssd.DNSSDRegistration;
import com.github.druk.dnssd.DNSSDService;
import com.github.druk.dnssd.QueryListener;
import com.github.druk.dnssd.RegisterListener;
import com.github.druk.dnssd.ResolveListener;
import com.github.druk.rx2dnssd.BonjourService;

import com.khz.smarthome.R;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DNSSDActivity extends AppCompatActivity {

    private static final String TAG = DNSSDActivity.class.getSimpleName();
    private              DNSSD  dnssd;

    private Handler mHandler;

    private       DNSSDService         browseService;
    private       DNSSDService         registerService;
    private final List<BonjourService> services = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dnssdactivity);

        dnssd = new DNSSDBindable(this);

        mHandler = new Handler(Looper.getMainLooper());

        startBrowse();
        register();
        new Handler().postDelayed(() -> {

            for (BonjourService bonjourService : services) {
                if (bonjourService.getInet4Address() != null) {
                    Log.e(TAG, "Inet4Address: " + bonjourService.getInet4Address().toString() + ":" + bonjourService.getPort());
                } else if (bonjourService.getInet6Address() != null) {
                    Log.e(TAG, "Inet6Address: " + bonjourService.getInet6Address().toString() + ":" + bonjourService.getPort());
                } else {
                    Log.e(TAG, getString(R.string.unresolved));
                }
                Log.e(TAG, "Interface: " + bonjourService.getIfIndex());
            }
        }, 500);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (browseService == null) {
            startBrowse();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (browseService != null) {
            stopBrowse();
            services.clear();
        }
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
                    services.remove(new BonjourService.Builder(flags, ifIndex, serviceName, regType, domain).build());
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
                            services.add(bonjourService);
                            Log.e(TAG, "builder => " + builder.hostname("hsi.local").build().getInet4Address());
                            if (bonjourService.getInet4Address() != null) {
                                Log.e(TAG, "Inet4Address: " + bonjourService.getInet4Address() + ":" + bonjourService.getPort());
                            } else if (bonjourService.getInet6Address() != null) {
                                Log.e(TAG, "Inet6Address: " + bonjourService.getInet6Address().toString() + ":" + bonjourService.getPort());
                            } else {
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
            registerService = dnssd.register(Build.DEVICE, "_rxdnssd._tcp", 123, new RegisterListener() {
                @Override
                public void serviceRegistered(DNSSDRegistration registration, int flags, String serviceName, String regType, String domain) {
                    Log.e(TAG, "Register successfully " + Build.DEVICE);
//                    button.setEnabled(true);
//                    button.setText(R.string.unregister);
                    Toast.makeText(DNSSDActivity.this, "serviceRegistered " + serviceName + Build.DEVICE, Toast.LENGTH_SHORT).show();
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

    private void unregistered(final Button button) {
        Log.e(TAG, "unregister");
        registerService.stop();
        registerService = null;
        button.setText(R.string.register);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (browseService != null) {
            browseService.stop();
        }
        if (registerService != null) {
            registerService.stop();
        }
    }
}
