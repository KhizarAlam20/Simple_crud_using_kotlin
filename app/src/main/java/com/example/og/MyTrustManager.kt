package com.example.og

import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

class MyTrustManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }
}
