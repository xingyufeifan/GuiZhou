package com.nandi.bdmap.anychat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.nandi.bdmap.utils.LogUtils;
import com.nandi.bdmap.utils.ToastUtils;

/**
 * Created by ChenPeng on 2017/5/31.
 */

public class MyService extends Service implements AnyChatBaseEvent {
    private static String TAG="MyService";
    public AnyChatCoreSDK anyChatSDK;
    private String ip="183.230.108.112";
    private int port=8906;
    private int roomid;
    private int targetid;
    private boolean isOnline;
    private final int LOCALVIDEOAUTOROTATION = 1; // 本地视频自动旋转控制
    private MyBroadReceiver mBroadcastReceiver;
    private Intent stopIntent=new Intent();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        stopIntent.setAction("CLOSE_PROGRESS");
        IntentFilter filter = new IntentFilter();
        filter.addAction("stop_service");
        mBroadcastReceiver = new MyBroadReceiver();
        registerReceiver(mBroadcastReceiver, filter);
        Log.e("MyService", "启动了服务.....");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        InitSDK();
        roomid=intent.getIntExtra("ROOM_ID",1);
        targetid=intent.getIntExtra("USER_ID",0);
        anyChatSDK.Connect(ip,port);
        anyChatSDK.Login("自己","123");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        anyChatSDK.LeaveRoom(-1);
        anyChatSDK.Logout();
        anyChatSDK.Release();
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private void InitSDK() {
        if (anyChatSDK == null) {
            anyChatSDK = AnyChatCoreSDK.getInstance(this);
            anyChatSDK.SetBaseEvent(this);
            anyChatSDK.InitSDK(android.os.Build.VERSION.SDK_INT, 0);
            AnyChatCoreSDK.SetSDKOptionInt(
                    AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION,
                    LOCALVIDEOAUTOROTATION);
        }
    }
    @Override
    public void OnAnyChatConnectMessage(boolean bSuccess) {
        if(bSuccess){
            Log.d(TAG,"连接服务器成功！");
        }else {
            Log.d(TAG,"连接服务器失败！");
            sendBroadcast(stopIntent);
            ToastUtils.showShort(getApplicationContext(),"网络不通畅，正在重新连接...");
        }
    }

    @Override
    public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
        if (dwErrorCode==0){
            LogUtils.d(TAG,"登录成功！");
            anyChatSDK.EnterRoom(roomid,"");
        }else {
            LogUtils.d(TAG,"登录失败！");
        }
    }

    @Override
    public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
        if(dwErrorCode==0){
            LogUtils.d(TAG,"进入房间成功！");
            sendBroadcast(stopIntent);
        }else {
            LogUtils.d(TAG,"进入房间失败！");
            sendBroadcast(stopIntent);
        }
    }

    @Override
    public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
        int[] users=anyChatSDK.GetRoomOnlineUsers(dwRoomId);
        for (int user : users) {
            if (user==targetid){
                isOnline=true;
            }
        }
        if (isOnline){
            Intent intent=new Intent();
            intent.putExtra("USER_ID",targetid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(this,VideoActivity.class);
            startActivity(intent);
        }else {
            ToastUtils.showShort(getApplicationContext(),"回话邀请已过期，或对方不在房间内！");
            stopSelf();
        }
    }

    @Override
    public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {

    }

    @Override
    public void OnAnyChatLinkCloseMessage(int dwErrorCode) {

    }
    private class MyBroadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("stop_service")) {
                stopSelf();
                Log.e("MyBroadReceiver", "收到stop service广播");
            }
        }
    }
}
