package cn.dujc.core.permission;

import cn.dujc.core.ui.IBaseUI;

/**
 * @author du
 * <br/>
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
 * <br/>
 * date 2018/9/5 上午10:10
 */
public interface IOddsPermissionOperator {

    boolean hasPermission(IBaseUI.IContextCompat context, String... permissions);

    boolean hasCalendar(IBaseUI.IContextCompat context);

    boolean hasCamera(IBaseUI.IContextCompat context);

    boolean hasContacts(IBaseUI.IContextCompat context);

    boolean hasLocation(IBaseUI.IContextCompat context);

    boolean hasMicrophone(IBaseUI.IContextCompat context);

    boolean hasPhone(IBaseUI.IContextCompat context);

    boolean hasSensors(IBaseUI.IContextCompat context);

    boolean hasSMS(IBaseUI.IContextCompat context);

    boolean hasStorage(IBaseUI.IContextCompat context);
}
