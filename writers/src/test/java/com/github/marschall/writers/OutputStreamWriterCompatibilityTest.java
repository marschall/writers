package com.github.marschall.writers;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

class OutputStreamWriterCompatibilityTest extends AbstractPrintWriterCompatibilityTest {

  @Override
  Writer newWriter(OutputStream out) {
    return new OutputStreamWriter(out, StandardCharsets.US_ASCII);
  }

}
