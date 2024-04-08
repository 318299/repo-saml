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
function languageFrench(){
    var langF= window.location.href;
    langF=langF.replace("/en", "/fr");
    location.href=langF;
   var element = document.getElementById("#myElement"); 
  element.textContent = location.href;
  }
  
  
  
  function languageEnglish(){
    var langE= window.location.href;
    langE=langE.replace("/fr", "/en");
      location.href=langE;
      var element = document.getElementById("#myElement1"); 
  element.textContent = location.href;
  
  }