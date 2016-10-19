package com.example.android.barcodezebra;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment {
    public static final String TAG=ScannerFragment.class.getSimpleName();

    private ZXingScannerView.ResultHandler mResultHandler;
    ZXingScannerView mScannerView;

    public static ScannerFragment newInstance(){
        ScannerFragment fragment = new ScannerFragment();
        return fragment;
    }

    public void setHandler(ZXingScannerView.ResultHandler handler){
        mResultHandler=handler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mScannerView=new ZXingScannerView(getContext());
       return mScannerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScannerView.setResultHandler((ZXingScannerView.ResultHandler) getActivity()); // Register MainActivity as handler for results
    }

    @Override
    public void onResume() {
        super.onResume();
        // start scanning for barcodes
        mScannerView.startCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
    }
}
