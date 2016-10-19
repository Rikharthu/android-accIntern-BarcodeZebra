package com.example.android.barcodezebra;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener, ZXingScannerView.ResultHandler {

    public static final String TAG= MainActivity.class.getSimpleName();

//    public static final String URL="https://developer.android.com/guide/webapps/webview.html";
    public static final String MAIN_PAGE="file:///android_asset/index.html";
    public static final String MAIN_FRAGMENT = "main_fragment";
    public static final String SCANNER_FRAGMENT = "scanner_fragment";
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
            mMainFragment = MainFragment.newInstance();
            mMainFragment.setListener(this);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, mMainFragment, MAIN_FRAGMENT);
            transaction.commit();
            isMainFragment = true;
        }else{
            // mainFragment is guaranteed to be in saveInstanceState
//            mMainFragment= (MainFragment) manager.getFragment(savedInstanceState,"main_fragment");
            Fragment fragment1 = manager.findFragmentByTag(MAIN_FRAGMENT);
            if (fragment1 != null && fragment1 instanceof MainFragment) {
                mMainFragment= (MainFragment) fragment1;
                mMainFragment.setListener(this);
            }
            // Scanner fragment might not exist
            Fragment fragment2 = manager.findFragmentByTag(SCANNER_FRAGMENT);
            if(fragment2 !=null && fragment2 instanceof ScannerFragment){
                mScannerFragment= (ScannerFragment) fragment2;
                mScannerFragment.setHandler(this);
            }
        }

    }

    public void startScanner(){
        mScannerFragment = ScannerFragment.newInstance();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container,mScannerFragment,SCANNER_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
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

        if(getSupportFragmentManager().findFragmentByTag(SCANNER_FRAGMENT)==null){
            Log.d(TAG,"scanner is null");
        }else{
            Log.d(TAG,"scanner is not null");
        }

        // Do something with the result here
        Log.e(TAG, rawResult.getText()); // Prints scan results
        Log.e(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

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

    @Override
    public void onBackPressed() {
        if(mMainFragment.goBackWV()){
            // we want back in WebView history. Do not call onBackPressed()
            return;
        }
        super.onBackPressed();
    }

    private Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    public void onButtonClicked(int code) {
        switch(code){
            case WebAppInterface.SCAN_QR:
                Log.d(TAG,"scanning QR code");
                startScanner();
                break;
        }
    }
}
