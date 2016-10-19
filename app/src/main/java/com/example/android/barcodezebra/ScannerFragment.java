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

    private ZXingScannerView.ResultHandler mResultHandler;
    private Context mContext;
    ZXingScannerView mScannerView;

    public static ScannerFragment newInstance(Context c){
        ScannerFragment fragment = new ScannerFragment();
        fragment.mContext=c;

        return fragment;
    }

    public void setHandler(ZXingScannerView.ResultHandler handler){
        mResultHandler=handler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SCANNER_FRAGMENT","onCreate()");
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
        // start scanning for barcodes
        ((ZXingScannerView)view).startCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ScannerFragment","onDestroy()");
    }
}
