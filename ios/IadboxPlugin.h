#import <Cordova/CDVPlugin.h>
#import "Qustodian.h"

@interface IadboxPlugin : CDVPlugin<QustodianDelegate>

- (void)createUser:(CDVInvokedUrlCommand*)command;
- (void)createSession:(CDVInvokedUrlCommand*)command;
- (void)openInbox:(CDVInvokedUrlCommand*)command;
- (void)getBadge:(CDVInvokedUrlCommand*)command;
- (void)getUrl:(CDVInvokedUrlCommand*)command;
- (void)customize:(CDVInvokedUrlCommand*)command;
- (void)disableTopBarBackButton:(CDVInvokedUrlCommand*)command;
- (void)enableExitAppOnBack:(CDVInvokedUrlCommand*)command;

@end