function iAdBoxPlugin() {}

iAdBoxPlugin.prototype.createUser = function (options, success, error) {
	cordova.exec(success, error, 'iAdBox', 'createUser', [options]);
};

iAdBoxPlugin.prototype.createSession = function (options, success, error) {
	cordova.exec(success, error, 'iAdBox', 'createSession', [options]);
};

iAdBoxPlugin.prototype.setSections = function (success, error) {
	cordova.exec(success, error, 'iAdBox', 'setSections', []);
};

iAdBoxPlugin.prototype.openInbox = function (success, error) {
	cordova.exec(success, error, 'iAdBox', 'openInbox', []);
};

iAdBoxPlugin.prototype.openDeals = function (success, error) {
	cordova.exec(success, error, 'iAdBox', 'openDeals', []);
};

iAdBoxPlugin.prototype.openProfile = function (success, error) {
	cordova.exec(success, error, 'iAdBox', 'openProfile', []);
};

iAdBoxPlugin.prototype.openNotificationsSettings = function (success, error) {
	cordova.exec(success, error, 'iAdBox', 'openNotificationsSettings', []);
};

iAdBoxPlugin.prototype.openMyPreferences = function (success, error) {
	cordova.exec(success, error, 'iAdBox', 'openMyPreferences', []);
};

iAdBoxPlugin.prototype.openAction = function (success, error) {
	cordova.exec(success, error, 'iAdBox', 'openAction', []);
};


module.exports = new iAdBoxPlugin();