var exec = require('cordova/exec');

var iadboxPlugin = {

    USER_ID_KEY: 'iadbox_user_id',
	authenticateSuccessCallback: null,
	authenticatedSuccess: false,

	authenticateUser: function(affiliateId, externalId, pushId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"pushId": pushId
		});

        this.authenticateSuccessCallback = successCallback;

        user_id = window.localStorage.getItem(this.USER_ID_KEY);
        if (user_id == null){
        	cordova.exec(this.saveUserIdAndCallback.bind(this), failureCallback, 'iadbox', 'createUser', options);
        } else {
			cordova.exec(this.saveUserIdAndCallback.bind(this), failureCallback, 'iadbox', 'createSession', options);
		}
		setTimeout(this.retryAuthenticationUnlessSuccess.bind(this), 3000);
	},

	saveUserIdAndCallback: function(user_id) {
		window.localStorage.setItem(this.USER_ID_KEY, user_id);
		this.authenticatedSuccess = true;
		this.authenticateSuccessCallback.call(user_id);
	},

	removeUserId: function() {
		window.localStorage.removeItem(this.USER_ID_KEY);
	},

	retryAuthenticationUnlessSuccess: function() {
		if(!this.authenticatedSuccess){
			this.removeUserId();
			this.authenticateUser.bind(this);
		}
	},

	createUser: function(affiliateId, externalId, pushId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"pushId": pushId
		});
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'createUser', options);
	},

	createSession: function(affiliateId, externalId, pushId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"pushId": pushId
		});
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'createSession', options);
	},

	openInbox: function(successCallback, failureCallback) {
		var options = [];
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'openInbox', options);
	},

	reload: function(successCallback, failureCallback) {
		var options = [];
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'reload', options);
	},

	disableTopBarBackButton: function(successCallback, failureCallback) {
		var options = [];
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'disableTopBarBackButton', options);
	},

	enableExitAppOnBack: function(successCallback, failureCallback) {
		var options = [];
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'enableExitAppOnBack', options);
	},

	customize: function(borderColor, title, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"borderColor": borderColor,
			"title": title
		});

		cordova.exec(successCallback, failureCallback, 'iadbox', 'customize', options);
	},

	getBadge: function(successCallback, failureCallback) {
		var options = [];
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'getBadge', options);
	},

	getUrl: function(action, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"action": action
		});

		cordova.exec(successCallback, failureCallback, 'iadbox', 'getUrl', options);
	}
};

module.exports = iadboxPlugin;