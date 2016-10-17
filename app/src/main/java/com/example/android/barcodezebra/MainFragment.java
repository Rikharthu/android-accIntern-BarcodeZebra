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

public class MainFragment extends Fragment implements WebAppInterface.WebAppListener {

    interface MainFragmentListener{
        void onButtonClicked(int code);
    }

    public static final String MAIN_PAGE="file:///android_asset/index.html";

    WebView mWebView;
    private Context mContext;
    private MainFragmentListener mListener;

    public static MainFragment newInstance(Context c){
        MainFragment fragment = new MainFragment();
        fragment.mContext=c;
        fragment.mListener= (MainFragmentListener) c;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        mWebView = (WebView) view.findViewById(R.id.webview);

        // enable javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // bind JavaScript to WebAppInterface with name "Android"
        /* we set this interface's name "Android" so in javascript we can refer to this class's methods
        as Android.showToast(...). Javascript interpretes this as Class name, whose methods it can call */
        // IMPORTANT! This objects runs in a separate thread!
        mWebView.addJavascriptInterface(new WebAppInterface(mContext,this), "Android");

//        mWebView.setWebViewClient(new WebViewClient() {
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                mWebView.loadUrl("file:///android_asset/myerrorpage.html");
//            }
//        });
        MyWebViewClient client = new MyWebViewClient();
        mWebView.setWebViewClient(client);
        mWebView.loadUrl(MAIN_PAGE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args=getArguments();
        if(args!=null) {
            // we have the barcode
            String text = args.getString("barcode");
            Log.d("MainFragment", text);
        }
    }

    @Override
    public void onClick(int c) {
        Log.d("MainFragment","onClick()");
        // execute javascript function in a webview
        // since webview views run on a separate thread
        mWebView.post(new Runnable() {
            @Override
            public void run() {
//                mWebView.loadUrl("javascript:setOutput('TESTTESTTEST')");
                mWebView.evaluateJavascript("setOutput('TEST TEST TEST')",null);
//                mWebView.evaluateJavascript("document.getElementById('output').innerHTML = 'TEST TEST';",null);
            }
        });

        switch(c){
            case WebAppInterface.SCAN_QR:
                Log.d("MainFragment","scanning QR code");
                // notify activity
                mListener.onButtonClicked(c);
                break;
        }
    }
}
