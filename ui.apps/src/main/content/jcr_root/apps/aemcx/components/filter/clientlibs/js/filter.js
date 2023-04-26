(function ($) {
        $('.cmp-filter__menu-trigger').on('click', function () {
            $(this).toggleClass('icon-close icon-menu');
            $(".cmp-filter__wrapper").toggleClass('active');
        });

        // update location on submit
        var form = $('.cmp-filter__form');
        form.on('submit', function (e) {
            e.preventDefault();
            var inputs = $('input:checked', this).toArray().map(input => input.value);
            var queryParams = new URLSearchParams(window.location.search);
            queryParams.delete("tags");
            inputs.forEach(input => queryParams.append("tags", input));

            window.location.search =  queryParams.toString();
        });

        // set filters when loading the page
        var queryParams = new URLSearchParams(window.location.search);
        var tags = queryParams.getAll('tags');

        tags.forEach(tag => {
            $('input[value="' + tag + '"]', form).prop('checked', true);
        });
})(jQuery);