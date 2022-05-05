var exec = require('cordova/exec');

module.exports = {

    /**
	 * Initialize VizbeeSDK
	 */
	initialize : function(appId) {
        exec(null, null, "VizbeeSenderSDK", "initialize", [appId]);
    },

    /**
	 * Add CastIcon
	 */
	addCastIcon : function() {
        exec(null, null, "VizbeeSenderSDK", "addCastIcon", []);
    },

    /**
	 * Smart Prompt (Cast Introduction or Smart Install)
	 */
    smartPrompt : function() {
         exec(null, null, "VizbeeSenderSDK", "smartPrompt", []);
    },

    /**
    * Smart Play
    */
    smartPlay : function(vizbeeVideoMap, success, error) {
        exec(success, error, "VizbeeSenderSDK", "smartPlay", [vizbeeVideoMap]);
    }
};
