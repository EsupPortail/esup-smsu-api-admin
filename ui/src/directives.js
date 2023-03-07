export const whenVisible = { created: function (el, binding) {
    const callback = binding.value
    if (!callback) return // disabled
    if (window.IntersectionObserver && window.IntersectionObserverEntry && 'isIntersecting' in window.IntersectionObserverEntry.prototype) {
        new IntersectionObserver((events) => {
            if (events.some(e => e.isIntersecting)) setTimeout(_ => callback(el), 100)
        }).observe(el)
    }
} }
