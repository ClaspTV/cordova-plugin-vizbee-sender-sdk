//
//  VizbeeAppAdapter.h
//  VizbeeKit
//

#import <Foundation/Foundation.h>
#import <VizbeeKit/VizbeeKit.h>
#import <Cordova/CDV.h>

@interface VizbeeAppAdapter : NSObject <VZBAppAdapterDelegate>

@property (nonatomic, strong) NSString* callbackId;
@property (nonatomic, weak) id <CDVCommandDelegate> commandDelegate;

@end
