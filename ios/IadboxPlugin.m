#import "IadboxPlugin.h"
#import "Qustodian.h"
#import <Cordova/CDVPlugin.h>

@interface IadboxPlugin(){
    NSString *createUserCallbackId;
    NSString *createSessionCallbackId;
    NSString *getMessageCountCallbackId;
}

@end
@implementation IadboxPlugin

- (void)createUser:(CDVInvokedUrlCommand*)command
{
    NSDictionary *params = [command.arguments objectAtIndex:0];
    
    if (params != nil && [params count] == 3) {
        if (createUserCallbackId == nil)
            createUserCallbackId = command.callbackId;
        [[Qustodian sharedInstance] createUser:params[@"externalId"] 
            affiliateId:params[@"affiliateId"] 
            pushDeviceRegistrationId:params[@"pushId"] 
            delegate:self];
        
    } else {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}

-(void)userCreated:(int)_user_id withAuthorization:(NSString *)_authorization response:(NSString *)_response andError:(NSError *)error
{
    CDVPluginResult *pluginResult = nil;
    if(error)
    {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    else
    {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:_user_id];
        
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:createUserCallbackId];
    createUserCallbackId = nil;
}


- (void)createSession:(CDVInvokedUrlCommand*)command
{
    NSDictionary *params = [command.arguments objectAtIndex:0];
    
    if (params != nil && [params count] == 3) {
        if (createSessionCallbackId == nil)
            createSessionCallbackId = command.callbackId;
        
        [[Qustodian sharedInstance] createSession:params[@"externalId"] 
            affiliateId:params[@"affiliateId"] 
            pushDeviceRegistrationId:params[@"pushId"] 
            delegate:self];
        
    } else {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}

-(void)sessionCreated:(int)_user_id withAuthorization:(NSString *)_authorization response:(NSString *)_response andError:(NSError *)error
{
    CDVPluginResult *pluginResult = nil;
    if(error)
    {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    else
    {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:_user_id];
        
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:createSessionCallbackId];
    createSessionCallbackId = nil;
}


- (void)openInbox:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    [[Qustodian sharedInstance] openInbox:self];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


- (void)getBadge:(CDVInvokedUrlCommand*)command
{
    if (getMessageCountCallbackId == nil)
        getMessageCountCallbackId = command.callbackId;
    
    [[Qustodian sharedInstance] getMessagesCount:self];
}

-(void)messageCountReceived:(int)_messages_count withError:(NSError *)error
{
    
    CDVPluginResult *pluginResult = nil;
    if(error)
    {
        NSLog(@"***** ERROR messageCountReceived: %@", error);
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    else
    {
        NSLog(@"***** messageCountReceived: %d", _messages_count);
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:_messages_count];
        
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:getMessageCountCallbackId];
    createSessionCallbackId = nil;
}

-(void)dealsReceived:(bool)_has_deals withError:(NSError *)error
{
    NSLog(@"***** dealsReceived called");
    
    CDVPluginResult *pluginResult = nil;
    if(error)
    {
        NSLog(@"***** ERROR dealsReceived: %@", error);
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    else
    {
        int messages_count = _has_deals ? 1 : 0;
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:messages_count];
        
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:getMessageCountCallbackId];
    createSessionCallbackId = nil;
}


- (void)getUrl:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSDictionary* params = [command.arguments objectAtIndex:0];
    
    if (params != nil && [params count] > 0) {
        NSString *url = [[Qustodian sharedInstance] 
            getUrl:params[@"action"] 
            withDelegate:self];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:url];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


- (void)disableTopBarBackButton:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    [[Qustodian sharedInstance] disableTopBarBackButton];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

//does nothing, this function is only for plugin compatibility
- (void)enableExitAppOnBack:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


- (void)customize:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSDictionary* params = [command.arguments objectAtIndex:0];
    
    if (params != nil && [params count] > 0) {
        NSString *title = params[@"title"];
        if (title != nil){
            [[Qustodian sharedInstance] setTitle: title];
        }
        NSString *borderColor = params[@"borderColor"];
        if (borderColor != nil){
            [[Qustodian sharedInstance] setBorderColor: [self colorFromHexString: borderColor]];
        }        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

// Assumes input like "#00FF00" (#RRGGBB).
- (UIColor *)colorFromHexString:(NSString *)hexString {
    unsigned rgbValue = 0;
    NSScanner *scanner = [NSScanner scannerWithString:hexString];
    [scanner setScanLocation:1]; // bypass '#' character
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0 green:((rgbValue & 0xFF00) >> 8)/255.0 blue:(rgbValue & 0xFF)/255.0 alpha:1.0];
}


@end
