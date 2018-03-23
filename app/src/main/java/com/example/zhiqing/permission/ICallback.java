package com.example.zhiqing.permission;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;


/**
 * Created by zhiqing on 2018/1/26.
 */

abstract class ICallback extends Binder {
    static final String descriptor = "android.os.IBinder";
    static final int CALLBACK = Binder.FIRST_CALL_TRANSACTION + 1;
    private static final String TAG = "PermissionCheck";

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply,
                                 int flags) throws RemoteException {
        if (code == CALLBACK) {
            try {
                data.enforceInterface(descriptor);
                Result result = new Result();
                result.code = data.readInt();
                result.id = data.readString();
                onCallback(result);
                reply.writeNoException();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.w(TAG, "Callback ERROR!");
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

    protected abstract void onCallback(Result result);


    static final class Result {
        public static final int GRANT = 100;
        public static final int DENY = 101;

        private int code;
        private String id;

        public Result(int code, String id) {
            this.code = code;
            this.id = id;
        }

        public Result() {
        }

        public int getCode() {
            return code;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Result {"
                    + "code=" + code
                    + ", id=" + id
                    + "}";
        }
    }

}
