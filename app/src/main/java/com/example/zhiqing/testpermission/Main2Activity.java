package com.example.zhiqing.testpermission;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.zhiqing.permission.IPermissionResultHandler;
import com.example.zhiqing.permission.PermissionManager;

public class Main2Activity extends AppCompatActivity {
    PermissionManager mPermissionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mPermissionManager=new PermissionManager();
        mPermissionManager.checkRequestedPermissions(this,
                new String[]{Manifest.permission.CAMERA}, new IPermissionResultHandler() {
                    @Override
                    public void handlePermissionResult(boolean isGrant) {
                        Log.d("hahaha", "CAMERA handlePermissionResult: isGrant=" + isGrant);
                    }
                });
        Log.d("hahaha", "forceCrash: 3");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
