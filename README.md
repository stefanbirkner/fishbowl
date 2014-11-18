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
      <version>1.0.0</version>
    </dependency>

Please don't forget to add the scope `test` if you use Fishbowl for
tests only.


## Usage

The following test verifies that the statement `noString.trim()` throws
a `NullPointerException`.

    import static com.github.stefanbirkner.fishbowl.Fishbowl.exceptionThrownBy;

    ...

    @Test
    public void anExceptionIsThrown() {
      String noString = null;
      Throwable exception = exceptionThrownBy(() -> noString.trim());
      assertEquals(NullPointerException.class, exception.getClass());
    }

Fishbowl exposes the exception that is thrown by the piece of code that
has been provided to `exceptionThrownBy`. This exception can be checked
by any assertion library. (The example uses JUnit's `Assert` class.)

In case that the statement did not throw an exception, Fishbowl itself
throws an `ExceptionNotThrownFailure`. This causes the test to fail

    com.github.stefanbirkner.fishbowl.ExceptionNotThrownFailure: The Statement did not throw an exception.

### Example for Several Assertion Libraries

The example above uses JUnit's `Assert` class. Below is the same test
with other assertion libraries.

#### Hamcrest

    @Test
    public void anExceptionIsThrown() {
      String noString = null;
      Throwable exception = exceptionThrownBy(() -> noString.trim());
      assertThat(exception, instanceOf(NullPointerException.class));
    }

#### AssertJ, FEST, Truth

The test looks the same for all three assertion libraries. The only
difference is the class that provides `assertThat`.

    @Test
    public void anExceptionIsThrown() {
      String noString = null;
      Throwable exception = exceptionThrownBy(() -> noString.trim());
      assertThat(exception).isInstanceOf(NullPointerException.class);
    }

For AssertJ please have a look at
[AbstractThrowableAssert](http://joel-costigliola.github.io/assertj/core/api/org/assertj/core/api/AbstractThrowableAssert.html)
for further asserts.

### Java 6 and 7

Fishbowl has been created with Java 8 in mind, but it can be used with
Java 6 and 7, too. In this case you have to use anonymous classes. Here
is the example from above for Java 6 and 7.

    @Test
    public void anExceptionIsThrown() {
      final String noString = null;
      Throwable exception = exceptionThrownBy(new Statement() {
        public void evaluate() throws Throwable {
          noString.trim();
        }
      });
      assertEquals(NullPointerException.class, exception.getClass());
    }


## Contributing

You have three options if you have a feature request, found a bug or
simply have a question about Fishbowl.

* [Write an issue.](https://github.com/stefanbirkner/quaidan/issues/new)
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
* Set the new version in the `Installation` section of this readme.
* Run `mvn release:prepare`
* Run `mvn release:perform`
