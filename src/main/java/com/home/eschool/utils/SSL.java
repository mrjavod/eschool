package com.home.eschool.utils;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SSL {

    public static void trustmanager() {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        try {
            if (sc != null) {
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        if (sc != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
