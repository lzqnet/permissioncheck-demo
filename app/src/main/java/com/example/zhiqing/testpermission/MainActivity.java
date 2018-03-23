package com.example.zhiqing.testpermission;

import android.Manifest;
import android.content.Intent;
import android.os.Binder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.zhiqing.permission.IPermissionResultHandler;
import com.example.zhiqing.permission.PermissionManager;

public class MainActivity extends AppCompatActivity {
PermissionManager mPermissionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionManager=new PermissionManager();
    }

    public void checkpermission(View view) {
        Log.d("hahaha", "forceCrash:1 ");
        startActivity(new Intent(this,Main2Activity.class));

        mPermissionManager.checkRequestedPermissions(this,
                new String[]{Manifest.permission.SEND_SMS}, new IPermissionResultHandler() {
                    @Override
                    public void handlePermissionResult(boolean isGrant) {
                        Log.d("hahaha", "SEND_SMS handlePermissionResult: isGrant=" + isGrant);
                    }
                });
        Log.d("hahaha", "forceCrash: 2");
    }
}
