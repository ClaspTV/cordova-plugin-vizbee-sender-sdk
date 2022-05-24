
# cordova-plugin-vizbee-sender-sdk
<img src="https://static.claspws.tv/images/common/logos/vizbee_logo_tagline.png" alt="vizbee" width="400"/>

## Overview

Vizbee enables streaming apps to deliver great cross-device experiences. This repository provides the Cordova Plugin for enabling Vizbee's cross-device experiences. You need a [Vizbee account](https://console.vizbee.tv) in order to use this SDK.

## Build setup & Developer guide

See [build setup & integration instructions](https://gist.github.com/vizbee/7b725288d2ef6ec906109a4e1a9c1ad9) for Cordova Plugin Vizbee Sender SDK.
  

## Build Errors

### iOS

- Failed to update the `*-Info.plist`  
  - If some plugins has `config-file` tag for `*-Info.plist` and `config.xml` has `edit-config` tag for `*-Info.plist`, this error may happen. In this case, remove `platforms/*` and `plugins/*`, and then cordova platform add ios and cordova prepare again. (If you do cordova prepare without cordova platform add ios, this error may happen)

### Android
