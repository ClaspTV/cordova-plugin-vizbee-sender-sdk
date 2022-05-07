#import "VizbeeNativeManager.h"
#import "VizbeeAppAdapter.h"
#import "VizbeeVideo.h"

#import <VizbeeKit/VizbeeKit.h>

@interface VizbeeNativeManager() <VZBSessionStateDelegate, VZBVideoStatusUpdateDelegate>

@property (nonatomic, assign) BOOL hasListeners;
@property (nonatomic, assign) VZBSessionState lastUpdatedState;

@end

@implementation VizbeeNativeManager

//-------------
#pragma mark - Constructors & Notifications
//-------------

-(void) initialize:(CDVInvokedUrlCommand*) command {

    NSLog(@"Initialize VizbeeSDK");
    
    // get appid
    NSString* vizbeeAppId = command.arguments[0];

    // initialize the sdk
    VZBOptions *vizbeeOptions = [VZBOptions new];
    VizbeeAppAdapter* vizbeeAppAdapter = [[VizbeeAppAdapter alloc] init];
    [Vizbee startWithAppID:vizbeeAppId appAdapterDelegate:vizbeeAppAdapter andVizbeeOptions:vizbeeOptions];

    [self initNotifications];
}

-(void) initNotifications {

    [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(onApplicationWillResignActive:)
                                               name:UIApplicationWillResignActiveNotification
                                             object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(onApplicationDidBecomeActive:)
                                                 name:UIApplicationDidBecomeActiveNotification
                                               object:nil];
    // force first update
    [self onApplicationDidBecomeActive:nil];
}

//----------------
#pragma mark - Flow APIs
//----------------

-(void) addCastIcon:(CDVInvokedUrlCommand*) command {

    NSLog(@"Adding CastIcon");
    [self topViewControllerThreadSafe:^(UIViewController* vc) {

        // create cast button
        VZBCastButton* castButton = [Vizbee createCastButton];
        [vc.view addSubview:castButton];

        // add autolayout constraints
        castButton.translatesAutoresizingMaskIntoConstraints = false;
        NSLayoutConstraint* constraintTop = [NSLayoutConstraint constraintWithItem:castButton attribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:vc.view attribute:NSLayoutAttributeTop multiplier:1.0 constant:48.0];
        NSLayoutConstraint* constraintTrailing = [NSLayoutConstraint constraintWithItem:castButton attribute:NSLayoutAttributeTrailing relatedBy:NSLayoutRelationEqual toItem:vc.view attribute:NSLayoutAttributeTrailing multiplier:1.0 constant:-24.0];
        NSLayoutConstraint* constraintWidth = [NSLayoutConstraint constraintWithItem:castButton attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:nil attribute:NSLayoutAttributeNotAnAttribute multiplier:1.0 constant:24.0];
        NSLayoutConstraint* constraintHeight = [NSLayoutConstraint constraintWithItem:castButton attribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:nil attribute:NSLayoutAttributeNotAnAttribute multiplier:1.0 constant:24.0];
        [NSLayoutConstraint activateConstraints:@[constraintTop, constraintTrailing, constraintWidth, constraintHeight]];
    }];
}

-(void) removeCastIcon:(CDVInvokedUrlCommand *)command {

    NSLog(@"Remove CastIcon");
    [self topViewControllerThreadSafe:^(UIViewController* vc) {

        for(id view in vc.view.subviews) {

           if([view isKindOfClass:NSClassFromString(@"VZBCastButton")]) {
                NSLog(@"Found CastIcon, removing");
                [view removeFromSuperview];
           }
        }
    }];
}

-(void) smartPrompt:(CDVInvokedUrlCommand*) command {

    NSLog(@"Invoking smartPrompt");
    [self topViewControllerThreadSafe:^(UIViewController* vc) {
        
        if (nil == vc) {
            NSLog(@"SmartPrompt - nil viewcontroller");
            return;
        }
        
        // Renamed old smartHelp API to new smartPrompt
        [Vizbee smartHelp:vc];
    }];
}

-(void) smartCast:(CDVInvokedUrlCommand *)command {
    
    NSLog(@"Invoking smartCast");
    [self topViewControllerThreadSafe:^(UIViewController* vc) {
        
        if (nil == vc) {
            NSLog(@"SmartCast - nil viewcontroller");
            return;
        }
        
        VZBSessionManager* sessionManager = [Vizbee getSessionManager];
        if (nil != sessionManager) {
            [sessionManager onCastIconTapped:vc];
        }
    }];
}

