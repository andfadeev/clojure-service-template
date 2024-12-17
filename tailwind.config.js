/** @type {import('tailwindcss').Config} */
module.exports = {
  // Use safelist for dynamic class names
  // safelist: [
  //   "bg-red-500",
  //   "text-lg",
  //   "font-bold",
  // ],
  content: [
    "./src/**/*.{clj,cljs,cljc}"
  ],
  theme: {
    extend: {
      container: {
        center: true,
        padding: '1rem',
        screens: {
          DEFAULT: '600px',
        },
      },
      fontFamily: {
        sans: ['Titillium Web',  'sans-serif'],
      },
    },
  },
  plugins: [],
};
