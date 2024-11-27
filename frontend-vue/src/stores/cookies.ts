/**
 * Get the value of a cookie by its name.
 * @param {string} name - Name of the cookie.
 * @returns {string|null} - Value of the cookie, or null if not found.
 */
export function getCookie(name) {
  const cookies = document.cookie.split(';');
  for (let cookie of cookies) {
    cookie = cookie.trim();
    if (cookie.startsWith(`${name}=`)) {
      return decodeURIComponent(cookie.substring(name.length + 1));
    }
  }
  return null;
}