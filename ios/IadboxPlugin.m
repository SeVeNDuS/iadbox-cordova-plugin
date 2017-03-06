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
    NSLog(@"***** createUser called");
    
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
    NSLog(@"***** createSession called");
    
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
    NSLog(@"***** getBadge called");
    if (getMessageCountCallbackId == nil)
        getMessageCountCallbackId = command.callbackId;
    
    [[Qustodian sharedInstance] getMessagesCount:self];
}

-(void)messageCountReceived:(int)_messages_count withError:(NSError *)error
{
    NSLog(@"***** messageCountReceived called");
    
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
        NSLog(@"***** dealsReceived: %d", _has_deals);
        int messages_count = _has_deals ? 1 : 0;
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:messages_count];
        
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:getMessageCountCallbackId];
    createSessionCallbackId = nil;
}


- (void)getUrl:(CDVInvokedUrlCommand*)command
{
    NSLog(@"***** getUrl called");
    CDVPluginResult* pluginResult = nil;
    NSDictionary* params = [command.arguments objectAtIndex:0];
    
    if (params != nil && [params count] > 0) {
        NSString *url = [[Qustodian sharedInstance] 
            getUrl:params[@"action"] 
            withDelegate:self];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:url];
        NSLog(@"***** getUrl: %@", url);
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


- (void)customize:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSDictionary* params = [command.arguments objectAtIndex:0];
    
    if (params != nil && [params count] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@""];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


@end
