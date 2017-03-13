package com.iadbox.cordova.plugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.net.Uri;

import com.google.firebase.iid.FirebaseInstanceId;
import com.qustodian.sdk.*;
import com.qustodian.sdk.androidutils.ShareHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class iadboxPlugin extends CordovaPlugin {

    private static final String LOGTAG = "iadboxPlugin";

    private static final String ACTION_CREATE_USER = "createUser";
    private static final String ACTION_CREATE_SESSION = "createSession";
    private static final String ACTION_OPEN_INBOX = "openInbox";
    private static final String ACTION_GET_BADGE = "getBadge";
    private static final String ACTION_GET_URL = "getUrl";
    private static final String ACTION_CUSTOMIZE = "customize";
    private static final String ACTION_OPEN_URL = "openUrlWithExternBrowser";
    private static final String ACTION_OPEN_SHARE = "openShareAction";
    private static final String ACTION_DISABLE_BACK = "disableTopBarBackButton";
    private static final String ACTION_ENABLE_EXIT_ON_BACK = "enableExitAppOnBack";

    private static final String OPT_EXTERNAL_ID = "externalId";
    private static final String OPT_AFFILIATE_ID = "affiliateId";
    private static final String OPT_PUSH_ID = "pushId";
    private static final String OPT_BORDER_COLOR = "borderColor";
    private static final String OPT_TITLE = "title";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(LOGTAG, "Plugin initialized");
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(LOGTAG, "executing: " + action);
        PluginResult result = null;
        try {
            if (ACTION_CREATE_USER.equals(action)) {
                result = createUser(args, callbackContext);
            } else if (ACTION_CREATE_SESSION.equals(action)) {
                result = createSession(args, callbackContext);
            } else if (ACTION_OPEN_INBOX.equals(action)) {
                result = openInbox(callbackContext);
            } else if (ACTION_GET_BADGE.equals(action)) {
                result = getBadge(callbackContext);
            } else if (ACTION_GET_URL.equals(action)) {
                result = getUrl(args, callbackContext);
            } else if (ACTION_OPEN_URL.equals(action)) {
                result = openUrlWithExternBrowser(args, callbackContext);
            } else if (ACTION_OPEN_SHARE.equals(action)) {
                result = openShareAction(args, callbackContext);
            } else if (ACTION_CUSTOMIZE.equals(action)) {
                result = customize(args, callbackContext);
            } else if (ACTION_DISABLE_BACK.equals(action)) {
                result = disableTopBarBackButton(callbackContext);
            } else if (ACTION_ENABLE_EXIT_ON_BACK.equals(action)) {
                result = enableExitAppOnBack(callbackContext);
            }
        } catch (JSONException e) {
            callbackContext.error(logException(e));
        } catch (Exception e) {
            callbackContext.error(logException(e));
        }

        if (result != null) callbackContext.sendPluginResult( result );

        return true;
    }

    private PluginResult createUser(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);

        final String affiliateId = obj.getString(OPT_AFFILIATE_ID);
        final String externalId = obj.getString(OPT_EXTERNAL_ID);
        final String pushId = obj.getString(OPT_PUSH_ID);

        if (affiliateId == "") {
            throw new Exception("Affiliate ID is required");
        }

        cordova.getActivity().runOnUiThread(runCreateUser(affiliateId, externalId, pushId, callbackContext));

        return null;
    }

    private PluginResult createSession(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);

        final String affiliateId = obj.getString(OPT_AFFILIATE_ID);
        final String externalId = obj.getString(OPT_EXTERNAL_ID);
        final String pushId = obj.getString(OPT_PUSH_ID);

        if (affiliateId == "") {
            throw new Exception("Affiliate ID is required");
        }

        cordova.getActivity().runOnUiThread(runCreateSession(affiliateId, externalId, pushId, callbackContext));

        return null;
    }

    private PluginResult openInbox(final CallbackContext callbackContext) throws Exception, JSONException {
        cordova.getActivity().runOnUiThread(runOpenInbox(callbackContext));

        return null;
    }

    private PluginResult getBadge(final CallbackContext callbackContext) throws Exception, JSONException {
        cordova.getActivity().runOnUiThread(runGetBadge(callbackContext));

        return null;
    }

    private PluginResult getUrl(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);
        String action = obj.getString("action");
        
        cordova.getActivity().runOnUiThread(runGetUrl(action, callbackContext));

        return null;
    }

    private PluginResult openUrlWithExternBrowser(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);
        String url = obj.getString("url");
        
        cordova.getActivity().runOnUiThread(runOpenUrlWithExternBrowser(url, callbackContext));

        return null;
    }

    private PluginResult openShareAction(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);
        String title = obj.getString("title");
        String url = obj.getString("url");
        String image = obj.getString("image");
        
        cordova.getActivity().runOnUiThread(runOpenShareAction(title, url, image, callbackContext));

        return null;
    }

    private PluginResult disableTopBarBackButton(final CallbackContext callbackContext) throws Exception, JSONException {
        cordova.getActivity().runOnUiThread(runDisableTopBarBackButton(callbackContext));

        return null;
    }

    private PluginResult enableExitAppOnBack(final CallbackContext callbackContext) throws Exception, JSONException {
        cordova.getActivity().runOnUiThread(runEnableExitAppOnBack(callbackContext));

        return null;
    }

    private PluginResult customize(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);

        final String borderColor = obj.getString(OPT_BORDER_COLOR);
        final String title = obj.getString(OPT_TITLE);

        cordova.getActivity().runOnUiThread(runCustomize(borderColor, title, callbackContext));

        return null;
    }

    private Runnable runCreateUser(final String affiliateId, final String externalId, final String pushDeviceRegistrationId, final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Qustodian.getInstance(cordova.getActivity())
                        .createUser(cordova.getActivity(),
                            externalId,
                            affiliateId,
                            pushDeviceRegistrationId,
                            new OnResponseListener() {
                                @Override
                                public void onSuccess(String s) {
                                    Log.d(LOGTAG, "createUser: onResponseListener onSuccess: " + s);
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String user_id = jsonObject.getJSONObject("session").getString("user_id");
                                        callbackContext.success(user_id);
                                    } catch(JSONException e) {
                                        callbackContext.error(logException(e));
                                    }
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.e(LOGTAG, "createUser: onResponseListener onError " + s);
                                    callbackContext.error(s);
                                }
                            });

                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runCreateSession(final String affiliateId, final String externalId, final String pushDeviceRegistrationId, final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Qustodian.getInstance(cordova.getActivity())
                        .createSession(cordova.getActivity(),
                            externalId,
                            affiliateId,
                            pushDeviceRegistrationId,
                            new OnResponseListener() {
                                @Override
                                public void onSuccess(String s) {
                                    Log.d(LOGTAG, "createSession: onResponseListener onSuccess: " + s);
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String user_id = jsonObject.getJSONObject("session").getString("user_id");
                                        callbackContext.success(user_id);
                                    } catch(JSONException e) {
                                        callbackContext.error(logException(e));
                                    }
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.e(LOGTAG, "createSession: onResponseListener onError " + s);
                                    callbackContext.error(s);
                                }
                            });

                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runOpenInbox(final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    Qustodian.getInstance(context).openInbox(context);
                    callbackContext.success();
                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runGetBadge(final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    Qustodian.getInstance(context)
                        .getMessagesCount(context,
                            new OnResponseMessagesCountListener() {
                                @Override
                                public void onSuccess(int messageCount) {
                                    Log.d(LOGTAG, "OnResponseMessagesCountListener onSuccess: " + messageCount);
                                    callbackContext.success(messageCount);
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.e(LOGTAG, "OnResponseMessagesCountListener onError " + s);
                                    callbackContext.error(s);
                                }
                            });
                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runGetUrl(final String action, final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    Qustodian.getInstance(context).getUrl(action, context,
                            new OnResponseListener() {
                                @Override
                                public void onSuccess(String url) {
                                    Log.d(LOGTAG, "OnResponseListener(runGetInboxUrl) onSuccess: " + url);
                                    callbackContext.success(url);
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.e(LOGTAG, "OnResponseListener(runGetInboxUrl) onError " + s);
                                    callbackContext.error(s);
                                }
                            });
                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runOpenUrlWithExternBrowser(final String url, final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                    callbackContext.success();
                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runOpenShareAction(final String title, final String url, final String image, final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    ShareHelper.shareStringWithoutImage(cordova.getActivity(), title, url);
                    callbackContext.success();
                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runDisableTopBarBackButton(final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    Qustodian.getInstance(context).disableTopBarBackButton();
                    callbackContext.success();
                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runEnableExitAppOnBack(final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    Qustodian.getInstance(context).enableExitAppOnBack();
                    callbackContext.success();
                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private Runnable runCustomize(final String borderColor, final String title, final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    if (borderColor != "") {
                        int color = Color.parseColor(borderColor);
                        Qustodian.getInstance(context).setBorderColor(color);
                    }
                    if (title != "") {
                        Qustodian.getInstance(context).setTitle(title);
                    }
                    callbackContext.success();
                } catch (RuntimeException e) {
                    callbackContext.error(logException(e));
                }
            }
        };
    }

    private String logException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String errorMessage = e.getMessage() + "\n" + sw.toString();
        Log.e(LOGTAG, errorMessage);
        return errorMessage;
    }
}