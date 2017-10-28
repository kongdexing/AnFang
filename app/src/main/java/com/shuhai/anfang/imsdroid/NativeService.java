/* Copyright (C) 2010-2011, Mamadou Diop.
*  Copyright (C) 2011, Doubango Telecom.
*
* Contact: Mamadou Diop <diopmamadou(at)doubango(dot)org>
*	
* This file is part of imsdroid Project (http://code.google.com/p/imsdroid)
*
* imsdroid is free software: you can redistribute it and/or modify it under the terms of 
* the GNU General Public License as published by the Free Software Foundation, either version 3 
* of the License, or (at your option) any later version.
*	
* imsdroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
* See the GNU General Public License for more details.
*	
* You should have received a copy of the GNU General Public License along 
* with this program; if not, write to the Free Software Foundation, Inc., 
* 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package com.shuhai.anfang.imsdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

import com.coolerfall.daemon.Daemon;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.ExtraKey;

import org.doubango.ngn.NgnNativeService;
import org.doubango.ngn.events.NgnEventArgs;
import org.doubango.ngn.events.NgnInviteEventArgs;
import org.doubango.ngn.events.NgnMessagingEventArgs;
import org.doubango.ngn.events.NgnMsrpEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventTypes;
import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.sip.NgnAVSession;

public class NativeService extends NgnNativeService {
    private final static String TAG = NativeService.class.getSimpleName();

    private PowerManager.WakeLock mWakeLock;
    private Engine mEngine;

    public NativeService() {
        super();
        try {
            mEngine = (Engine) Engine.getInstance();
        } catch (Exception ex) {
            Log.i(TAG, "NativeService exception: " + ex.getMessage());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        mEngine = (Engine) Engine.getInstance();
        if (mEngine == null) {
            Log.i(TAG, "onCreate mEngine is null: ");
            return;
        }
        Daemon.run(NativeService.this,
                NativeService.class, Daemon.INTERVAL_ONE_MINUTE);

        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null && mWakeLock == null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.ON_AFTER_RELEASE
                    | PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart()");
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT);
        intentFilter.addAction(NgnInviteEventArgs.ACTION_INVITE_EVENT);
        intentFilter.addAction(NgnMessagingEventArgs.ACTION_MESSAGING_EVENT);
        intentFilter.addAction(NgnMsrpEventArgs.ACTION_MSRP_EVENT);
        registerReceiver(mBroadcastReceiver, intentFilter);

        if (mEngine.start()) {
            mEngine.getSipService().register(null);
        }
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @SuppressWarnings("incomplete-switch")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            // Registration Events
            if (NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT.equals(action)) {
                NgnRegistrationEventArgs args = intent.getParcelableExtra(NgnEventArgs.EXTRA_EMBEDDED);
                final NgnRegistrationEventTypes type;
                if (args == null) {
                    Log.e(TAG, "Invalid event args");
                    return;
                }
                Log.i(TAG, "ACTION_REGISTRATION_EVENT onReceive: " + args.getEventType());
                switch ((type = args.getEventType())) {
                    case REGISTRATION_OK:
                        NetWorkStatusChangeHelper.getInstance().initNetWorkChange();
                    case REGISTRATION_NOK:
                    case REGISTRATION_INPROGRESS:
                    case UNREGISTRATION_INPROGRESS:
                    case UNREGISTRATION_OK:
                    case UNREGISTRATION_NOK:
                    default:
                        break;
                }
            } else if (NgnInviteEventArgs.ACTION_INVITE_EVENT.equals(action)) {
                NgnInviteEventArgs args = intent.getParcelableExtra(NgnEventArgs.EXTRA_EMBEDDED);
                if (args == null) {
                    Log.e(TAG, "Invalid event args");
                    return;
                }

                final NgnMediaType mediaType = args.getMediaType();
                Log.i(TAG, "ACTION_INVITE_EVENT onReceive: " + args.getEventType());
                switch (args.getEventType()) {
                    case TERMWAIT:
                    case TERMINATED:
                        if (NgnMediaType.isAudioVideoType(mediaType)) {
                            mEngine.refreshAVCallNotif(R.drawable.phone_call_25);
                            mEngine.getSoundService().stopRingBackTone();
                            mEngine.getSoundService().stopRingTone();
                        }
                        break;
                    case INCOMING:
                        if (NgnMediaType.isAudioVideoType(mediaType)) {
                            final NgnAVSession avSession = NgnAVSession.getSession(args.getSessionId());
                            if (avSession != null) {
//                                mEngine.showAVCallNotif(R.drawable.phone_call_25, getString(R.string.string_call_incoming));
                                int session_size = NgnAVSession.getSessions().size();
                                Log.i(TAG, "incoming session_size: " + session_size + " uri:" + avSession.getRemotePartyDisplayName());
                                if (session_size >= 2) {
                                    avSession.hangUpCall();

//                                    Intent b = new Intent(BroadcastAction.VIDEO_INCOMING);
//                                    b.putExtra(CallScreen.EXTRAT_CALL_TYPE, "incoming");
//                                    b.putExtra(CallScreen.EXTRAT_SIP_SESSION_ID, avSession.getId());
//                                    sendBroadcast(b);

                                    return;
                                } else {
                                    Intent i = new Intent();
                                    i.setClass(NativeService.this, CallScreen.class);
                                    i.putExtra(ExtraKey.EXTRAT_CALL_TYPE, "incoming");
                                    i.putExtra(ExtraKey.EXTRAT_SIP_SESSION_ID, avSession.getId());
//                                i.putExtra(EXTRAT_TEACHER_ID, null);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                                if (mWakeLock != null && !mWakeLock.isHeld()) {
                                    mWakeLock.acquire(10);
                                }
                                mEngine.getSoundService().startRingTone();
                            } else {
//                                Log.e(TAG, String.format("Failed to find session with id=%ld", args.getSessionId()));
                            }
                        }
                        break;
                    case INPROGRESS:
                        if (NgnMediaType.isAudioVideoType(mediaType)) {
                            mEngine.showAVCallNotif(R.drawable.phone_call_25, getString(R.string.string_call_outgoing));
                        }
                        break;
                    case RINGING:
                        if (NgnMediaType.isAudioVideoType(mediaType)) {
                            mEngine.getSoundService().startRingBackTone();
                        }
                        break;
                    case CONNECTED:
                    case EARLY_MEDIA:
                        if (NgnMediaType.isAudioVideoType(mediaType)) {
                            mEngine.showAVCallNotif(R.drawable.phone_call_25, getString(R.string.string_incall));
                            mEngine.getSoundService().stopRingBackTone();
                            mEngine.getSoundService().stopRingTone();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        if (mBroadcastReceiver != null) {
            try {
                unregisterReceiver(mBroadcastReceiver);
            } catch (Exception ex) {
                Log.i(TAG, "onDestroy: not registered");
            }
            mBroadcastReceiver = null;
        }

        if (null != mEngine) {
            mEngine.getSipService().unRegister();
        }

        if (mWakeLock != null) {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
                mWakeLock = null;
            }
        }
    }
}
