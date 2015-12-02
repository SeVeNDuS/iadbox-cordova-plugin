package uk.mondosports.plugins.iadbox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.qustodian.sdk.*;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class iAdBoxPlugin extends CordovaPlugin {

    private static final String LOGTAG = "iAdBoxPlugin";
    private static final String DEFAULT_AFFILIATE_ID = "futmondo";

    private static final String ACTION_CREATE_USER_AND_SESSION = "createUserAndSession";
    private static final String ACTION_CREATE_USER = "createUser";
    private static final String ACTION_CREATE_SESSION = "createSession";
    private static final String ACTION_OPEN_INBOX = "openInbox";
    private static final String ACTION_GET_MESSAGES_COUNT = "getMessagesCount";
    private static final String ACTION_CUSTOMIZE = "customize";

    private static final String OPT_EXTERNAL_ID = "externalId";
    private static final String OPT_AFFILIATE_ID = "affiliateId";
    private static final String OPT_GOOGLE_PROJECT_ID = "googleProjectId";
    private static final String OPT_THEME = "theme";
    private static final String OPT_BORDER_COLOR = "borderColor";
    private static final String OPT_TITLE = "title";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.v(LOGTAG, "Plugin initialized");
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.v(LOGTAG, action);
        PluginResult result = null;
        try {
            if (ACTION_CREATE_USER_AND_SESSION.equals(action)) {
                result = createUserAndSession(args, callbackContext);
            } else if (ACTION_CREATE_USER.equals(action)) {
                result = createUser(args, callbackContext);
            } else if (ACTION_CREATE_SESSION.equals(action)) {
                result = createSession(args, callbackContext);
            } else if (ACTION_OPEN_INBOX.equals(action)) {
                result = openInbox(callbackContext);
            }  else if (ACTION_GET_MESSAGES_COUNT.equals(action)) {
                result = getMessagesCount(args, callbackContext);
            } else if (ACTION_CUSTOMIZE.equals(action)) {
                result = customize(args, callbackContext);
            }
        } catch (JSONException e) {
            callbackContext.error(e.getMessage());
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }

        if (result != null) callbackContext.sendPluginResult( result );

        return true;
    }

    private PluginResult createUserAndSession(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);

        String googleProjectId = obj.getString(OPT_GOOGLE_PROJECT_ID);

        final String affiliateId = obj.getString(OPT_AFFILIATE_ID);
        final String externalId = obj.getString(OPT_EXTERNAL_ID);
        final String pushDeviceRegistrationId = this.getPushDeviceRegistrationId(googleProjectId);

        if (affiliateId == "") {
            throw new Exception("Affiliate ID is required");
        }

        cordova.getActivity().runOnUiThread(runCreateUser(affiliateId, externalId, pushDeviceRegistrationId, callbackContext, true));

        return null;
    }

    private PluginResult createUser(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);

        String googleProjectId = obj.getString(OPT_GOOGLE_PROJECT_ID);

        final String affiliateId = obj.getString(OPT_AFFILIATE_ID);
        final String externalId = obj.getString(OPT_EXTERNAL_ID);
        final String pushDeviceRegistrationId = this.getPushDeviceRegistrationId(googleProjectId);

        if (affiliateId == "") {
            throw new Exception("Affiliate ID is required");
        }

        cordova.getActivity().runOnUiThread(runCreateUser(affiliateId, externalId, pushDeviceRegistrationId, callbackContext, false));

        return null;
    }

    private PluginResult createSession(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);

        String googleProjectId = obj.getString(OPT_GOOGLE_PROJECT_ID);

        final String affiliateId = obj.getString(OPT_AFFILIATE_ID);
        final String externalId = obj.getString(OPT_EXTERNAL_ID);
        final String pushDeviceRegistrationId = this.getPushDeviceRegistrationId(googleProjectId);

        if (affiliateId == "") {
            throw new Exception("Affiliate ID is required");
        }

        cordova.getActivity().runOnUiThread(runCreateSession(affiliateId, externalId, pushDeviceRegistrationId, callbackContext));

        return null;
    }

    private PluginResult openInbox(final CallbackContext callbackContext) throws Exception, JSONException {
        Log.v(LOGTAG, "Enter on OpenInbox");

        cordova.getActivity().runOnUiThread(runOpenInbox(callbackContext));

        return null;
    }

    private PluginResult getMessagesCount(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);

        cordova.getActivity().runOnUiThread(runGetMessagesCount(callbackContext));

        return null;
    }

    private PluginResult customize(JSONArray args, final CallbackContext callbackContext) throws Exception, JSONException {
        JSONObject obj = args.getJSONObject(0);

        final String theme = obj.getString(OPT_THEME);
        final String borderColor = obj.getString(OPT_BORDER_COLOR);
        final String title = obj.getString(OPT_TITLE);

        cordova.getActivity().runOnUiThread(runCustomize(theme, borderColor, title, callbackContext));

        return null;
    }

    private Runnable runCreateUser(final String affiliateId, final String externalId, final String pushDeviceRegistrationId, final CallbackContext callbackContext, final boolean executeCreateSession) {
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
                                        Log.w(LOGTAG, "createUser: onResponseListener onSuccess: " + s);
                                        if (executeCreateSession) {
                                            cordova.getActivity().runOnUiThread(runCreateSession(affiliateId, externalId, pushDeviceRegistrationId, callbackContext));
                                        } else {
                                            callbackContext.success(s);
                                        }
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        Log.w(LOGTAG, "createUser: onResponseListener onFailure " + s);
                                        callbackContext.error(s);
                                    }
                                });

                } catch (RuntimeException e) {
                    callbackContext.error(e.getMessage());
                }
            }
        };
    };

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
                                            Log.w(LOGTAG, "createSession: onResponseListener onSuccess: " + s);
                                            callbackContext.success(s);
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Log.w(LOGTAG, "createSession: onResponseListener onFailure " + s);
                                            callbackContext.error(s);
                                        }
                                    });

                } catch (RuntimeException e) {
                    callbackContext.error(e.getMessage());
                }
            }
        };
    };

    private Runnable runOpenInbox(final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    Qustodian.getInstance(context).openInbox(context);
                    callbackContext.success();
                } catch (RuntimeException e) {
                    callbackContext.error(e.getMessage());
                }
            }
        };
    };

    private Runnable runGetMessagesCount(final CallbackContext callbackContext) {
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
                                            Log.w(LOGTAG, "OnResponseMessagesCountListener onSuccess: " + messageCount);
                                            callbackContext.success(messageCount);
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Log.w(LOGTAG, "OnResponseMessagesCountListener onFailure " + s);
                                            callbackContext.error(s);
                                        }
                                    });
                } catch (RuntimeException e) {
                    callbackContext.error(e.getMessage());
                }
            }
        };
    };

    private Runnable runCustomize(final String theme, final String borderColor, final String title, final CallbackContext callbackContext) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = cordova.getActivity();
                    if (theme != "") {
                        //Qustodian.getInstance(context).set.setTheme(theme);
                    }
                    if (borderColor != "") {
                        int color = Color.parseColor(borderColor);
                        Qustodian.getInstance(context).setBorderColor(color);
                    }
                    if (title != "") {
                        Qustodian.getInstance(context).setTitle(title);
                    }
                    callbackContext.success();
                } catch (RuntimeException e) {
                    callbackContext.error(e.getMessage());
                }
            }
        };
    };

    private String getPushDeviceRegistrationId(String projectId) {
        String authorizedEntity = projectId; // Project id from Google Developer Console
        String scope = "GCM"; // e.g. communicating using GCM, but you can use any
        // URL-safe characters up to a maximum of 1000, or
        // you can also leave it blank.
        String token;

        try {
            token = InstanceID.getInstance(cordova.getActivity().getApplicationContext()).getToken(authorizedEntity, scope);
        }
        catch (IOException e) {
            token = "";
        }
        return token;
    }
}