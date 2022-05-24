
# cordova-plugin-vizbee-sender-sdk
<img src="https://static.claspws.tv/images/common/logos/vizbee_logo_tagline.png" alt="vizbee" width="400"/>

## Overview

Vizbee enables streaming apps to deliver great cross-device experiences. This repository provides the Cordova Plugin for enabling Vizbee's cross-device experiences. You need a [Vizbee account](https://console.vizbee.tv) in order to use this SDK.

## Build setup

See [build setup instructions](https://gist.github.com/vizbee/7b725288d2ef6ec906109a4e1a9c1ad9) for Cordova Plugin Vizbee Sender SDK.

## Developer guide

Once you are setup, you can get further instructions for integrating this Cordova Plugin Vizbeee Sender from the [Vizbee console]([https://console.vizbee.tv](https://console.vizbee.tv/app/vzb2018119/develop/guides/reactnative-snippets))
  

## Build Errors

If some plugins has config-file tag for *-Info.plist and config.xml has edit-config tag for *-Info.plist, this error may happen. In this case, remove platforms/* and plugins/*, and then cordova platform add ios and cordova prepare again. (If you do cordova prepare without cordova platform add ios, this error may happen)
