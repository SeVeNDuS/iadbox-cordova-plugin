# iadbox Cordova Plugin
Add support for [iadbox](https://www.iadbox.com/) to your Cordova and Phonegap based mobile apps.

## How do I install it? ##

If you're like me and using [CLI](http://cordova.apache.org/):
```
cordova plugin add iadbox-cordova-plugin
```

or

```
cordova plugin add https://github.com/SeVeNDuS/iadbox-cordova-plugin.git
```

or

```
phonegap local plugin add https://github.com/SeVeNDuS/iadbox-cordova-plugin.git
```

## How do I use it? ##

```javascript
document.addEventListener('deviceready', function() {
	var color = '#669900';
	var text = 'Messages';
	var affiliateId = 'affid';
	var userId = 'someuserid';
	var googleProjectId = 'someprojectid';
	window.plugins.iadbox.customize('', color, text);
	window.plugins.iadbox.createUserAndSession(affiliateId, userId, googleProjectId, 	function (s) { console.log('Success') }, 
		function (s) { console.log('Ouch!!!')}
	);
                    
}, false);

// To open the inbox
window.plugins.iadbox.openInbox();

```