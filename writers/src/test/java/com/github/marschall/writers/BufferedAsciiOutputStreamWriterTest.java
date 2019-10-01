package com.github.marschall.writers;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BufferedAsciiOutputStreamWriterTest {

  private ByteArrayOutputStream outputStream;
  private BufferedAsciiOutputStreamWriter writer;

  @BeforeEach
  void setUp() {
    this.outputStream = new ByteArrayOutputStream(128);
    this.writer = new BufferedAsciiOutputStreamWriter(this.outputStream, 4);
  }

  @Test
  void writeStringServeralSegments() throws IOException {
    this.writer.write("1234567");
    this.writer.write("abcdefgh");
    this.writer.write('Z');
    assertContent("1234567" + "abcdefgh" + "Z");
  }

  @Test
  void writeCharArrayServeralSegments() throws IOException {
    this.writer.write("1234567".toCharArray());
    this.writer.write("abcdefgh".toCharArray());
    this.writer.write('Z');
    assertContent("1234567" + "abcdefgh" + "Z");
  }

  @Test
  void appendServeralSegments() throws IOException {
    this.writer.append("1234567");
    this.writer.append("abcdefgh");
    this.writer.append('Z');
    assertContent("1234567" + "abcdefgh" + "Z");
  }

  private void assertContent(String expected) throws IOException {
    this.writer.flush();
    assertEquals(expected, new String(this.outputStream.toByteArray(), US_ASCII));
  }

}
