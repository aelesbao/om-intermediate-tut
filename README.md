# Om Intermediate Tutorial

[Check complete tutorial here](https://github.com/omcljs/om/wiki/Intermediate-Tutorial)

## Setup

First download a copy of [Datomic Free](http://my.datomic.com/downloads/free). Unzip it and run the following inside the directory:

    bin/transactor config/samples/free-transactor-template.properties

Launch a Lein repl with lein repl. Once the repl is up run the following:

    user=> (use 'om-async.util)
    nil
    user=> (init-db)
    :done

The tutorial database now exists and is populated. Quit the REPL.

If you got an error, try an earlier version of Datomic Free.

## How to run

We will use [Figwheel](https://github.com/bhauman/lein-figwheel) to reload our front end ClojureScript while we code. Figwheel uses a server to auto compile our code and push it to the browser. But we also need a server running our back end code. To start both the server and the compilation process, run:

    lein figwheel

When the server is up, point your browser at [localhost:3449](http://localhost:3449). When it is done compiling, check if the Browser REPL is connected by typing:

    ClojureScript:cljs.user> (js/alert "Am I connected?")