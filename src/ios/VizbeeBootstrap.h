//
//  VizbeeBootstrap.h
//  RNVizbeeSenderSdk
//

#import <VizbeeKit/VizbeeKit.h>
#import <Foundation/Foundation.h>

#import <Cordova/CDV.h>

@interface VizbeeBootstrap : CDVPlugin

-(void) initialize:(CDVInvokedUrlCommand*)command;

-(void) addCastIcon:(CDVInvokedUrlCommand *)command;

-(void) smartPrompt:(CDVInvokedUrlCommand *)command;

-(void) smartPlay:(CDVInvokedUrlCommand *)command;

@end
