package com.example.zhiqing.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.example.zhiqing.testpermission.R;


public class PermissionGuideActivity extends Activity {
    private static final String TAG = "PermissionCheck";
    IBinder mCallback;
    private PermissionGrant mPermissionGrant;
    private String[] mPermissions;
    private String mRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_guide);
        init();
    }

    private void init() {
        Log.d(TAG, "PermissionGuideActivity init: ");
        mPermissionGrant = new PermissionGrant();
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(PermissionManager.REQUESTED_DATA);
            mCallback = bundle.getBinder(PermissionManager.REQUESTED_CALLBACK);
            mPermissions = bundle.getStringArray(PermissionManager.REQUESTED_PERMISSION);
            mRequestId=bundle.getString(PermissionManager.REQUEST_ID);
        } catch (Exception e) {
            Log.e(TAG, "PermissionGuideActivity init: ", e);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "PermissionGuideActivity onResume");
        if (mPermissions != null && !mPermissionGrant.hasRequiredPermissions(this, mPermissions)) {
            mPermissionGrant.tryRequestPermission(this, mPermissions);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "PermissionGuideActivity onStop");
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String permissions[], final int[] grantResults) {
        dumpPermissionState(permissions, grantResults);
        if (requestCode == mPermissionGrant.REQUIRED_PERMISSIONS_REQUEST_CODE) {
            // We do not use grantResults as some of the granted permissions might have been
            // revoked while the permissions dialog box was being shown for the missing permissions.
            if (mPermissionGrant.hasRequiredPermissions(this, mPermissions)) {
                //already get permission
                //setResult(PermissionManager.REQUESTED_RESULT_GRANT);
                sendResult(new ICallback.Result(ICallback.Result.GRANT, mRequestId));
                finish();
                Log.d(TAG, "PermissionGuideActivity onRequestPermissionsResult: PERMISSION ALLOW");
            } else {
                Log.d(TAG, "PermissionGuideActivity onRequestPermissionsResult: PERMISSION DENY");
                //TODO:exit app
                //setResult(PermissionManager.REQUESTED_RESULT_DENY);
                sendResult(new ICallback.Result(ICallback.Result.DENY, mRequestId));
                finish();
            }
        }
    }

    private void sendResult(ICallback.Result result) {
        if (mCallback != null && mCallback.isBinderAlive()) {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(ICallback.descriptor);
            data.writeInt(result.getCode());
            data.writeString(result.getId());
            try {
                mCallback.transact(ICallback.CALLBACK, data, reply, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            reply.readException();
            data.recycle();
            reply.recycle();
        }
    }

    private void dumpPermissionState(final String permissions[], final int[] grantResults) {
        Log.d(TAG, "PermissionGuideActivity PERMISSIONS=" + permissions.toString() + " GRANTRESULTS=" + grantResults.toString());
    }

    private void redirect() {
        //todo:maybe redirect to spec ui
        finish();
    }
}
