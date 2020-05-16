let last_known_scroll_position = 0;
let ticking = false;

function updateNav(scroll_pos) {
    let navcontainer = document.getElementById('nav-bg');
    if (scroll_pos > 50) {
        navcontainer.classList.remove('navtop');
        navcontainer.classList.add('navscroll');
    } else {
        navcontainer.classList.remove('navscroll');
        navcontainer.classList.add('navtop');
    }
}

window.addEventListener('scroll', e => {
    last_known_scroll_position = window.scrollY;

    if (!ticking) {
        window.requestAnimationFrame(function () {
            updateNav(last_known_scroll_position);
            ticking = false;
        });

        ticking = true;
    }
});

window.onload = e => {
    let navcontainer = document.getElementById('nav-bg');
    navcontainer.style.transition = 'none';
    updateNav(window.scrollY);
    navcontainer.offsetHeight; // ensure layout is up to date
    navcontainer.style.transition = '';
}