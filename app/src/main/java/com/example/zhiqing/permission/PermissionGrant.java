package com.example.zhiqing.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
/**
 * Created by zhiqing on 2017/11/21.
 */

/**
 * Android OS  PermissionGrant
 */
public class PermissionGrant {
    public static final int REQUIRED_PERMISSIONS_REQUEST_CODE = 1;
    private final String TAG = "PermissionCheck";
    private Hashtable<String, Integer> sPermissions = new Hashtable<String, Integer>();

    public PermissionGrant() {

    }

    /**
     * @return The Android API version of the OS that we're currently running on.
     */
    public int getApiVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * try to request permission.
     */
    public void tryRequestPermission(Activity context, String[] permissions) {
        if (getApiVersion() < Build.VERSION_CODES.M) {
            Log.d(TAG, "PermissionGrant tryRequestPermission api is " + getApiVersion());

            return;
        }
        final String[] missingPermissions = getMissingRequiredPermissions(context, permissions);
        if (missingPermissions.length == 0) {
            return;
        }
        try {
            context.requestPermissions(missingPermissions, REQUIRED_PERMISSIONS_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the app has the specified permission. If it does not, the app needs to use
     * {@link Activity#requestPermission}. Note that if it
     * returns true, it cannot return false in the same process as the OS kills the process when
     * any permission is revoked.
     *
     * @param permission A permission from {@link Manifest.permission}
     */
    private boolean hasPermission(final String permission, Context context) {
        if (getApiVersion() < Build.VERSION_CODES.M) {
            Log.d(TAG, "PermissionGrant hasPermission api is " + getApiVersion());
            return true;
        }
        if (!sPermissions.containsKey(permission)
                || sPermissions.get(permission) == PackageManager.PERMISSION_DENIED) {
            try {
                final int permissionState = context.checkSelfPermission(permission);
                sPermissions.put(permission, permissionState);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sPermissions.get(permission) == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Does the app have all the specified permissions
     */
    private boolean hasPermissions(final String[] permissions, Context context) {
        if (getApiVersion() < Build.VERSION_CODES.M) {
            Log.d(TAG, "PermissionGrant hasPermission api is " + getApiVersion());
            return true;
        }
        for (final String permission : permissions) {
            if (!hasPermission(permission, context)) {
                Log.d(TAG, "PermissionGrant hasPermissions: has no permission=" + permission);
                return false;
            }
        }
        return true;
    }

    /**
     * Returns array with the set of permissions that have not been granted from the given set.
     * The array will be empty if the app has all of the specified permissions. Note that calling
     * {@link Activity#requestPermissions} for an already granted permission can prompt the user
     * again, and its up to the app to only request permissions that are missing.
     */
    private String[] getMissingPermissions(final String[] permissions, Context context) {
        final ArrayList<String> missingList = new ArrayList<String>();
        for (final String permission : permissions) {
            if (!hasPermission(permission, context)) {
                missingList.add(permission);
            }
        }

        final String[] missingArray = new String[missingList.size()];
        missingList.toArray(missingArray);
        return missingArray;
    }

    /**
     * Does the app have the minimum set of permissions required to operate.
     */
    public boolean hasRequiredPermissions(Context context, String[] permissions) {
        Log.d(TAG, "PermissionGrant hasRequiredPermissions: permissions=" + Arrays.toString(permissions));
        if (permissions == null) {
            return true;
        } else {
            return hasPermissions(permissions, context);
        }
    }

    private String[] getMissingRequiredPermissions(Context context, String[] permissions) {
        if (permissions == null) {
            return null;
        } else {
            return getMissingPermissions(permissions, context);
        }
    }
}
