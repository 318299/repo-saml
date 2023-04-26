(function() {
    'use strict';

    /**
     * @param {Object} region
     * @returns {function}
     */
    var countryRegion = function(region) {

        var processed = false;

        return function isCountryFromRegion() {
            /* let the SegmentEngine know when script should be re-run */
            if (!processed) {
                ContextHub.SegmentEngine.Dependency.dependencyMonitor(this, false);
                this.dependOn(ContextHub.SegmentEngine.Property('profile/countryCode'));
                ContextHub.SegmentEngine.Dependency.dependencyMonitor(this, true);
            }

            /* variables */
            var country = ContextHub.get('profile/countryCode');

            /* return result */
            return country && window.AEMCX && AEMCX.regions[country.toUpperCase()] === region;
        }
    };

    /* register functions */
    ContextHub.SegmentEngine.ScriptManager.register('Africa', countryRegion('AF'));
    ContextHub.SegmentEngine.ScriptManager.register('Antarctica', countryRegion('OC'));
    ContextHub.SegmentEngine.ScriptManager.register('Asia', countryRegion('AS'));
    ContextHub.SegmentEngine.ScriptManager.register('Europe', countryRegion('EU'));
    ContextHub.SegmentEngine.ScriptManager.register('North America', countryRegion('NA'));
    ContextHub.SegmentEngine.ScriptManager.register('Oceania', countryRegion('OC'));
    ContextHub.SegmentEngine.ScriptManager.register('South America', countryRegion('SA'));

})();