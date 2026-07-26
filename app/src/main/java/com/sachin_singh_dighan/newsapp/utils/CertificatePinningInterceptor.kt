package com.sachin_singh_dighan.newsapp.utils

import android.util.Log
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class CertificatePinningInterceptor : Interceptor {

    companion object {
        private const val TAG = "CertPinning"
    }

    //SHA-256 hashes of trusted certificates
    private val TRUSTED_PINS = setOf(
        "sha256/bKg2aMKeqFfv5TjaYDkBnCWyl2fCvavyedlN3mNcy+8=", // newsapi.org (leaf certificate)
        "sha256/kIdp6NNEd8wsugYyyIYFsi1ylMCED3hZbSR8ZFsa/A4=", // Google Trust Services (intermediate)
        "sha256/mEflZT5enoR1FuXLgYYGqnVEoZvmf9c2bVBpiOjYQ0c="  // GTS Root R4 (root certificate)
    )

    //Hosts that required certificate pinning
    private val PINNED_HOSTS = setOf("newsapi.org")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val host = request.url.host

        //only apply pinning to specific hosts
        if (PINNED_HOSTS.contains(host)) {
            Log.d(TAG, "Applying pinning certificate to the host :  $host")

            //Create certificate pinner
            val certificatePinner =
                CertificatePinner.Builder().add(host, *TRUSTED_PINS.toTypedArray()).build()

            //Create new client with certificate pinning
            val pinnedClient = OkHttpClient.Builder().certificatePinner(certificatePinner).build()

            //Execute request with pinned client
            val call = pinnedClient.newCall(request)
            return call.execute()
        }
        //For non-pinned hosts, proceed normally
        return chain.proceed(request)
    }
}