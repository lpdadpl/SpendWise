/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/**/*.{html,ts}",
    ],
    theme: {
        extend: {
            colors: {
                'primary-blue': '#5B7C99',
                'primary-green': '#7FB685',
                'dark-blue': '#2C3E50',
                'light-blue': '#E8EDF2',
                'light-green': '#E8F3EA',
            }
        },
    },
    plugins: [],
}
