# Clojure Basics

Playground project for experimentation and learning of Clojure features:

* Basics and foundations of Clojure
* Collections (lists, sets and maps)
* Functions
* Datomic (distributed database made in Clojure)
* Basic I/O functions
* Schema (using [prismatic/schema](https://github.com/plumatic/schema))

Requirements:

* JDK 11
* Maven
* Homebrew
* SDKMAN

## Java Installation

Install java version using `sdkman`:

`sdk install java 11.0.17-tem`

## Clojure Installation

Install clojure using `homebrew`:

`brew install clojure/tools/clojure`

Run in the terminal:

```clojure
> clj
Clojure 1.11.1
user=>
> (println "hello world")
hello world
```

## Datomic Installation

1. Create an account at [Datomic](https://www.datomic.com/get-datomic.html)
2. Get a `starter` version
3. Download the latest version and extract files
4. Install maven dependencies locally: `bin/maven-install`
5. Copy file for dev transactor `cp -vi config/samples/dev-transactor-template.properties config/dev-transactor.properties`
6. Edit configuration and paste the license received by e-mail
7. Start the datomic transactor `bin/transactor config/dev-transactor.properties`

## Quick Start

1. Clone this repo
2. Run tests `lein test`
3. Run code using `REPL`

### References

* [Clojure](https://clojure.org/)
* [Cursive - IntelliJ Plugin](https://cursive-ide.com/)
* [Datomic](https://www.datomic.com/)
* [SDKMAN](https://sdkman.io/)
* [Homebrew](https://brew.sh/)