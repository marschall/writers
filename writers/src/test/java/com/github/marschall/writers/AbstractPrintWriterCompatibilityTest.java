package com.github.marschall.writers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class AbstractPrintWriterCompatibilityTest {

  private ByteArrayOutputStream outputStream;

  private Writer writer;

  @BeforeEach
  void setUp() {
    this.outputStream = new ByteArrayOutputStream();
    this.writer = newWriter(outputStream);
  }

  abstract Writer newWriter(OutputStream out);

  @Test
  void wirteSingleChar() throws IOException {
    this.writer.write('a');
    assertContent((byte) 'a');
  }

  @Test
  void wirteSingleCharInvalid() throws IOException {
    this.writer.write(255);
    assertContent((byte) '?');
  }

  @Test
  void wirteSingleCharInvalidNonBmp() throws IOException {
    this.writer.write(0x1f43b);
    assertContent((byte) '?');
  }

  @Test
  void appendSingleChar() throws IOException {
    this.writer.append('a');
    assertContent((byte) 'a');
  }

  @Test
  void appendSingleCharInvalid() throws IOException {
    this.writer.append((char) 255);
    assertContent((byte) '?');
  }

  @Test
  void appendString() throws IOException {
    this.writer.append("abc");
    assertContent("abc");
  }

  @Test
  void appendStringNull() throws IOException {
    this.writer.append(null);
    assertContent("null");
  }

  @Test
  void appendStringInvalid() throws IOException {
    this.writer.append("abc\u20AC");
    assertContent("abc?");
  }

  @Test
  void appendStringStartEnd() throws IOException {
    this.writer.append("0123456789", 3, 5);
    assertContent("34");
  }

  private void assertContent(byte... expected) throws IOException {
    this.writer.flush();
    assertArrayEquals(expected, this.outputStream.toByteArray());
  }

  private void assertContent(String expected) throws IOException {
    this.writer.flush();
    assertEquals(expected, new String(this.outputStream.toByteArray(), StandardCharsets.US_ASCII));
  }

}
