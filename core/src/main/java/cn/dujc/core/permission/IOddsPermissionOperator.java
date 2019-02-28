package cn.dujc.core.permission;

import android.content.Context;

/**
 * @author du
 * <br />
 * 权限组	权限
 * CALENDAR
 * •	READ_CALENDAR
 * •	WRITE_CALENDAR
 * CAMERA
 * •	CAMERA
 * CONTACTS
 * •	READ_CONTACTS
 * •	WRITE_CONTACTS
 * •	GET_ACCOUNTS
 * LOCATION
 * •	ACCESS_FINE_LOCATION
 * •	ACCESS_COARSE_LOCATION
 * MICROPHONE
 * •	RECORD_AUDIO
 * PHONE
 * •	READ_PHONE_STATE
 * •	CALL_PHONE
 * •	READ_CALL_LOG
 * •	WRITE_CALL_LOG
 * •	ADD_VOICEMAIL
 * •	USE_SIP
 * •	PROCESS_OUTGOING_CALLS
 * SENSORS
 * •	BODY_SENSORS
 * SMS
 * •	SEND_SMS
 * •	RECEIVE_SMS
 * •	READ_SMS
 * •	RECEIVE_WAP_PUSH
 * •	RECEIVE_MMS
 * STORAGE
 * •	READ_EXTERNAL_STORAGE
 * •	WRITE_EXTERNAL_STORAGE
 * <p>
 * <p>
 * <br />
 * date 2018/9/5 上午10:10
 */
public interface IOddsPermissionOperator {

    /**
     * 是否采用特异机型的权限处理方案
     */
    boolean useOddsPermissionOperate(Context context);

    /**
     * 特异的申请权限方法，低水平的狗X的rom，都是在使用权限时才申请权限，所以权限判断和请求权限都在这一个方法里
     */
    boolean requestPermissions(int requestCode, String title, String message, String... permissions);

    /**
     * 权限处理流程是否到此结束，此方法在{@link #requestPermissions(int, String, String, String...)}后调用
     */
    boolean doneHere(String... permissions);

    /**
     * 是否显示默认流程的确认对话框
     */
    boolean showConfirmDialog(String... permissions);

}
