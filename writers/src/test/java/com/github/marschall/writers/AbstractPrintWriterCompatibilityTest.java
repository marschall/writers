package com.github.marschall.writers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
  void writeCharArray() throws IOException {
    this.writer.write(new char[] {'a', 'b', 'c'});
    assertContent("abc");
  }

  @Test
  void writeCharArrayNull() throws IOException {
    assertThrows(NullPointerException.class, () -> this.writer.write((String) null));
    assertContent("");
  }

  @Test
  void writeCharArrayNullOffsetLenght() throws IOException {
    assertThrows(NullPointerException.class, () -> this.writer.write((String) null, 2, 2));
    assertContent("");
  }

  @Test
  void writeCharArrayInvalid() throws IOException {
    this.writer.write(new char[] {'a', 'b', 'c', '\u20AC'});
    assertContent("abc?");
  }

  @Test
  void writeCharArrayOffsetLenght() throws IOException {
    this.writer.write(new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, 3, 5);
    assertContent("34567");
  }

  @Test
  void writeString() throws IOException {
    this.writer.write("abc");
    assertContent("abc");
  }

  @Test
  void writeStringNull() throws IOException {
    assertThrows(NullPointerException.class, () -> this.writer.write((String) null));
    assertContent("");
  }

  @Test
  void writeStringNullOffsetLenght() throws IOException {
    assertThrows(NullPointerException.class, () -> this.writer.write((String) null, 2, 2));
    assertContent("");
  }

  @Test
  void writeStringInvalid() throws IOException {
    this.writer.write("abc\u20AC");
    assertContent("abc?");
  }

  @Test
  void writeStringOffsetLenght() throws IOException {
    this.writer.write("0123456789", 3, 5);
    assertContent("34567");
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
  void appendStringNullStartEnd() throws IOException {
    this.writer.append(null, 2, 4);
    assertContent("ll");
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

  @Test
  @Disabled("inconsisten behavior")
  void close() throws IOException {
    this.writer.close();
    assertThrows(IOException.class, () -> this.writer.write("x"));
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
