# permissioncheck-demo
android 6.0权限检查 采用callback的方式代替原生onRequestPermissionsResult的方式

使用简单:

 mPermissionManager=new PermissionManager();
        mPermissionManager.checkRequestedPermissions(this,
                new String[]{Manifest.permission.CAMERA}, new IPermissionResultHandler() {
                    @Override
                    public void handlePermissionResult(boolean isGrant) {
                        Log.d("hahaha", "CAMERA handlePermissionResult: isGrant=" + isGrant);
                    }
                });
                
                只需要在PermissionManager的checkRequestedPermissions方法里传入IPermissionResultHandler对象.以及要申请的权限.
                在IPermissionResultHandler该接口对象里处理权限申请是否成功即可.详细可以看maina2ctivity的实现
