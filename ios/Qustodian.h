//
//  Qustodian.h
//
//  Dependencies: SystemConfiguration.framework, CoreLocation.framework, QuartzCore.framework, CoreTelephony.framework

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

#define QUSTODIAN_DEPRECATED_ATTRIBUTE        __attribute__((deprecated))

#pragma mark - Qustodian SDK delegate

@protocol QustodianDelegate<NSObject>

@optional -(void)userCreated:(int)_user_id withAuthorization:(NSString *)_authorization response:(NSString*)_response andError:(NSError *)error;
@optional -(void)sessionCreated:(int)_user_id withAuthorization:(NSString *)_authorization response:(NSString*)_response andError:(NSError *)error;
@optional -(void)summaryReceived:(id)_response withError:(NSError *)error;
@optional -(void)categoriesReceived:(id)_response withError:(NSError *)error;
@optional -(void)productsReceived:(id)_response withError:(NSError *)error;
@optional -(void)dealsReceived:(BOOL)_deals withError:(NSError *)error;
@optional -(void)messageCountReceived:(int)_messages_count withError:(NSError *)error;
@optional -(void)sharedText:(NSString *)title url:(NSString *)url image:(NSString *)image;
@optional -(void)notAuthorized:(NSError *)error;
@optional -(void)invalidPushMessage:(NSError *)error;
@end


#pragma mark - Qustodian SDK singleton

@interface Qustodian : NSObject

+ (Qustodian *) sharedInstance;

#pragma mark geolocation

// Activate geolocation

-(void)activateGeolocation:(BOOL)activate;

#pragma mark -
#pragma mark configuration

// Specifies which ViewController will receive and manage the 'openShareAction' call (optional)
// If not set, it will be managed by Qustodian.

- (void) setOpenShareActionDelegate:(id)delegate;

// Specifies the Network error message (optional)
// If not set, it will use the 'QSDK_STRING_NO_INTERNET_CONNECTION_MESSAGE' string.

- (void) replaceCustomNetworkErrorStringResourceId:(NSString*)newMessage;

// Specifies if the network erros will be shown in alert view
// By default YES
- (void) showNetworkErrorInAlertView:(BOOL)showAlertView;


#pragma mark -
#pragma mark User Login/Creation

// User creation
// Params:
//          externalId
//          affiliateId
//          pushDeviceRegistrationId
//          delegate

-(void)createUser:(NSString *)externalId
      affiliateId:(NSString *)affiliateId
pushDeviceRegistrationId:(NSString *)pushDeviceRegistrationId
         delegate:(id<QustodianDelegate>)delegate;

// User login
// Params:
//          externalId
//          affiliateId
//          pushDeviceRegistrationId
//          delegate

-(void)createSession:(NSString *)externalId
         affiliateId:(NSString *)affiliateId
pushDeviceRegistrationId:(NSString *)pushDeviceRegistrationId
            delegate:(id<QustodianDelegate>)delegate;

#pragma mark -
#pragma mark Other Services

// Summary
// Params:
//
- (void)getSummaryOfMessages:(id <QustodianDelegate>)delegate;

// Categories
// Params:
//
- (void)getDealCategories:(id <QustodianDelegate>)delegate;
- (void)getAppCategories:(id <QustodianDelegate>)delegate;
- (void)getGameCategories:(id <QustodianDelegate>)delegate;

// Products
// Params:
//
- (void)getDealProducts:(id <QustodianDelegate>)delegate;
- (void)getAppProducts:(id <QustodianDelegate>)delegate;
- (void)getGameProducts:(id <QustodianDelegate>)delegate;

#pragma mark -
#pragma mark periods

// Set number of times in the same day to show icon

-(void)setMaxShowsPerDay:(int)_max_show_per_day;

//-(void)setMaxDiaryBadges:(int)_max_times;

// Set number of executions of the application to show the icon once

// -(void)setSessionsIntervalForBadges:(int)_session_interval;

-(void)setAppOpeningsPerShow:(int)_app_openings_per_show;

#pragma mark -
#pragma mark show options

// Set default button image (104x104)

-(void)replaceButton:(UIImage *)_image;

// Set default color

-(void)setBorderColor:(UIColor *)color;

// Set default text color

-(void)setTextColor:(UIColor *)color;

// Set title

-(void)setTitle:(NSString *)title;

// Opens the Qustodian dialog with Inbox section selected.

-(void)openInbox:(id<QustodianDelegate>)delegate;

// Opens the Qustodian dialog with Deals section selected.

-(void)openDeals:(id<QustodianDelegate>)delegate;

// Opens the Qustodian dialog with Apps section selected.

-(void)openApps:(id<QustodianDelegate>)delegate;

// Opens the Qustodian dialog with Games section selected.

-(void)openGames:(id<QustodianDelegate>)delegate;

// Opens the Qustodian dialog with Profile section selected.

-(void)openProfile:(id<QustodianDelegate>)delegate;

// Opens the Qustodian dialog with Notification Settings section selected.

-(void)openNotificationSettings:(id<QustodianDelegate>)delegate;

// Opens the Qustodian dialog with My Preferences section selected.

-(void)openMyPreferences:(id<QustodianDelegate>)delegate;

// Opens the specified action or message

- (void)openAction:(NSString *)action withDelegate:(id <QustodianDelegate>)delegate;

// Opens browser window

- (void)openBrowser:(NSString *)url;

#pragma mark -
#pragma mark Other methods

// Gets the message count
-(void)getMessagesCount:(id<QustodianDelegate>)delegate;

// Get deals count
-(void)getDealsCount:(id<QustodianDelegate>)delegate;

// Gets the url for the specified section
-(NSString*)getUrl:(NSString *)section withDelegate:(id<QustodianDelegate>)delegate;

// Disables the Back Button in the Top Bar
-(void)disableTopBarBackButton;

// Get SDK version
-(NSString*)getSDKVersion;

// Get SDK api Host
-(NSString*)getSDKApiHost;

// Get SDK resource URL
-(NSString*)getSDKResourceURL;

#pragma -
#pragma mark attach/detach overlay view

- (void) attachToWindow:(UIWindow *)_window withDelegate:(id<QustodianDelegate>)delegate;
- (void) attach:(UIView *)view withDelegate:(id<QustodianDelegate>)delegate;
- (void) attachWithDelegate:(id<QustodianDelegate>)delegate;
- (void) detach;

#pragma mark -

@end


#pragma mark - Qustodian SDK for remote notifications

@interface Qustodian (UIRemoteNotifications)

- (void)application:(UIApplication *)application setPushMessage:(NSData *)message withDelegate:(id <QustodianDelegate>)delegate;

- (void)setPushMessage:(NSData *)message withDelegate:(id <QustodianDelegate>)delegate QUSTODIAN_DEPRECATED_ATTRIBUTE;

- (void)registerForRemoteNotifications:(UIApplication *)application;

- (void)unregisterForRemoteNotifications:(UIApplication *)application;

@end
