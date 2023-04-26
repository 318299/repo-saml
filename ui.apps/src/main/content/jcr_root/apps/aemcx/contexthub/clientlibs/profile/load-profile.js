(function ($, document) {

    var $header = $('.cmp-header'),
        isPublish = $header.data('is-publish');

        if (isPublish) {
            $.ajax({
                type   : "GET",
                url    : "/libs/granite/security/currentuser.json",
                async  : true,
                success: function (json) {
                    // On publish: load the request user into ContextHub
                    if (typeof ContextHub !== "undefined") {
                        var profileStore   = ContextHub.getStore('profile');
                        var requestUser    = json["home"];
                        var contextHubUser = profileStore.getTree().path;
                        if (!contextHubUser || contextHubUser !== requestUser) {
                            profileStore.loadProfile(requestUser);
                        }
                    }
                }
            });
        }

})(jQuery, document);