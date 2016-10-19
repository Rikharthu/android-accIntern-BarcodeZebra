package com.example.android.barcodezebra;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener, ZXingScannerView.ResultHandler {

    public static final String TAG= MainActivity.class.getSimpleName();

//    public static final String URL="https://developer.android.com/guide/webapps/webview.html";
    public static final String MAIN_PAGE="file:///android_asset/index.html";
    private ZXingScannerView mScannerView;
    MainFragment mMainFragment;
    ScannerFragment mScannerFragment;
    private boolean isMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        Fragment currentFragment = manager.findFragmentById(R.id.container);
        if(savedInstanceState==null) {
            // setup
            Log.d(TAG, "creating both fragments");
            mMainFragment = MainFragment.newInstance(this);
            mMainFragment.setListener(this);
            FragmentTransaction transaction = manager.beginTransaction();
            //TODO create string consts
            transaction.replace(R.id.container, mMainFragment, "main_fragment");
            transaction.commit();
            isMainFragment = true;
        }else{
            // mainFragment is guaranteed to be in saveInstanceState
            mMainFragment= (MainFragment) manager.getFragment(savedInstanceState,"main_fragment");
            mMainFragment.setListener(this);
            // but scanner fragment may be null
            mScannerFragment= (ScannerFragment) manager.getFragment(savedInstanceState,"scanner_fragment");
            if(mScannerFragment==null){
                Log.d(TAG,"scanner is null");
            }else{
                Log.d(TAG,"scanner is not null");
                mScannerFragment.setHandler(this);
            }
        }

    }

    //TODO naming
    public void QrScanner(){
        mScannerFragment = ScannerFragment.newInstance(this);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        // FIXME doesnt work with replace
        transaction.add(R.id.container,mScannerFragment,"scanner_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO - don't do so
        //Save the fragment's instance
        FragmentManager manager = getSupportFragmentManager();
        manager.putFragment(outState, "main_fragment", mMainFragment);
        if(manager.findFragmentByTag("scanner_fragment")!=null)
            // persist scanner fragment too, if it is in the manager
            manager.putFragment(outState, "scanner_fragment", mScannerFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mMainFragment.addBarcode("1234567890");
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.d(TAG,"handleResult");
        // Remove scanner fragment from manager
        FragmentManager manager = getSupportFragmentManager();
        getSupportFragmentManager().beginTransaction()
                .remove(mScannerFragment).commit();
        manager.popBackStack();
        // FIXME why it is tellin that scanner is not null?
        if(getSupportFragmentManager().findFragmentByTag("scanner_fragment")==null){
            Log.d(TAG,"scanner is null");
        }else{
            Log.d(TAG,"scanner is not null");
        }
        // Do something with the result here
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // show the scanner result into dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();

//        while(!mMainFragment.isVisible());
        mMainFragment.addBarcode(rawResult.getText());

        // no need, since we know, that there always is MainFragment
//        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container,mMainFragment);
//        transaction.commit();

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }

    //TODO use onBackPressed()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // if current fragment is MainFragment (WebView is initialized)
            if(getCurrentFragment()==mMainFragment && mMainFragment.mWebView.canGoBack()){
                mMainFragment.mWebView.goBack();
                return true;
            }
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);

    }

    private Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    public void onButtonClicked(int code) {
        switch(code){
            case WebAppInterface.SCAN_QR:
                Log.d(TAG,"scanning QR code");
                QrScanner();
                break;
            case 13:
                // mainfragment view created. can interract with WebView
                mMainFragment.addBarcode("Halloy");
                break;
        }
    }
}
