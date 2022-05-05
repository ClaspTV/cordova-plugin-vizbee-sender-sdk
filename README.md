<h1 align="center">cordova-plugin-vizbee-sender-sdk</h1>

# Installation

```
cordova plugin add https://github.com/ClaspTV/cordova-plugin-vizbee-sender-sdk.git
```

*Add the plugin with the --link option to test it during development*
```
cordova plugin add /<path>/cordova-plugin-vizbee-sender-sdk --link
```

If you have trouble installing the plugin or running the project for iOS, from `/platforms/ios/` try running:  
```bash
sudo gem install cocoapods
pod repo update
pod install
```

# Errors

If some plugins has config-file tag for *-Info.plist and config.xml has edit-config tag for *-Info.plist, this error may happen. In this case, remove platforms/* and plugins/*, and then cordova platform add ios and cordova prepare again. (If you do cordova prepare without cordova platform add ios, this error may happen)