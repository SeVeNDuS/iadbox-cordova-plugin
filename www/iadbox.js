var exec = require('cordova/exec');

var iAdBoxPlugin = {
	createUserAndSession: function(affiliateId, externalId, googleProjectId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"googleProjectId": googleProjectId
		});
		
		cordova.exec(successCallback, failureCallback, 'iAdBox', 'createUserAndSession', options);
	},

	createUser: function(affiliateId, externalId, googleProjectId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"googleProjectId": googleProjectId
		});
		
		cordova.exec(successCallback, failureCallback, 'iAdBox', 'createUser', options);
	},

	createSession: function(affiliateId, externalId, googleProjectId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"googleProjectId": googleProjectId
		});
		
		cordova.exec(successCallback, failureCallback, 'iAdBox', 'createSession', options);
	},

	openInbox: function(successCallback, failureCallback) {
		var options = [];
		
		cordova.exec(successCallback, failureCallback, 'iAdBox', 'openInbox', options);
	},

	customize: function(theme, borderColor, title, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"theme": theme,
			"borderColor": borderColor,
			"title": title
		});

		cordova.exec(successCallback, failureCallback, 'iAdBox', 'customize', options);
	}
};

module.exports = iAdBoxPlugin;