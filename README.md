# clojure-service-template



### Build TailwindCSS

Template is configured to use `tailwindcss-cli` to build production-ready CSS files. 

Installation and usage example:

```shell
brew install tailwindcss

tailwindcss init

bb tailwindcss:build
bb tailwind:watch
bb tailwind:minify
```

Build, watch or minify will generate CSS file: `resources/public/css/output.css`.

### Developer Experience:

```shell
The following tasks are available:

app:precommit Run all checks and tests
app:lint      Lint with `clj-kondo`
app:fmt:check Check code format with `cljfmt`
app:fmt:fix   Reformat code with `cljfmt`
app:run       Start application
app:run:prod  Start application in production
app:test      Run all tests
app:start     Start local instance of service.
app:antq      Check for outdated dependencies with `antq`
app:deps      Install all dependencies
```

