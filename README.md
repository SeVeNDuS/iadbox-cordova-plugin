# iadbox Cordova Plugin
Add support for [iadbox](https://www.iadbox.com/) to your Cordova and Phonegap based mobile apps.

We currently support Android and iOS platforms.

## How do I install it? ##

If you're like me and using [CLI](http://cordova.apache.org/):
```
cordova plugin add iadbox-cordova-plugin
```

or

```
cordova plugin add https://github.com/iadbox/iadbox-cordova-plugin.git
```

or

```
phonegap local plugin add https://github.com/iadbox/iadbox-cordova-plugin.git
```

If you want to see a full example of a simple app using this plugin you can check: [iadbox-cordova-app](https://github.com/iadbox/iadbox-cordova-app).

## How do I use it? ##

Here is a demonstration of the main functions.

```javascript
document.addEventListener('deviceready', function() {
	var topbar_color = '#669900';
	var topbar_text = 'Messages';
	var affiliateId = 'affid'; //your affiliate id
	var userId = 'someuserid'; //your app user id
	var pushId = 'obtain push registration ID'; //set it to blank to disable notifications
	window.plugins.iadbox.customize(topbar_color, topbar_text);
	window.plugins.iadbox.authenticateUser(affiliateId, userId, pushId,	
		function() { console.log('Success') }, 
		function(message) { console.log('Ouch!!!' + message)}
	);
                    
}, false);

// To open the inbox
window.plugins.iadbox.openInbox();

// To get the number of unread messages
window.plugins.iadbox.getBadge(
	function(count){ console.log(count + 'unread messages'); }, 
	function(message) { console.log('Ouch!!!' + message); });
```

## How to integrate push notifications? ##

The easiest way to integrate our notifications feature is to use [phonegap-plugin-push](https://github.com/phonegap/phonegap-plugin-push) plugin. Follow its instructions to set it up.

Here you have an example on how to use it:

```javascript
var pushId = "";
var pushNotificationPlugin = PushNotification.init({
    android: {
        senderID: "YOUR SENDER ID"
    },
    ios: {
        alert: "true",
        badge: "true",
        sound: "true"
    }
});

pushNotificationPlugin.on('error', function(e) {
    console.error(e.message);
    authenticateUser();
});

pushNotificationPlugin.on('registration', function(data) {
    pushId = data.registrationId;
    console.log("Regitration pushId: " + pushId);
    authenticateUser();
});

pushNotificationPlugin.on('notification', function(data) {
    // data.message,
    // data.title,
    // data.count,
    // data.sound,
    // data.image,
    // data.additionalData
});

```

Our notification payload has the following format:
```json
{
	"message":"You have 1 new message.",
	"additionalData":{"action":"inbox"}
}
```

The previous notification payload purpose is to open our Inbox, you should do something like:

```javascript
pushNotificationPlugin.on('notification', function(data) {
    if(data.additionalData.action === "inbox"){
    	window.plugins.iadbox.openInbox();
	}
});
```

## How to show unread messages in your App's badge? ##

Here you have an example using [cordova-plugin-badge](https://github.com/katzer/cordova-plugin-badge):

```javascript
window.plugins.iadbox.getBadge(
	function(count) {
        console.log("updateMessageCountBadge: " + count);
        cordova.plugins.notification.badge.set(count);
    }, 
    function(message) { console.log('Ouch!!!' + message); });
```