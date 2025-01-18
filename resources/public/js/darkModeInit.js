function isDark() {
    return localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches);
}

function toggleDarkClass(isDark) {
    document.documentElement.classList.toggle(
        'dark',
        isDark
    )
}

toggleDarkClass(isDark())

document.addEventListener('alpine:init', () => {
    Alpine.store('theme', {
        dark: isDark(),
        toggleTheme() {
            this.dark = !this.dark;
            localStorage.theme = this.dark ? 'dark' : 'light';
            toggleDarkClass(this.dark)
        }
    });
});