package com.frame.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 网络工具
 */
public class NetWorkUtil {

    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivity = getConnectManager(context.getApplicationContext());
        if (connectivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivity.getAllNetworks();
                for (Network network : networks) {
                    if (connectivity.getNetworkInfo(network).isConnected()) {
                        return true;
                    }
                }
            } else {
                NetworkInfo[] infos = connectivity.getAllNetworkInfo();
                if (infos != null) {
                    for (NetworkInfo info : infos) {
                        if (info.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = getConnectManager(context.getApplicationContext());
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.getType();
            }
        }
        return -1;
    }

    private static ConnectivityManager getConnectManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static String getWifiIp(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    public static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            TLog.i(ex.toString());
        }
        return "";
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    public static String getIpAddress(Context context) {
        if (getNetWorkType(context.getApplicationContext()) == ConnectivityManager.TYPE_WIFI) {
            return getWifiIp(context.getApplicationContext());
        } else {
            return getLocalIp();
        }
    }

}
