package com.example.android.barcodezebra;


import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/** Interface between JavaScript code and Android code */
public class WebAppInterface {
    public static final String TAG=WebAppInterface.class.getSimpleName();

    public static final String JOBECT_NAME="Android";

    public static final int SCAN_QR=1;
    public static final int SAY_HELLO=2;

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
        mListener.onClick(SAY_HELLO);
    }

    @JavascriptInterface
    public void scanQRCode(){
        Log.d(TAG,"scanQRCode()");
        mListener.onClick(SCAN_QR);
    }

    interface WebAppListener{
        /** notifies that click happened on webview button
         * @param code code used to identify which button was clicked
         * SCAN_QR - scan qr code button pressed
         * SAY_HELLO - button that shows toast notification*/
        public void onClick(int code);
    }




}
