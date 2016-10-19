package com.example.android.barcodezebra;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.ArrayList;

public class MainFragment extends Fragment implements WebAppInterface.WebAppListener {

    interface MainFragmentListener{
        void onButtonClicked(int code);
    }

    public static final String MAIN_PAGE="file:///android_asset/index.html";

    WebView mWebView;
    private Context mContext;
    private MainFragmentListener mListener;
    private ArrayList<String> mBarcodes;
    private MyWebViewClient mClient;

    public static MainFragment newInstance(Context c){
        MainFragment fragment = new MainFragment();
        fragment.mContext=c;
        return fragment;
    }

    public void setListener(MainFragmentListener listener){
        mListener=listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainFragment","onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("MainFragment","onCreateView");
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        mWebView = (WebView) view.findViewById(R.id.webview);

        // enable javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // bind JavaScript to WebAppInterface with name "Android"
        /* we set this interface's name "Android" so in javascript we can refer to this class's methods
        as Android.showToast(...). Javascript interpretes this as Class name, whose methods it can call */
        // IMPORTANT! This objects runs in a separate thread!
        //TODO store name at WebAppInterface
        mWebView.addJavascriptInterface(new WebAppInterface(getContext(),this), "Android");

//        mWebView.setWebViewClient(new WebViewClient() {
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                mWebView.loadUrl("file:///android_asset/myerrorpage.html");
//            }
//        });
        mClient = new MyWebViewClient();
        mWebView.setWebViewClient(mClient);
        if(savedInstanceState!=null){
            //TODO store keys properly
            mBarcodes=savedInstanceState.getStringArrayList("barcodes");
            if(mBarcodes!=null) {
                // notify WebViewClient to restore barcodes when WebView finishes rendering (when javascript becomes available)
                mClient.restoreBarcodes(mBarcodes);
            }

        }
        mWebView.loadUrl(MAIN_PAGE);
        return view;
    }




    @Override
    public void onClick(int c) {
        Log.d("MainFragment","onClick()");
        // execute javascript function in a webview
        // since webview views run on a separate thread
//        addBarcode("11116661488 ");
        // all WebView methods must be called on the same thread
        mWebView.post(new Runnable() {
            @Override
            public void run() {
//                mWebView.loadUrl("javascript:setOutput('TESTTESTTEST')");
//                mWebView.evaluateJavascript("setOutput('TEST TEST TEST')",null);
//                mWebView.evaluateJavascript("document.getElementById('output').innerHTML = 'TEST TEST';",null);
            }
        });

        switch(c){
            case WebAppInterface.SCAN_QR:
                Log.d("MainFragment","scanning QR code");
                // notify activity that it should start QR scanner
                mListener.onButtonClicked(c);
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Instance",""+this.toString());
    }

    public void addBarcode(final String barcode){
        Log.d("Instance",""+this.toString());

        if(mBarcodes==null){
            mBarcodes=new ArrayList<>();
        }
        mBarcodes.add(barcode);
        // at that point mWebView is fully rendered. I hope so.
        mWebView.post(new Runnable() {
            @Override
            public void run() {
//                mWebView.evaluateJavascript("setOutput('TEST TEST TEST')",null);
                mWebView.loadUrl("javascript:addBarcodeJS('"+barcode+"')");
//                mWebView.evaluateJavascript("addBarcodeJS('"+barcode+"')",null);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO use keys properly
        outState.putStringArrayList("barcodes",mBarcodes);
        super.onSaveInstanceState(outState);
    }



}
