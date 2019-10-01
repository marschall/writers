package com.github.marschall.writers;

import java.io.OutputStream;
import java.io.Writer;

public class BufferedAsciiOutputStreamWriterCompatibilityTest extends AbstractPrintWriterCompatibilityTest {

  @Override
  Writer newWriter(OutputStream out) {
    return new BufferedAsciiOutputStreamWriter(out);
  }

}
