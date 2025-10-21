package com.gk.world.comprehensive.receivers

import android.content.Context
import android.content.Intent
import android.widget.Toast
import cn.jpush.android.api.*
import cn.jpush.android.service.JPushMessageReceiver
import com.drake.channel.sendEvent
import com.gxd.jpush.TagAliasOperatorHelper
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.arouter.guest.MessageTypeEnum
import com.gk.world.resouce.arouter.guest.VisitorCallMessage
import timber.log.Timber

class PushMessageReceiver : JPushMessageReceiver() {

    private val TAG = "PushMessageReceiver"

    override fun onMessage(context: Context?, customMessage: CustomMessage?) {
//        super.onMessage(p0, p1)

        Timber.e(TAG, "[onMessage] $customMessage")
        //       Intent intent = new Intent("com.jiguang.demo.message");
//        intent.putExtra("msg", customMessage.message);
//        context.sendBroadcast(intent);
        val visitorCallBean = VisitorCallMessage(MessageTypeEnum.Update)
        sendEvent(visitorCallBean, ARouterConstance.Main.GTASKS_FRAGMENT)
        sendEvent(visitorCallBean, ARouterConstance.Main.REPAIR_ORDER_FRAGMENT)
    }

    override fun onNotifyMessageOpened(context: Context?, message: NotificationMessage) {
        Timber.e(TAG, "[onNotifyMessageOpened] $message")
        try {
            Toast.makeText(context, "已更新首页任务", Toast.LENGTH_SHORT).show()
            //打开自定义的Activity
            /* Intent i = new Intent(context, TestActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE,message.notificationTitle);
            bundle.putString(JPushInterface.EXTRA_ALERT,message.notificationContent);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(i);*/
        } catch (throwable: Throwable) {
        }
    }

    override fun onMultiActionClicked(context: Context?, intent: Intent) {
        Timber.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮")
        val nActionExtra = intent.extras!!.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Timber.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null")
            return
        }
        if (nActionExtra == "my_extra1") {
            Timber.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一")
        } else if (nActionExtra == "my_extra2") {
            Timber.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二")
        } else if (nActionExtra == "my_extra3") {
            Timber.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三")
        } else {
            Timber.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义")
        }
    }

    override fun onNotifyMessageArrived(context: Context?, message: NotificationMessage) {
        Timber.e(TAG, "[onNotifyMessageArrived] $message")
    }

    override fun onNotifyMessageDismiss(context: Context?, message: NotificationMessage) {
        Timber.e(TAG, "[onNotifyMessageDismiss] $message")
    }

    override fun onRegister(context: Context, registrationId: String) {
        Timber.e(TAG, "[onRegister] $registrationId")
        val intent = Intent("com.jiguang.demo.register")
        context.sendBroadcast(intent)
    }

    override fun onConnected(context: Context?, isConnected: Boolean) {
        Timber.e(TAG, "[onConnected] $isConnected")
    }

    override fun onCommandResult(context: Context?, cmdMessage: CmdMessage) {
        Timber.e(TAG, "[onCommandResult] $cmdMessage")
    }

    override fun onTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage)
        super.onTagOperatorResult(context, jPushMessage)
    }

    override fun onCheckTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage)
        super.onCheckTagOperatorResult(context, jPushMessage)
    }

    override fun onAliasOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage)
        super.onAliasOperatorResult(context, jPushMessage)
    }

    override fun onMobileNumberOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage)
        super.onMobileNumberOperatorResult(context, jPushMessage)
    }

    override fun onNotificationSettingsCheck(context: Context?, isOn: Boolean, source: Int) {
        super.onNotificationSettingsCheck(context, isOn, source)
        Timber.e(TAG, "[onNotificationSettingsCheck] isOn:$isOn,source:$source")
    }
}