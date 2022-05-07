
#import <Cordova/CDV.h>

@interface VizbeeNativeManager : CDVPlugin

-(void) initialize:(CDVInvokedUrlCommand*)command;

-(void) addCastIcon:(CDVInvokedUrlCommand *)command;

-(void) removeCastIcon:(CDVInvokedUrlCommand *)command;

-(void) smartPrompt:(CDVInvokedUrlCommand *)command;

-(void) smartCast:(CDVInvokedUrlCommand *)command;

-(void) smartPlay:(CDVInvokedUrlCommand *)command;

//----------------
// Session APIs
//----------------

//----------------
// Video APIs
//----------------

-(void) play:(CDVInvokedUrlCommand *)command;

-(void) pause:(CDVInvokedUrlCommand *)command;

-(void) seek:(CDVInvokedUrlCommand *)command;

-(void) stop:(CDVInvokedUrlCommand *)command;

@end
  
