var exec = require('cordova/exec');

var iadboxPlugin = {
	createUserAndSession: function(affiliateId, externalId, googleProjectId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"googleProjectId": googleProjectId
		});
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'createUserAndSession', options);
	},

	createUser: function(affiliateId, externalId, googleProjectId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"googleProjectId": googleProjectId
		});
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'createUser', options);
	},

	createSession: function(affiliateId, externalId, googleProjectId, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"affiliateId": affiliateId,
			"externalId": externalId,
			"googleProjectId": googleProjectId
		});
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'createSession', options);
	},

	openInbox: function(successCallback, failureCallback) {
		var options = [];
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'openInbox', options);
	},

	customize: function(theme, borderColor, title, successCallback, failureCallback) {
		var options = [];
		
		options.push({
			"theme": theme,
			"borderColor": borderColor,
			"title": title
		});

		cordova.exec(successCallback, failureCallback, 'iadbox', 'customize', options);
	},

	getMessagesCount: function(successCallback, failureCallback) {
		var options = [];
		
		cordova.exec(successCallback, failureCallback, 'iadbox', 'getMessagesCount', options);
	}
};

module.exports = iadboxPlugin;