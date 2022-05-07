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
	addCastIcon : function(x, y) {
        exec(null, null, "VizbeeSenderSDK", "addCastIcon", [x, y]);
    },

    /**
    * Remove CastIcon
    */
    removeCastIcon : function() {
         exec(null, null, "VizbeeSenderSDK", "removeCastIcon", []);
    },

    /**
	 * Smart Prompt (Cast Introduction or Smart Install)
	 */
    smartPrompt : function() {
         exec(null, null, "VizbeeSenderSDK", "smartPrompt", []);
    },

    /**
	 * Smart Cast (CastIcon Click Action)
	 */
    smartCast : function() {
         exec(null, null, "VizbeeSenderSDK", "smartCast", []);
    },

    /**
    * Smart Play
    */
    smartPlay : function(vizbeeVideoMap, success, error) {
        exec(success, error, "VizbeeSenderSDK", "smartPlay", [vizbeeVideoMap]);
    },

    //----------------
    // Session APIs
    //----------------

    // TODO:

    //----------------
   // Video APIs
   //----------------

   /**
   * Play
   */
   play : function(success, error) {
       exec(success, error, "VizbeeSenderSDK", "play", []);
   },

   /**
   * Pause
   */
   pause : function(success, error) {
      exec(success, error, "VizbeeSenderSDK", "pause", []);
   },

   /**
   * Seek
   */
   seek : function(position, success, error) {
     exec(success, error, "VizbeeSenderSDK", "seek", [position]);
   },

    /**
    * Stop
    */
    stop : function(success, error) {
        exec(success, error, "VizbeeSenderSDK", "stop", []);
    }
};
