package com.frame.core.util;

import android.Manifest;
import android.app.Activity;
import android.os.Build;

/**
 * 用于兼容6.0权限管理
 * 此处权限为高危权限，需要通知用户授权
 *
 * CALENDAR
 ----------------------
   READ_CALENDAR
   WRITE_CALENDAR

   CAMERA
 ----------------------
   CAMERA

   CONTACTS
 ----------------------
   READ_CONTACTS
   WRITE_CONTACTS
   GET_ACCOUNTS

   LOCATION
 ----------------------
   ACCESS_FINE_LOCATION
   ACCESS_COARSE_LOCATION

   MICROPHONE
 ----------------------
   RECORD_AUDIO

   PHONE
 ----------------------
   READ_PHONE_STATE
   CALL_PHONE
   READ_CALL_LOG
   WRITE_CALL_LOG
   ADD_VOICEMAIL
   USE_SIP
   PROCESS_OUTGOING_CALLS

   SENSORS
 ----------------------
   BODY_SENSORS

   SMS
 ----------------------
   SEND_SMS
   RECEIVE_SMS
   READ_SMS
   RECEIVE_WAP_PUSH
   RECEIVE_MMS

   STORAGE
 ----------------------
   READ_EXTERNAL_STORAGE
   WRITE_EXTERNAL_STORAGE

 * Created by yzd on 2017/2/17 0017.
 */

public class PermissionUtil {

    public static void checkReadCalendarPermission(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.READ_CALENDAR, requestCode);
    }

    public static void checkWriteCalendarPermission(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.WRITE_CALENDAR, requestCode);
    }

    public static void checkCamera(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.CAMERA, requestCode);
    }

    public static void checkReadContacts(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.READ_CONTACTS, requestCode);
    }

    public static void checkWriteContacts(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.WRITE_CONTACTS, requestCode);
    }

    public static void checkGetAccounts(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.GET_ACCOUNTS, requestCode);
    }

    public static void checkAccessFineLocation(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, requestCode);
    }

    public static void checkAccessCoarseLocation(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION, requestCode);
    }

    public static void checkRecordAudio(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.RECORD_AUDIO, requestCode);
    }

    public static void checkReadPhoneState(Activity activity, int reqeustCode) {
        AppHelper.requestPermission(activity, Manifest.permission.READ_PHONE_STATE, reqeustCode);
    }

    public static void checkCallPhone(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.CALL_PHONE, requestCode);
    }

    public static void checkReadCallLog(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.READ_CALL_LOG, requestCode);
    }

    public static void checkWriteCallLog(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.WRITE_CALL_LOG, requestCode);
    }

    public static void checkAddVoiceMail(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.ADD_VOICEMAIL, requestCode);
    }

    public static void checkUseSip(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.USE_SIP, requestCode);
    }

    public static void checkProcessOutGoingCalls(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.PROCESS_OUTGOING_CALLS, requestCode);
    }

    public static void checkBodySensors(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            AppHelper.requestPermission(activity, Manifest.permission.BODY_SENSORS, requestCode);
        }
    }

    public static void checkSendSms(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.SEND_SMS, requestCode);
    }

    public static void checkReceiveSms(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.RECEIVE_SMS, requestCode);
    }

    public static void checkReadSms(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.READ_SMS, requestCode);
    }

    public static void checkReceiveWapPush(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.RECEIVE_WAP_PUSH, requestCode);
    }

    public static void checkReceiveMms(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.RECEIVE_MMS, requestCode);
    }

    public static void checkReadExternalStorage(Activity activity, int requestcode) {
        AppHelper.requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, requestcode);
    }

    public static void checkWriteExternalStorage(Activity activity, int requestCode) {
        AppHelper.requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode);
    }
}
