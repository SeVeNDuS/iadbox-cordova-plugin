//
//  QustodianWebViewController.h
//  QustodianSDK
//

#import <UIKit/UIKit.h>
#import "Qustodian.h"

#pragma mark different options for showing

#define QUSTODIAN_INBOX                     0
#define QUSTODIAN_DEALS                     1
#define QUSTODIAN_APPS                      2
#define QUSTODIAN_GAMES                     3
#define QUSTODIAN_PROFILE                   4
#define QUSTODIAN_NOTIFICATION_SETTINGS     5
#define QUSTODIAN_MY_PREFERENCES            6
#define QUSTODIAN_NONE                      7

#define QUSTODIAN_INTERSTITIAL              (1<<4)
#define QUSTODIAN_ACTIVITY                  (1<<8)

#pragma mark -

@protocol QustodianWebViewControllerDelegate <NSObject>

- (void)sharedText:(NSString *)title url:(NSString *)url image:(NSString *)image;
- (BOOL)hasNetworkConnectivity;
- (void)openUrlWithExternBrowser:(NSString*)url;
- (void)onJavascriptToast:(NSString*)message useCustomLayout:(BOOL)useCustomLayout;
- (void)openVideoUrlWithExternPlayer:(NSString*)url withUnusedStartPositionMS:(long)unusedStartPositionMs;
- (void)onJavascriptError:(NSString*)message fromLink:(NSString*)urlLink atLine:(int)lineNumber;
- (void)webViewClosed;

@end

@interface QustodianWebViewController : UIViewController<UIWebViewDelegate>
{
    
}

// init webview controller
- (id)initWithType:(int)_type;
- (id)initWithFrame:(CGRect)_frame andType:(int)_type;

#pragma mark ONLY for default integration
@property (nonatomic, strong) NSString *url;
@property (nonatomic, strong) UIColor *color;
@property (nonatomic, strong) UIColor *textColor;
@property (nonatomic, strong) NSString *titulo;
@property (nonatomic, assign) BOOL isBackButtonEnabled;
@property (nonatomic, weak) id<QustodianWebViewControllerDelegate> delegate;
#pragma mark -

#pragma mark opening methods, default is Inbox if none of this methods is called

// Shows the inbox

-(void)openInbox:(id<QustodianDelegate>)delegate;

// Shows deals

-(void)openDeals:(id<QustodianDelegate>)delegate;

// Shows apps

-(void)openApps:(id<QustodianDelegate>)delegate;

// Shows games

-(void)openGames:(id<QustodianDelegate>)delegate;

// Shows profile

-(void)openProfile:(id<QustodianDelegate>)delegate;

// Shows notification settings

-(void)openNotificationSettings:(id<QustodianDelegate>)delegate;

// Shows preferences settings

-(void)openMyPreferences:(id<QustodianDelegate>)delegate;

#pragma mark -


@end
