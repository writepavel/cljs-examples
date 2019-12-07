# Example project for target nodejs using `clojure` as build tool

## REPL

### Terminal

`yarn build:dev` starts the REPL in every terminal.

### VSCode with calva

Just `Calva: Jack In` and select `fig` and `build`.

### VSCode Remote with Calva

In VSCode Remote container, start `yarn nrepl:fig`, then run task `Calva: Connect to running REPL in the project`.

## Configuration

*./deps.edn* The configuration file format [for the `clj` tools] is an edn map with top-level keys for :deps, :paths, and :aliases, plus provider-specific keys for configuring dependency sources. [Source: https://web.archive.org/save/https://clojure.org/reference/deps_and_cli#_deps_edn]

*./figwheel-main.edn* Main configuration for `figwheel-main` (which is not `lein-figwheel`). [Source: https://web.archive.org/web/20191207110228/https://figwheel.org/#configuring-figwheel-main]

*./dev.cljs.edn* *./test.cljs.edn* For `clj -m figwheel.main`, the `-b dev` or `-b test` options indicate that compiler options must be read from the according `*.cljs.edn` file. [Source: https://web.archive.org/save/https://figwheel.org/#setting-up-a-build-with-tools-cli]

## Directories

*./src* Place for code.

*./target* Place where figwheel compiles the code to.