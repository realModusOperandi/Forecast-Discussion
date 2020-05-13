let last_known_scroll_position = 0;
let ticking = false;

function doSomething(scroll_pos) {
    let navcontainer = document.getElementById('navcontainer');
    if (scroll_pos > 50) {
        navcontainer.classList.remove('navtop');
        navcontainer.classList.add('navscroll');
    } else {
        navcontainer.classList.remove('navscroll');
        navcontainer.classList.add('navtop');
    }
}

window.addEventListener('scroll', function (e) {
    last_known_scroll_position = window.scrollY;

    if (!ticking) {
        window.requestAnimationFrame(function () {
            doSomething(last_known_scroll_position);
            ticking = false;
        });

        ticking = true;
    }
});