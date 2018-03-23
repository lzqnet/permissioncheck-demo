package com.example.zhiqing.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.UUID;


/**
 * Created by zhiqing on 2017/12/20.
 */

public class PermissionManager {
    public static final String REQUESTED_DATA = "data";
    public static final String REQUESTED_CALLBACK = "callback";
    private static final String TAG = "PermissionCheck";
    public static String REQUESTED_PERMISSION = "permission";
    public static String REQUEST_ID="request_id";
    private ICallback mICallback;
    private final HashMap<String ,Request> mProcessing  = new HashMap<>();

    private PermissionGrant mPermissionGrant;

    public PermissionManager() {
        init();
    }

    private void init() {
        mPermissionGrant = new PermissionGrant();
        mICallback = new ICallback() {
            @Override
            protected void onCallback(Result result) {
                Log.d(TAG, "PermissionManager onCallback: result=" + result);
                if (mProcessing.get(result.getId()) != null && mProcessing.get(result.getId()).handler!=null) {
                    mProcessing.get(result.getId()).handler.handlePermissionResult(result.getCode() == Result.GRANT);
                    mProcessing.remove(result.getId());
                } else {
                    Log.d(TAG, "PermissionManager onCallback: no handler");
                }
            }
        };
    }

    public void checkRequestedPermissions(Context context, String[] permissions, IPermissionResultHandler handler) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d(TAG, "checkRequestedPermissions: api is " + Build.VERSION.SDK_INT);
            if(handler!=null){
                handler.handlePermissionResult(true);
            }
            return;
        }

        Log.d(TAG, "PermissionManager checkRequestedPermissions: ");
        if (!mPermissionGrant.hasRequiredPermissions(context, permissions)) {
            Log.d(TAG, "PermissionManager checkRequestedPermissions: do check");
            redirectToPermissionGuideUI(context, permissions,handler);
        } else {
            handler.handlePermissionResult(true);
            Log.d(TAG, "PermissionManager checkRequestedPermissions: successful");
        }
    }

    public void checkRequestedPermissions(Context context, String[] permissions) {
        checkRequestedPermissions(context,permissions,null);
    }

    private void redirectToPermissionGuideUI(Context context, String[] permissions,IPermissionResultHandler handler) {
        Log.d(TAG, "PermissionManager redirectToPermissionGuideUI: ");
        try {
            Request request=new Request(context,permissions,handler);
            Intent intent = new Intent(context, PermissionGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBinder(REQUESTED_CALLBACK, mICallback);
            bundle.putStringArray(REQUESTED_PERMISSION, permissions);
            bundle.putString(REQUEST_ID,request.requestId);
            intent.putExtra(REQUESTED_DATA, bundle);
            mProcessing.put(request.requestId,request);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.d(TAG, "redirectToPermissionGuideUI: ", e);
            e.printStackTrace();
        }
    }

    private class Request{
        Context context;
        String[] permissions;
        IPermissionResultHandler handler;
        String requestId;

        public Request(Context context, String[] permissions, IPermissionResultHandler handler) {
            this.context = context;
            this.permissions = permissions;
            this.handler = handler;
            requestId= UUID.randomUUID().toString();
        }

    }

}
