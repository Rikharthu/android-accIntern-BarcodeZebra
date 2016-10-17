package com.example.android.barcodezebra;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment {

    private ZXingScannerView.ResultHandler mResultHandler;
    private Context mContext;

    public static ScannerFragment newInstance(Context c){
        ScannerFragment fragment = new ScannerFragment();
        fragment.mContext=c;
        fragment.mResultHandler= (ZXingScannerView.ResultHandler) c;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ZXingScannerView view=new ZXingScannerView(mContext);
        view.setResultHandler(mResultHandler); // Register ourselves as a handler for scan results.
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ZXingScannerView)view).startCamera();
    }
}
