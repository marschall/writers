package com.github.marschall.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Objects;

/**
 * A writer that encodes to ASCII.
 * <p>
 * For non-ASCII characters {@code '?'} will be written instead just
 * like {@link OutputStreamWriter} does.
 * <p>
 *
 * @implNote This class is thread-safe.
 * @implNote This writer does not allocate any objects
 *           or call methods that allocate objects.
 */
public final class AsciiOutputStreamWriter extends Writer {

  private final OutputStream out;

  /**
   * Constructs a new {@link AsciiOutputStreamWriter}
   *
   * @param out the output stream to delegate to, not {@code null}
   * @throws NullPointerException when {@code out} is {@code null}
   */
  public AsciiOutputStreamWriter(OutputStream out) {
    Objects.requireNonNull(out, "out");
    this.out = out;
  }

  @Override
  public void write(int c) throws IOException {
    if (c > Character.MAX_VALUE) {
      this.writeAscii('?');
    } else {
      this.writeAscii((char) c);
    }
  }

  @Override
  public void write(char[] cbuf) throws IOException {
    this.writeAsciiOffsetLength(cbuf, 0, cbuf.length);
  }

  @Override
  public void write(char[] cbuf, int offset, int len) throws IOException {
    this.writeAsciiOffsetLength(cbuf, offset, len);
  }

  @Override
  public void write(String s) throws IOException {
    this.writeAsciiOffsetLength(s, 0, s.length());
  }

  @Override
  public void write(String s, int offset, int len) throws IOException {
    this.writeAsciiOffsetLength(s, offset, len);
  }

  @Override
  public Writer append(CharSequence csq) throws IOException {
    if (csq == null) {
      return this.writeAciiNull();
    } else {
      return this.writeAsciiStartEnd(csq, 0, csq.length());
    }
  }

  @Override
  public Writer append(CharSequence csq, int start, int end) throws IOException {
    if (csq == null) {
      return this.writeAsciiStartEnd("null", start, end);
    } else {
      return this.writeAsciiStartEnd(csq, start, end);
    }
  }

  @Override
  public Writer append(char c) throws IOException {
    return this.writeAscii(c);
  }

  private Writer writeAciiNull() throws IOException {
    return this.writeAsciiStartEnd("null", 0, 4);
  }

  private Writer writeAscii(char c) throws IOException {
    synchronized (this.lock) {
      if (Repertoires.fitsInAscii(c)) {
        this.out.write(c);
      } else {
        this.out.write('?');
      }
    }
    return this;
  }

  private Writer writeAsciiStartEnd(CharSequence csq, int start, int end) throws IOException {
    synchronized (this.lock) {
      int from = Objects.checkFromToIndex(start, end, csq.length());
      for (int i = from; i < end; i++) {
        char c = csq.charAt(i);
        if (Repertoires.fitsInAscii(c)) {
          this.out.write(c);
        } else {
          this.out.write('?');
        }
      }
    }
    return this;
  }

  private Writer writeAsciiOffsetLength(String s, int offset, int length) throws IOException {
    synchronized (this.lock) {
      int from = Objects.checkFromIndexSize(offset, length, s.length());
      for (int i = from; i < (offset + length); i++) {
        char c = s.charAt(i);
        if (Repertoires.fitsInAscii(c)) {
          this.out.write(c);
        } else {
          this.out.write('?');
        }
      }
    }
    return this;
  }

  private void writeAsciiOffsetLength(char[] cbuf, int offset, int length) throws IOException {
    synchronized (this.lock) {
      int from = Objects.checkFromIndexSize(offset, length, cbuf.length);
      for (int i = from; i < (offset + length); i++) {
        char c = cbuf[i];
        if (Repertoires.fitsInAscii(c)) {
          this.out.write(c);
        } else {
          this.out.write('?');
        }
      }
    }
  }

  @Override
  public void flush() throws IOException {
    this.out.flush();
  }

  @Override
  public void close() throws IOException {
    this.out.close();
  }

}
