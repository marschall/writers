package com.github.marschall.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Objects;

final class BufferedAsciiOutputStreamWriter extends Writer {

  private final OutputStream out;

  private final byte[] buffer;

  private int position;

  private boolean closed;

  /**
   * Constructs a new {@link BufferedAsciiOutputStreamWriter}
   * 
   * @param out the output stream to delegate to, not {@code null}
   * @param the buffer size in bytes, must be positive
   * @throws NullPointerException when {@code out} is {@code null}
   * @throws bufferSize if {@code bufferSize} negative or 0
   */
  public BufferedAsciiOutputStreamWriter(OutputStream out, int bufferSize) {
    Objects.requireNonNull(out, "out");
    if (bufferSize <= 0) {
      throw new IllegalArgumentException("buffer size must be positive");
    }
    this.out = out;
    this.buffer = new byte[bufferSize];
    this.position = 0;
    this.closed = false;
  }

  /**
   * Constructs a new {@link BufferedAsciiOutputStreamWriter} with a
   * default buffer size of 8192.
   * 
   * @param out the output stream to delegate to, not {@code null}
   * @throws NullPointerException when {@code out} is {@code null}
   */
  public BufferedAsciiOutputStreamWriter(OutputStream out) {
    this(out, 8192);
  }

  private void closedCheck() throws IOException {
    if (this.closed) {
      throw new IOException("closed writer");
    }
  }

  private void flushBufferIfNotEmpty() throws IOException {
    if (this.position > 0) {
      this.flushBuffer();
    }
  }

  private boolean ensureCapacity(int capacity) throws IOException {
    if (capacity < 0) {
      // will throw later
      return true;
    }
    if (capacity > this.buffer.length - this.position) {
      this.flushBuffer();
    }
    return capacity <= this.buffer.length;
  }

  private void flushBuffer() throws IOException {
    this.out.write(this.buffer, 0, this.position);
    this.position = 0;
  }

  @Override
  public void write(int c) throws IOException {
    this.closedCheck();
    this.ensureCapacity(1);
    if (Repertoires.fitsInAscii(c)) {
      this.writeAscii((char) c);
    } else {
      this.writeNonAscii();
    }
  }

  @Override
  public void write(char[] cbuf, int offset, int length) throws IOException {
    this.closedCheck();
    if (this.ensureCapacity(length)) {
      if (Repertoires.fitsInAscii(cbuf, offset, length)) {
        this.writeAsciiOffsetLength(cbuf, offset, length);
      } else {
        this.writeNonAsciiOffsetLength(cbuf, offset, length);
      }
    } else {
      this.writeSegmented(cbuf, offset, length);
    }
  }

  @Override
  public void write(String str, int offset, int length) throws IOException {
    this.closedCheck();
    if (this.ensureCapacity(length)) {
      if (Repertoires.fitsInAsciiOffsetLength(str, offset, length)) {
        this.writeAsciiOffsetLength(str, offset, length);
      } else {
        this.writeNonAsciiOffsetLength(str, offset, length);
      }
    } else {
      this.writeSegmentedOffsetLenth(str, offset, length);
    }
  }

  @Override
  public Writer append(CharSequence csq) throws IOException {
    return this.append(csq, 0, csq.length());
  }

  @Override
  public Writer append(CharSequence csq, int start, int end) throws IOException {
    CharSequence charSequence = csq != null ? csq : "null";
    this.closedCheck();
    if (this.ensureCapacity(end - start)) {
      if (Repertoires.fitsInAsciiStartEnd(charSequence, start, end)) {
        this.writeAsciiStartEnd(charSequence, start, end);
      } else {
        this.writeNonAsciiStartEnd(charSequence, start, end);
      }
    } else {
      this.writeSegmentedStartEnd(charSequence, start, end);
    }
    return this;
  }

  @Override
  public Writer append(char c) throws IOException {
    this.closedCheck();
    this.ensureCapacity(1);
    if (Repertoires.fitsInAscii(c)) {
      this.writeAscii((char) c);
    } else {
      this.writeNonAscii();
    }
    return this;
  }

  private void writeSegmentedStartEnd(CharSequence csq, int initialStart, int end) throws IOException {
    int currentStart = initialStart;
    while (currentStart < end) {
      int length = Math.min(this.buffer.length, end - currentStart);
      this.append(csq, currentStart, currentStart + length);
      currentStart += length;
    }
  }

  private void writeSegmentedOffsetLenth(String s, int offset, int totalLength) throws IOException {
    int written = 0;
    while (written < totalLength) {
      int length = Math.min(this.buffer.length, written - totalLength);
      this.write(s, offset + written, length);
      written += length;
    }
  }

  private void writeSegmented(char[] cbuf, int offset, int totalLength) throws IOException {
    int written = 0;
    while (written < totalLength) {
      int length = Math.min(this.buffer.length, written - totalLength);
      this.write(cbuf, offset + written, length);
      written += length;
    }
  }

  private Writer writeNonAscii() throws IOException {
    this.buffer[this.position++] = (byte) ('?' & 0xff);
    return this;
  }

  private void writeNonAsciiOffsetLength(char[] cbuf, int offset, int length) {
    // TODO Auto-generated method stub
    
  }

  private void writeNonAsciiOffsetLength(String str, int offset, int length) {
    // TODO Auto-generated method stub
    
  }

  private Writer writeAscii(char c) throws IOException {
    this.buffer[this.position++] = (byte) (c & 0xff);
    return this;
  }

  private void writeAsciiStartEnd(CharSequence csq, int start, int end) throws IOException {
    int from = Objects.checkFromToIndex(start, end, csq.length());
    for (int i = from; i < end; i++) {
      this.buffer[this.position++] = (byte) (csq.charAt(i) & 0xff);
    }
  }

  private void writeNonAsciiStartEnd(CharSequence csq, int start, int end) {
    int from = Objects.checkFromToIndex(start, end, csq.length());
    for (int i = from; i < end; i++) {
      char c = csq.charAt(i);
      if (!Repertoires.fitsInAscii(c)) {
        c = '?';
      }
      this.buffer[this.position++] = (byte) (c & 0xff);
    }
  }

  private Writer writeAsciiOffsetLength(String s, int offset, int length) throws IOException {
    int from = Objects.checkFromIndexSize(offset, length, s.length());
    for (int i = from; i < offset + length; i++) {
      this.buffer[this.position++] = (byte) (s.charAt(i) & 0xff);
    }
    return this;
  }

  private void writeAsciiOffsetLength(char[] cbuf, int offset, int length) throws IOException {
    int from = Objects.checkFromIndexSize(offset, length, cbuf.length);
    for (int i = from; i < offset + length; i++) {
      this.buffer[this.position++] = (byte) (cbuf[i] & 0xff);
    }
  }

  @Override
  public void flush() throws IOException {
    this.closedCheck();
    this.doFlush();
  }

  private void doFlush() throws IOException {
    this.flushBufferIfNotEmpty();
    this.out.flush();
  }

  @Override
  public void close() throws IOException {
    try {
      if (!this.closed) {
        this.doFlush();
      }
    } finally {
      this.closed = true;
      this.out.close();
    }
  }

}
