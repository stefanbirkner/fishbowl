# Fishbowl

[![Build Status](https://travis-ci.org/stefanbirkner/fishbowl.svg?branch=master)](https://travis-ci.org/stefanbirkner/fishbowl)

Fishbowl provides helper methods for dealing with exceptions.

Fishbowl is published under the
[MIT license](http://opensource.org/licenses/MIT).


## Installation

Fishbowl is available from [Maven Central](http://search.maven.org/).

    <dependency>
      <groupId>com.github.stefanbirkner</groupId>
      <artifactId>fishbowl</artifactId>
      <version>1.4.0</version>
    </dependency>

Please don't forget to add the scope `test` if you use Fishbowl for
tests only.


## Usage

Fishbowl's documentation is stored in the `gh-pages` branch and is
available online at
http://stefanbirkner.github.io/fishbowl/index.html


## Contributing

You have three options if you have a feature request, found a bug or
simply have a question about Fishbowl.

* [Write an issue.](https://github.com/stefanbirkner/fishbowl/issues/new)
* Create a pull request. (See [Understanding the GitHub Flow](https://guides.github.com/introduction/flow/index.html))
* [Write an email to mail@stefan-birkner.de](mailto:mail@stefan-birkner.de)


## Development Guide

Fishbowl is build with [Maven](http://maven.apache.org/). If you want to
contribute code than

* Please write a test for your change.
* Ensure that you didn't break the build by running `mvn test`.
* Fork the repo and create a pull request. (See [Understanding the GitHub Flow](https://guides.github.com/introduction/flow/index.html))

The basic coding style is described in the
[EditorConfig](http://editorconfig.org/) file `.editorconfig`.

Fishbowl supports [Travis CI](https://travis-ci.org/) for continuous
integration. Your pull request will be automatically build by Travis
CI.


## Release Guide

* Select a new version according to the
  [Semantic Versioning 2.0.0 Standard](http://semver.org/).
* Set the new version in `pom.xml` and in the `Installation` section of
  this readme.
* Commit the modified `pom.xml` and `README.md`.
* Run `mvn clean deploy` with JDK 6 or 7.
* Add a tag for the release: `git tag fishbowl-X.X.X`
