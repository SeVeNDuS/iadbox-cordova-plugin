package uk.mondosports.plugins.iadbox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.qustodian.sdk.*;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class iAdBoxPlugin extends CordovaPlugin {

    private static final String LOGTAG = "iAdBoxPlugin";
    private static final String DEFAULT_AFFILIATE_ID = "futmondo";

    private static final String ACTION_CREATE_USER = "createUser";
    private static final String ACTION_CREATE_SESSION = "createSession";
    private static final String ACTION_SET_SECTIONS = "setSections";
    private static final String ACTION_OPEN_INBOX = "openInbox";
    private static final String ACTION_OPEN_DEALS = "openDeals";
    private static final String ACTION_OPEN_PROFILE = "openProfile";
    private static final String ACTION_OPEN_NOTIFICATIONS_SETTINGS = "openNotificationsSettings";
    private static final String ACTION_OPEN_MY_PREFERENCES = "openMyPreferences";
    private static final String ACTION_OPEN_ACTION = "openAction";
    private static final String ACTION_GET_SUMMARY_MESSAGES = "getSummaryOfMessages";
    private static final String ACTION_GET_PRODUCTS = "getProducts";
    private static final String ACTION_GET_CATEGORIES = "getCategories";
    private static final String ACTION_SET_CUSTOM_TOAST_COLORS = "setCustomToastColors";
    private static final String ACTION_GET_MESSAGES_COUNT = "getMessagesCount";
    private static final String ACTION_GET_DEALS_COUNT = "getDealsCount";
    private static final String ACTION_SET_PUSH_MESSAGE = "setPushMessage";
    private static final String ACTION_SET_BORDER_COLOR = "setBorderColor";
    private static final String ACTION_SET_TITLE = "setTitle";
    private static final String ACTION_SET_THEME = "setTheme";
    private static final String ACTION_SET_MAX_SHOWS_DAY = "setMaxShowsPerDay";
    private static final String ACTION_SET_APP_OPENING_SHOW = "setAppOpeningsPerShow";
    private static final String ACTION_REPLACE_BUTTON = "replaceButton";
    private static final String ACTION_SET_OPEN_SHARE_ACTION_LISTENER = "setOpenShareActionListener";
    private static final String ACTION_REPLACE_CUSTOM_NETWORK_ERROR_STRING = "replaceCustomNetworkErrorStringResourceId";

    private static final String OPT_EXTERNAL_ID = "externalId";
    private static final String OPT_AFFILIATE_ID = "affiliateId";
    private static final String OPT_PUSH_DEVICE_REGISTRADTION_ID = "pushDeviceRegistrationId";
    private static final String OPT_GOOGLE_PROJECT_ID = "googleProjectId";

    private String affiliateId = DEFAULT_AFFILIATE_ID;
    private String externalId = "";
    private String googleProjectId = "";
    private String pushDeviceRegistrationId = "";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        PluginResult result = null;

        if (ACTION_CREATE_USER.equals(action)) {
            JSONObject options = args.optJSONObject(0);
            result = executeCreateUser(options, callbackContext);
        } else if (ACTION_CREATE_SESSION.equals(action)) {
            JSONObject options = args.optJSONObject(0);
            result = executeCreateSession(options, callbackContext);
        }  else if (ACTION_SET_SECTIONS.equals(action)) {
            JSONObject options = args.optJSONObject(0);
            result = executeSetSections(options, callbackContext);
        } else if (ACTION_OPEN_INBOX.equals(action)) {
            JSONObject options = args.optJSONObject(0);
            result = executeOpenInbox(options, callbackContext);
        }

        if (result != null) callbackContext.sendPluginResult( result );

        return true;
    }

    private PluginResult executeCreateUser(JSONObject options, CallbackContext callbackContext) {
        Log.w(LOGTAG, "executeCreateUser");

        this.createUser(options, callbackContext);

        return null;
    }

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

    private void createUser(JSONObject options, final CallbackContext callbackContext) {
        if (options.has(OPT_AFFILIATE_ID)) {
            this.affiliateId = options.optString(OPT_AFFILIATE_ID);
        }
        if (options.has(OPT_EXTERNAL_ID)) {
            this.externalId = options.optString(OPT_EXTERNAL_ID);
        }
        if (options.has(OPT_GOOGLE_PROJECT_ID)) {
            this.googleProjectId = options.optString(OPT_GOOGLE_PROJECT_ID);
        }
        if (options.has(OPT_PUSH_DEVICE_REGISTRADTION_ID)) {
            this.pushDeviceRegistrationId = options.optString(OPT_PUSH_DEVICE_REGISTRADTION_ID);
        } else {
            this.pushDeviceRegistrationId = this.getPushDeviceRegistrationId(this.googleProjectId);
        }

        try {
            Context context = cordova.getActivity();
            Qustodian.getInstance(context)
                    .createUser(context,
                            this.externalId,
                            this.affiliateId,
                            this.pushDeviceRegistrationId,
                            new OnResponseListener() {
                                @Override
                                public void onSuccess(String s) {
                                    Log.w(LOGTAG, "onResponseListener onSuccess: " + s);
                                    callbackContext.success(s);
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.w(LOGTAG, "onResponseListener onFailure " + s);
                                    callbackContext.error(s);
                                }
                            });
        } catch (RuntimeException e){
            Log.d(LOGTAG, e.getLocalizedMessage());
        }
    }

    private PluginResult executeCreateSession(JSONObject options, CallbackContext callbackContext) {
        Log.w(LOGTAG, "executeCreateSession");

        this.createSession(options, callbackContext);

        return null;
    }

    private void createSession(JSONObject options, final CallbackContext callbackContext) {
        if (options.has(OPT_AFFILIATE_ID)) {
            this.affiliateId = options.optString(OPT_AFFILIATE_ID);
        }
        if (options.has(OPT_EXTERNAL_ID)) {
            this.externalId = options.optString(OPT_EXTERNAL_ID);
        }
        if (options.has(OPT_GOOGLE_PROJECT_ID)) {
            this.googleProjectId = options.optString(OPT_GOOGLE_PROJECT_ID);
        }
        if (options.has(OPT_PUSH_DEVICE_REGISTRADTION_ID)) {
            this.pushDeviceRegistrationId = options.optString(OPT_PUSH_DEVICE_REGISTRADTION_ID);
        } else {
            this.pushDeviceRegistrationId = this.getPushDeviceRegistrationId(this.googleProjectId);
        }

        try {
            Context context = cordova.getActivity();
            Qustodian.getInstance(context)
                    .createSession(context,
                            this.externalId,
                            this.affiliateId,
                            this.pushDeviceRegistrationId,
                            new OnResponseListener() {
                                @Override
                                public void onSuccess(String s) {
                                    Log.w(LOGTAG, "onResponseListener onSuccess: " + s);
                                    callbackContext.success(s);
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Log.w(LOGTAG, "onResponseListener onFailure " + s);
                                    callbackContext.error(s);
                                }
                            });
        } catch (RuntimeException e){
            Log.d(LOGTAG, e.getLocalizedMessage());
        }
    }

    private PluginResult executeSetSections(JSONObject options, CallbackContext callbackContext) {
        Log.w(LOGTAG, "executeCreateSession");

        this.setSections(options);

        callbackContext.success();

        return null;
    }

    private void setSections( JSONObject options ) {
        boolean showInbox = true;
        boolean showDeals = true;
        boolean showInvite = true;
        boolean showProfile = true;

        try {
            Context context = cordova.getActivity();
            Qustodian.getInstance(context)
                    .setSections(showInbox, showDeals, showInvite, showProfile);
        } catch (RuntimeException e){
            Log.d(LOGTAG, e.getLocalizedMessage());
        }
    }

    private PluginResult executeOpenInbox(JSONObject options, CallbackContext callbackContext) {
        Log.w(LOGTAG, "executeOpenInbox");

        try {
            Context context = cordova.getActivity();
            Qustodian.getInstance(context).openInbox(context);

            callbackContext.success();
        } catch (RuntimeException e){
            Log.d(LOGTAG, e.getLocalizedMessage());
        }

        return null;
    }
}