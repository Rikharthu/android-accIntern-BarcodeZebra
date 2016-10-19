package com.example.android.barcodezebra;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.Map;

public class MyWebViewClient extends WebViewClient {
    public static final String TAG=MyWebViewClient.class.getSimpleName();

    private boolean shouldRestore;
    private ArrayList<String> mBarcodesToRestore;

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        view.loadUrl("file:///android_asset/error_page.html");

        Map<String,String> headers =request.getRequestHeaders();
        Log.d(TAG,"headers: "+headers.toString());
        Log.d(TAG,"error code: "+error.getErrorCode()+"\n"+error.getDescription());

    }

    // executed when WebView is fully rendered
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//        view.loadUrl("javascript:addBarcodeJS('"+1234567890+"')");
        if(shouldRestore){
            for (String barcode: mBarcodesToRestore) {
                view.loadUrl("javascript:addBarcodeJS('"+barcode+"')");
            }
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Uri.parse(url).getHost().equals("www.example.com")) {
            // This is my web site, so do not override; let my WebView load the page
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        startActivity(intent);
//        return true;

        return false;
    }

    public void restoreBarcodes(ArrayList<String> mBarcodes) {
        shouldRestore=true;
        mBarcodesToRestore=mBarcodes;
    }
    /* Now when the user clicks a link, the system calls shouldOverrideUrlLoading(),
    which checks whether the URL host matches a specific domain (as defined above).
    If it does match, then the method returns false in order to not override the URL
    loading (it allows the WebView to load the URL as usual). If the URL host does
    not match, then an Intent is created to launch the default Activity for handling
    URLs (which resolves to the user's default web browser). */

}
