document.documentElement.classList.toggle(
    'dark',
    localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)
)

document.addEventListener('alpine:init', () => {
    Alpine.store('theme', {
        dark: localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches),
        darkTheme() {
            this.dark = true;
            localStorage.theme = 'dark';
            document.documentElement.classList.add('dark');
        },
        lightTheme() {
            this.dark = false;
            localStorage.theme = 'light';
            document.documentElement.classList.remove('dark');
        }
    });
});