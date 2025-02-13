# clojure-service-template


### Build TailwindCSS
Template is configured to use `tailwindcss-cli` to build production-ready CSS files:
```shell
brew install tailwindcss

bb tailwindcss:build
bb tailwind:watch
bb tailwind:minify
```
Build, watch or minify will generate CSS file: `resources/public/css/output.css`.

By default, it's configured to discover Tailwind classes in clj, cljc, and cljs files:
```js 
content: [
    "./src/**/*.{clj,cljs,cljc}"
]
```
You can change that in the `tailwind.config.js` file, for example, if you have plain HTML with TailwindCSS.

If you have dynamic class names, something that's not a string-like literal - you'll have to use `safelist` option, example in the `tailwind.config.js`:
```js
safelist: [
    "bg-red-500",
    "text-lg",
    "font-bold",
]
```

### Developer Experience:
Babashka is used as the tasks runner:
```shell
bb tasks 

The following tasks are available:

app:precommit   Run all checks and tests
app:lint        Lint with `clj-kondo`
app:fmt:check   Check code format with `cljfmt`
app:fmt:fix     Reformat code with `cljfmt`
app:run         Start application
app:run:prod    Start application in production
app:test        Run all tests
app:start       Start local instance of service.
app:antq        Check for outdated dependencies with `antq`
app:deps        Install all dependencies
tailwind:watch  Build TailwindCSS in watch mode
tailwind:build  Build TailwindCSS output
tailwind:minify Build & minify TailwindCSS
```

