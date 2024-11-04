import Cookies from 'js-cookie';

const CookieService = {
    // Get a cookie by name
    getCookie(name) {
        return Cookies.get(name);
    },

    // Set a cookie with a name, value, and options
    setCookie(name, value, options = {}) {
        Cookies.set(name, value, { ...options });
    },

    // Remove a cookie by name
    removeCookie(name) {
        Cookies.remove(name);
    }
};

export default CookieService;
