package com.example.android.barcodezebra;


import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/** Interface between JavaScript code and Android code */
public class WebAppInterface {
    private static final String TAG="WebAppInterface";

    public static final int SCAN_QR=1;

    WebAppListener mListener;
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c,WebAppListener listener) {
        mContext = c;
        mListener= listener;
    }

    /** Show a toast from the web page */
    @JavascriptInterface // required for API>=  17
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        mListener.onClick(2);
    }

    @JavascriptInterface
    public void scanQRCode(){
        Log.d(TAG,"scanQRCode()");
        mListener.onClick(SCAN_QR);
    }

    interface WebAppListener{
        public void onClick(int c);
    }


}
