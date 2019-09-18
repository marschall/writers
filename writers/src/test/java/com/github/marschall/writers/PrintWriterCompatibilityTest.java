package com.github.marschall.writers;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

class PrintWriterCompatibilityTest extends AbstractPrintWriterCompatibilityTest {
  
  @Override
  Writer newWriter(OutputStream out) {
    return new PrintWriter(out, false, StandardCharsets.US_ASCII);
  }

}
