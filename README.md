# Fishbowl

[![Build Status](https://travis-ci.org/stefanbirkner/fishbowl.svg?branch=master)](https://travis-ci.org/stefanbirkner/fishbowl)

Fishbowl makes it possible to use the
[AAA (Arrange-Act-Assert) pattern](http://c2.com/cgi/wiki?ArrangeActAssert)
for writing tests for code that throws an exception. For that purpose it
exposes the exception so that it can be checked by any assertion
library.

Fishbowl is published under the
[MIT license](http://opensource.org/licenses/MIT).


## Installation

Fishbowl is available from [Maven Central](http://search.maven.org/).

    <dependency>
      <groupId>com.github.stefanbirkner</groupId>
      <artifactId>fishbowl</artifactId>
      <version>1.1.1</version>
    </dependency>

Please don't forget to add the scope `test` if you use Fishbowl for
tests only.


## Usage

Fishbowl's documentation is the Javadoc of the class `Fishbowl`. It is
available online at
http://stefanbirkner.github.io/fishbowl/apidocs/com/github/stefanbirkner/fishbowl/Fishbowl.html


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

## Release Notes

### Release 1.1.1

Recompile the library with JDK 7. Release 1.1.0 has been created with
JDK 8 and because of this the library cannot be used with JDK < 8.
Compilation aborts with the message

    Unsupported major.minor version 52.0

### Release 1.1.0

Add a new method

    exceptionThrownBy(Statement, Class<? extends Throwable>)

that returns a certain type of exception. This is necessary for
verifying the state of custom exceptions.

### Release 1.0.1

Recompile the library with JDK 7. Release 1.0.0 has been created with
JDK 8 and because of this the library cannot be used with JDK < 8.
Compilation aborts with the message

    Unsupported major.minor version 52.0

### Release 1.0.0

Initial release.