-(void) smartPlay:(CDVInvokedUrlCommand *)command {
    
    NSLog(@"Invoking smartPlay");
    [self topViewControllerThreadSafe:^(UIViewController* vc) {
        
        if (nil == vc) {
            NSLog(@"SmartPlay - nil viewcontroller");
            
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Play on Phone"];
            [self.commandDelegate sendPluginResult:pluginResult callbackId: command.callbackId];
            return;
        }

        NSDictionary* vizbeeVideoMap = command.arguments[0];
        VizbeeVideo* vizbeeVideo = [[VizbeeVideo alloc] init:vizbeeVideoMap];
        BOOL didPlayOnTV = [Vizbee smartPlay:vizbeeVideo
                                     atPosition:vizbeeVideo.startPositionInSeconds
                       presentingViewController:vc];
                              
        if (didPlayOnTV) {

            NSLog(@"SmartPlay success in casting content");
            if (nil != command) {
                 CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Playing on TV"];
                 [self.commandDelegate sendPluginResult:pluginResult callbackId: command.callbackId];
            }

        } else {

            NSLog(@"SmartPlay failed in casting content");
            if (nil != command){
                CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Play on Phone"];
                [self.commandDelegate sendPluginResult:pluginResult callbackId: command.callbackId];
            }
        }
    }];
}

//----------------
#pragma mark - Session APIs
//----------------
/*
RCT_REMAP_METHOD(getSessionState, getSessionStateWithResolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTLogInfo(@"Invoking getSessionState");

    VZBSessionManager* sessionManager = [Vizbee getSessionManager];
    if (nil == sessionManager) {
          reject(@"No session manager", @"Session manager is nil", nil);
          return;
    }

    resolve([self getSessionStateString:[sessionManager getSessionState]]);
}

RCT_REMAP_METHOD(getSessionConnectedDevice, getSessionConnectedDeviceWithResolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTLogInfo(@"Invoking getSessionConnectedDevice");
  
    VZBSessionManager* sessionManager = [Vizbee getSessionManager];
    if (nil == sessionManager) {
          reject(@"No session manager", @"Session manager is nil", nil);
          return;
    }
  
    NSDictionary* map = [self getSessionConnectedDeviceMap];
    RCTLogInfo(@"getSessionConnectedDevice %@", map);
    resolve(map);
}
*/

//----------------
#pragma mark - Video APIs
//----------------

-(void) play:(CDVInvokedUrlCommand *)command {

    VZBVideoClient* videoClient = [self getSessionVideoClient];
    if (nil != videoClient) {
        [videoClient play];
    } else {
        NSLog(@"Play ignored because videoClient is null");
    }
}

-(void) pause:(CDVInvokedUrlCommand *)command {
  
    VZBVideoClient* videoClient = [self getSessionVideoClient];
    if (nil != videoClient) {
        [videoClient pause];
    } else {
        NSLog(@"Pause ignored because videoClient is null");
    }
}

-(void) seek:(CDVInvokedUrlCommand *)command {
  
    VZBVideoClient* videoClient = [self getSessionVideoClient];
    NSNumber* positionNumber = command.arguments[0];
    if (nil != videoClient) {
        [videoClient seek:positionNumber.floatValue];
    } else {
        NSLog(@"Seek ignored because videoClient is null");
    }
}

-(void) stop:(CDVInvokedUrlCommand *)command {
  
    VZBVideoClient* videoClient = [self getSessionVideoClient];
    if (nil != videoClient) {
        [videoClient stop];
    } else {
        NSLog(@"Stop ignored because videoClient is null");
    }
}

//----------------
#pragma mark - App & session lifecycle
//----------------

-(void) onApplicationDidBecomeActive:(NSNotification*)notification {
    NSLog(@"onApplicationDidBecomeActive - adding session state listener");
    [self addSessionStateListener];
}

-(void) onApplicationWillResignActive:(NSNotification*)notification {
    NSLog(@"onApplicationWillResignActive - removing session state listener");
    [self removeSessionStateListener];
}

-(void) onSessionStateChanged:(VZBSessionState)newState {
    
    [self notifySessionStatus:newState];
    
    // handle videoClient
    if (newState == VZBSessionStateConnected) {
        [self addVideoStatusListener];
    } else {
        [self removeVideoStatusListener];
    }
}

-(void) addSessionStateListener {

    // sanity
    [self removeSessionStateListener];
    
    VZBSessionManager* sessionManager = [Vizbee getSessionManager];
    if (nil != sessionManager) {
        
        NSLog(@"RNVZBSDK: Adding session state listener");
        [sessionManager addSessionStateDelegate:self];

        // force first update
        [self notifySessionStatus:[sessionManager getSessionState]];
    }
}

-(void) removeSessionStateListener {

    VZBSessionManager* sessionManager = [Vizbee getSessionManager];
    if (nil != sessionManager) {

        NSLog(@"RNVZBSDK: Removing session state listener");
        [sessionManager removeSessionStateDelegate:self];
    }
}

-(void) notifySessionStatus:(VZBSessionState) newState {

    if (newState == self.lastUpdatedState) {
        NSLog(@"RNVZBSDK: Ignoring duplicate state update");
        return;
    }
    self.lastUpdatedState = newState;

    NSString* state = [self getSessionStateString:newState];
    NSMutableDictionary* stateMap = [NSMutableDictionary new];
    [stateMap setObject:state forKey:@"connectionState"];

    NSMutableDictionary* deviceMap = [self getSessionConnectedDeviceMap];
    if (nil != deviceMap) {
        [stateMap addEntriesFromDictionary:deviceMap];
    }

    NSLog(@"RNVZBSDK: Sending session status %@", stateMap);
//    [self sendEvent:@"VZB_SESSION_STATUS" withBody:stateMap];
}

