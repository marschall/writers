package com.github.marschall.writers;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

abstract class AbstractPrintWriterCompatibilityTest {

  private ByteArrayOutputStream outputStream;

  private Writer writer;

  @BeforeEach
  void setUp() {
    this.outputStream = new ByteArrayOutputStream();
    this.writer = this.newWriter(this.outputStream);
  }

  abstract Writer newWriter(OutputStream out);

  @Test
  void writeSingleChar() throws IOException {
    this.writer.write('a');
    this.assertContent((byte) 'a');
  }

  @Test
  void writeSingleCharInvalid() throws IOException {
    this.writer.write(255);
    this.assertContent((byte) '?');
  }

  @Test
  void writeSingleCharInvalidNonBmp() throws IOException {
    this.writer.write(0x1f43b);
    this.assertContent((byte) '?');
  }

  @Test
  void writeCharArray() throws IOException {
    this.writer.write(new char[] {'a', 'b', 'c'});
    this.assertContent("abc");
  }

  @Test
  void writeCharArrayNull() throws IOException {
    assertThrows(NullPointerException.class, () -> this.writer.write((char[]) null));
    this.assertContent("");
  }

  @Test
  void writeCharArrayNullOffsetLenght() throws IOException {
    assertThrows(NullPointerException.class, () -> this.writer.write((char[]) null, 2, 2));
    this.assertContent("");
  }

  @Test
  void writeCharArrayInvalid() throws IOException {
    this.writer.write(new char[] {'a', 'b', 'c', '\u20AC'});
    this.assertContent("abc?");
  }

  @Test
  void writeCharArrayOffsetLenght() throws IOException {
    this.writer.write(new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, 3, 5);
    this.assertContent("34567");
  }

  @Test
  void writeString() throws IOException {
    this.writer.write("abc");
    this.assertContent("abc");
  }

  @Test
  void writeStringNull() throws IOException {
    assertThrows(NullPointerException.class, () -> this.writer.write((String) null));
    this.assertContent("");
  }

  @Test
  void writeStringNullOffsetLenght() throws IOException {
    assertThrows(NullPointerException.class, () -> this.writer.write((String) null, 2, 2));
    this.assertContent("");
  }

  @Test
  void writeStringInvalid() throws IOException {
    this.writer.write("abc\u20AC");
    this.assertContent("abc?");
  }

  @Test
  void writeStringOffsetLenght() throws IOException {
    this.writer.write("0123456789", 3, 5);
    this.assertContent("34567");
  }

  @Test
  void appendSingleChar() throws IOException {
    this.writer.append('a');
    this.assertContent((byte) 'a');
  }

  @Test
  void appendSingleCharInvalid() throws IOException {
    this.writer.append((char) 255);
    this.assertContent((byte) '?');
  }

  @Test
  void appendString() throws IOException {
    this.writer.append("abc");
    this.assertContent("abc");
  }

  @Test
  void appendCharSequence() throws IOException {
    this.writer.append(new StringWrapper("abc"));
    this.assertContent("abc");
  }

  @Test
  void appendStringNull() throws IOException {
    this.writer.append(null);
    this.assertContent("null");
  }

  @Test
  void appendStringNullStartEnd() throws IOException {
    this.writer.append(null, 2, 4);
    this.assertContent("ll");
  }

  @Test
  void appendStringInvalid() throws IOException {
    this.writer.append("abc\u20AC");
    this.assertContent("abc?");
  }

  @Test
  void appendCharSequenceInvalid() throws IOException {
    this.writer.append(new StringWrapper("abc\u20AC"));
    this.assertContent("abc?");
  }

  @Test
  void appendStringStartEnd() throws IOException {
    this.writer.append("0123456789", 3, 5);
    this.assertContent("34");
  }

  @Test
  void appendCharSequenceStartEnd() throws IOException {
    this.writer.append(new StringWrapper("0123456789"), 3, 5);
    this.assertContent("34");
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
    assertEquals(expected, new String(this.outputStream.toByteArray(), US_ASCII));
  }

  static final class StringWrapper implements CharSequence {

    private final String s;

    StringWrapper(String s) {
      this.s = s;
    }

    @Override
    public int length() {
      return this.s.length();
    }

    @Override
    public char charAt(int index) {
      return this.s.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
      return this.s.subSequence(start, end);
    }

    @Override
    public String toString() {
      return this.s;
    }

  }

}
