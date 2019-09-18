package com.github.marschall.writers;

import java.io.OutputStream;
import java.io.Writer;

class AsciiOutputStreamWriterCompatibilityTest extends AbstractPrintWriterCompatibilityTest {

  @Override
  Writer newWriter(OutputStream out) {
    return new AsciiOutputStreamWriter(out);
  }

}
