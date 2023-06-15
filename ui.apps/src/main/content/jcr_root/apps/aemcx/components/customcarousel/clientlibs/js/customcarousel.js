var imgList = document.getElementById('imgList');
  var scrollRight = document.getElementById('scroll-right');
  var scrollLeft = document.getElementById('scroll-left');

// When a user clicks on the right arrow, the ul will scroll 750px to the right
  scrollRight.addEventListener('click', scrollRightSide);

function scrollRightSide() {
    imgList.scrollBy(100, 0);
}

// When a user clicks on the left arrow, the ul will scroll 750px to the left
  scrollLeft.addEventListener('click', scrollLeftSide);

function scrollLeftSide(){
    imgList.scrollBy(-100, 0);
}