//
//  VizbeeBootstrap.m
//  RNVizbeeSenderSdk
//

#import "VizbeeBootstrap.h"
#import "VizbeeAppAdapter.h"

@implementation VizbeeBootstrap

-(void) initialize:(CDVInvokedUrlCommand*) command {
    
    // get appid
    NSString* vizbeeAppId = command.arguments[0];
    
    // initialize the sdk
    VZBOptions *vizbeeOptions = [VZBOptions new];
    VizbeeAppAdapter* vizbeeAppAdapter = [[VizbeeAppAdapter alloc] init];
    [Vizbee startWithAppID:vizbeeAppId appAdapterDelegate:vizbeeAppAdapter andVizbeeOptions:vizbeeOptions];
}

-(void) addCastIcon:(CDVInvokedUrlCommand*) command {
    
    // create cast button
    VZBCastButton* castButton = [Vizbee createCastButton];
    [self.viewController.view addSubview:castButton];

    // add autolayout constraints
    castButton.translatesAutoresizingMaskIntoConstraints = false;
    NSLayoutConstraint* constraintTop = [NSLayoutConstraint constraintWithItem:castButton attribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:self.viewController.view attribute:NSLayoutAttributeTop multiplier:1.0 constant:48.0];
    NSLayoutConstraint* constraintTrailing = [NSLayoutConstraint constraintWithItem:castButton attribute:NSLayoutAttributeTrailing relatedBy:NSLayoutRelationEqual toItem:self.viewController.view attribute:NSLayoutAttributeTrailing multiplier:1.0 constant:-24.0];
    NSLayoutConstraint* constraintWidth = [NSLayoutConstraint constraintWithItem:castButton attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:nil attribute:NSLayoutAttributeNotAnAttribute multiplier:1.0 constant:24.0];
    NSLayoutConstraint* constraintHeight = [NSLayoutConstraint constraintWithItem:castButton attribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:nil attribute:NSLayoutAttributeNotAnAttribute multiplier:1.0 constant:24.0];
    [NSLayoutConstraint activateConstraints:@[constraintTop, constraintTrailing, constraintWidth, constraintHeight]];
}

-(void) smartPrompt:(CDVInvokedUrlCommand*) command {

}

-(void) smartPlay:(CDVInvokedUrlCommand*) command {

}

@end
