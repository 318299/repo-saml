(function ($) {
    $(function () {
        $('.cmp-header__menu-trigger').on('click', function () {
            $(this).toggleClass('icon-close icon-menu');
            $(".cmp-header__menu-list").toggleClass('active');
        });

        $('#aemcx-search').on('show.bs.modal hidden.bs.modal', function () {
            $('body').toggleClass('aemcx-search-color');
        });

        $('#aemcx-search').on('shown.bs.modal', function () {
            // focus on search input
            $('.cmp-search__input', $(this)).focus();
        });
    });
})(jQuery);