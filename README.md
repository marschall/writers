Writers [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/writers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/writers) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/writers.svg)](https://www.javadoc.io/doc/com.github.marschall/writers)
=======

Implementations of `java.io.Writer` with different trade-offs.

`java.io.OutputStreamWriter` is very flexible and supports any encoding. However its use of `sun.nio.cs.StreamEncoder` can result an a noticeable overhead for small writes. It allocates a few temporary objects which for small writes can be noticeable. By addressing only special cases we can make optimizations based on differet trade-offs.

Currently we only offer the following two classes:

* `com.github.marschall.writers.AsciiOutputStreamWriter`
  * does not allocate any objects
  * thread-safe
* `com.github.marschall.writers.BufferedAsciiOutputStreamWriter`, like `com.github.marschall.writers.AsciiOutputStreamWriter` but also buffers like a `java.io.BufferedOutputStream`, can result in more efficient writes than using the latter two classes individually
  * does not allocate any objects beyond the initial `byte[]`, the `#write` and `#append` methods do not allocate memory
  * not thread-safe

This project requires Java 11.
