package com.example.android.barcodezebra;

import android.content.Intent;
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

    public static final String URL="https://developer.android.com/guide/webapps/webview.html";
    public static final String MAIN_PAGE="file:///android_asset/index.html";
    private ZXingScannerView mScannerView;
    MainFragment mMainFragment;
    ScannerFragment mScannerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFragment = MainFragment.newInstance(this);
        mScannerFragment=ScannerFragment.newInstance(this);
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,mMainFragment);
        transaction.commit();

    }

    public void QrScanner(){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,mScannerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here</p>
        Log.e("handler", rawResult.getText()); // Prints scan results<br />
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)</p>
        // show the scanner result into dialog box.<br />
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        // TODO zamenit na metod fragmenta
        Bundle args = new Bundle();
        args.putString("barcode",rawResult.getText());
//        mMainFragment.setArguments(args);
        transaction.replace(R.id.container,mMainFragment);
        transaction.commit();

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        QrScanner();
//        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
//        Intent chooserIntent = Intent.createChooser(intent, "Select Application");
//        startActivityForResult(chooserIntent, 0);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mMainFragment.mWebView.canGoBack()) {
            mMainFragment.mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onButtonClicked(int code) {
        switch(code){
            case WebAppInterface.SCAN_QR:
                Log.d(TAG,"scanning QR code");
                QrScanner();
                break;
        }
    }
}
