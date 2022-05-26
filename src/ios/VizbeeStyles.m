//
//  VizbeeStyles.m
//  MGM ROAR
//
//  Created by Shiva on 24/05/22.
//

#import "VizbeeStyles.h"

@implementation VizbeeStyles

//----------------------------
// MARK : - Light Theme
//---------------------------

+(NSDictionary*) lightTheme {
    
    // ===============================================================
    // Basic Style Customization (Required)
    // ===============================================================
    
    return @{
        
        @"base": @"LightTheme",

        @"references" : @{

            /* Your app's theme colors -->

            1. Primary Color - this is typically the background color used on most of your app screens
            2. Secondary Color - this is the highlight or accent color used on buttons etc. in your app screens
            3. Tertiary Color - this is the text color used in most of your app screens

            Update the colors below to suit your app.
            */
            @"@primaryColor"       : @"#FFFFFF",
            @"@secondaryColor"     : @"#FF140F",
            @"@tertiaryColor"      : @"#0C0C0C",
            @"@lighterTertiaryColor" : @"#323236",

            /* Your app's theme fonts

            Provide fonts located in your supporting files folder including any nested directories.
            i.e 'yourFont.<extension>' or 'fonts/yourFont.<extension>.

            Update the fonts below to suit your app.
            */
//            @"@primaryFont"        : @"<apps_primary_font_name>",
//            @"@secondaryFont"      : @"<apps_secondary_font_name>",
        },

        // ===============================================================
        // Advanced Style Customization (Optional)
        // ===============================================================

        @"classes" : @{

            /* Card background

            set this attribute if you prefer an image background for Vizbee screens
            "BackgroundLayer": [
                                "backgroundType"  : "image",
                                "backgroundImageName" : "<image_name.png>"
                                ],
            */

        }

        /*
         OVERLAY CARD
         Customize the Overlay cards as shown here
         https://gist.github.com/vizbee/7e9613f76a546c8f9ed9087e91bdc7b1#customize-overlay-cards
        */

        /*
        INTERSTITIAL CARD

        Customize the Interstitial cards as shown here
        https://gist.github.com/vizbee/7e9613f76a546c8f9ed9087e91bdc7b1#customize-interstitial-cards
        */

         /*
         SPECIFIC CARD ATTRIBUTES

         Player Card
            Customize the Player card as shown here
            https://gist.github.com/vizbee/7e9613f76a546c8f9ed9087e91bdc7b1#customize-player-card
         */
    };
}

//---------------------------
// MARK : - Dark Theme
//---------------------------

+(NSDictionary*) darkTheme {
    
    // ===============================================================
    // Basic Style Customization (Required)
    // ===============================================================

    return @{
        
        @"base" : @"DarkTheme",

        @"references" : @{

            /* Your app's theme colors -->

            1. Primary Color - this is typically the background color used on most of your app screens
            2. Secondary Color - this is the highlight or accent color used on buttons etc. in your app screens
            3. Tertiary Color - this is the text color used in most of your app screens

            Update the colors below to suit your app.
            */
            @"@primaryColor"       : @"#333333",
            @"@secondaryColor"     : @"#d7bb73",
            @"@tertiaryColor"      : @"#FFFFFF",

            /* Your app's theme fonts

            Provide fonts located in your supporting files folder including any nested directories.
            i.e 'yourFont.<extension>' or 'fonts/yourFont.<extension>.

            Update the fonts below to suit your app.
            */
//             @"@primaryFont"        : @"<apps_primary_font_name>",
//             @"@secondaryFont"      : @"<apps_secondary_font_name>",
            
            // Used in player card for video title
            @"@titleLabel": @{
                @"font": @{
                    @"style" : @"title2"
                },
                @"textTransform" : @"capitalize",
            },
            @"@subtitleLabel": @{
                @"font": @{
                    @"style" : @"caption1"
                },
            },
            @"@overlaySubtitleLabel": @{
                @"font": @{
                    @"style" : @"headline"
                },
            },
            
            // Used in app install body
            @"@actionBodyText" : @{
                @"font" : @{
                    @"name" : @"@secondaryFont",
                    @"style" : @"headline",
                },
            },
        },
        @"classes": @{
            @"FloatingCastIcon": @{
                
                @"backgroundColor" : @"@secondaryColor",
                
                @"shadowOffset" : @{@"width" : @0, @"height" : @2},
                @"shadowRadius" : @2,
                @"shadowColor" : @"#000000",
                @"shadowOpacity" : @0.5
            },
            @"CallToActionButton" : @{
                @"backgroundColor" : @"@secondaryColor",
                @"textColor": @"@primaryColor",
            },
            @"OverlayCallToActionButton" : @{
                @"textColor": @"@primaryColor",
            },
            @"ControlButton" : @{
                @"enabledImageColor" : @"@secondaryColor",
            },
        }
    };
}

@end