-(NSString*) getSessionStateString:(VZBSessionState) state {

    switch (state) {
        case VZBSessionStateNoDeviceAvailable:
            return @"NO_DEVICES_AVAILABLE";
        case VZBSessionStateNotConnected:
            return @"NOT_CONNECTED";
        case VZBSessionStateConnecting:
            return @"CONNECTING";
        case VZBSessionStateConnected:
            return @"CONNECTED";
        default:
            return @"UNKNOWN";
    }
}

-(NSMutableDictionary*) getSessionConnectedDeviceMap {
    
    VZBSessionManager* sessionManager = [Vizbee getSessionManager];
    if (nil == sessionManager) {
        return nil;
    }

    VZBSession* currentSession = [sessionManager getCurrentSession];
    if (nil == currentSession) {
        return nil;
    }

    VZBScreen* screen = currentSession.vizbeeScreen;
    if (nil == screen) {
      return nil;
    }

    NSMutableDictionary* map = [NSMutableDictionary new];
    [map setObject:screen.screenType.typeName forKey:@"connectedDeviceType"];
    [map setObject:screen.screenInfo.friendlyName forKey:@"connectedDeviceFriendlyName"];
    [map setObject:screen.screenInfo.model forKey:@"connectedDeviceModel"];
    return map;
}

//----------------
#pragma mark - Video client listener
//----------------
    
- (void)onVideoStatusUpdate:(VZBVideoStatus *)videoStatus {
    [self notifyMediaStatus:videoStatus];
}

-(VZBVideoClient*) getSessionVideoClient {

    VZBSessionManager* sessionManager = [Vizbee getSessionManager];
    if (nil == sessionManager) {
        return nil;
    }

    VZBSession* currentSession = [sessionManager getCurrentSession];
    if (nil == currentSession) {
        return nil;
    }

    VZBVideoClient* videoClient = currentSession.videoClient;
    return videoClient;
}

-(void) addVideoStatusListener {

    // sanity
    [self removeVideoStatusListener];

    NSLog(@"RNVZBSDK: Trying to add video status listener");
    VZBVideoClient* videoClient = [self getSessionVideoClient];
    if (nil != videoClient) {

        [videoClient addVideoStatusDelegate:self];
        NSLog(@"RNVZBSDK: Success adding video status listener");

        // force first update
        [self notifyMediaStatus:[videoClient getVideoStatus]];
    } else {

        NSLog(@"RNVZBSDK: Failed adding video status listener");
    }
}

-(void) removeVideoStatusListener {
    
    NSLog(@"RNVZBSDK: Trying to remove video status listener");
    VZBVideoClient* videoClient = [self getSessionVideoClient];
    if (nil != videoClient) {
        
        NSLog(@"RNVZBSDK: Success removing video status listener");
        [videoClient removeVideoStatusDelegate:self];
    }
}

-(void) notifyMediaStatus:(VZBVideoStatus*) videoStatus {

    NSLog(@"RNVZBSDK: Sending media status %@", videoStatus);
//    NSDictionary* videoStatusMap = [self getVideoStatusMap:videoStatus];
//    [self sendEvent:@"VZB_MEDIA_STATUS" withBody:videoStatusMap];
}

-(NSMutableDictionary*) getVideoStatusMap:(VZBVideoStatus*) videoStatus {

    NSMutableDictionary* videoStatusMap = [NSMutableDictionary new];
    [videoStatusMap setObject:videoStatus.guid forKey:@"guid"];
    [videoStatusMap setObject:[NSNumber numberWithInt:videoStatus.streamPosition] forKey:@"position"];
    [videoStatusMap setObject:[NSNumber numberWithInt:videoStatus.streamDuration] forKey:@"duration"];
    [videoStatusMap setObject:[NSNumber numberWithBool:videoStatus.isStreamLive] forKey:@"isLive"];
    [videoStatusMap setObject:[self getPlayerStateString:videoStatus.playerState] forKey:@"playerState"];
    [videoStatusMap setObject:[NSNumber numberWithBool:videoStatus.isAdPlaying] forKey:@"isAdPlaying"];
    
    return videoStatusMap;
}

-(NSString*) getPlayerStateString:(VZBVideoPlayerState) state {

    switch (state) {
        case VZBVideoPlayerStateIdle:
            return @"IDLE";
        case VZBVideoPlayerStatePlaying:
            return @"PLAYING";
        case VZBVideoPlayerStatePaused:
            return @"PAUSED";
        case VZBVideoPlayerStateBuffering:
            return @"BUFFERING";
        default:
            return @"UNKNOWN";
    }
}

//----------------
#pragma mark - Helpers
//----------------

- (UIViewController *) topViewController {

    UIViewController *topVC = [[[UIApplication sharedApplication] keyWindow] rootViewController];
    while (topVC.presentedViewController) {
        topVC = topVC.presentedViewController;
    }
    return topVC;
}

-(void) topViewControllerThreadSafe:(void (^)(UIViewController*)) completion {
    
    dispatch_async(dispatch_get_main_queue(), ^(){
    
        UIViewController *topVC = [self topViewController];
        completion(topVC);
    });
}

@end
