var exec = require('cordova/exec');

var vizbeeSenderSDKPlugin = {
    initialize:function(arg0, success, error) {
        exec(success, error, "VizbeeSenderSDKPlugin", "initialize", [arg0]);
    },
};

module.exports = vizbeeSenderSDKPlugin;