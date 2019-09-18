package com.github.marschall.writers.benchmark;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.github.marschall.writers.AsciiOutputStreamWriter;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class WriterBenchmark {
  
  private static final int ITERATIONS = 1000;

  private PrintWriter printWriter;

  private Writer outputStreamWriter;

  private Writer asciiOutputStreamWriter;

  private ByteArrayOutputStream printWriterStream;

  private ByteArrayOutputStream outputStreamWriterStream;

  private ByteArrayOutputStream asciiOutputStreamWriterStream;

  @Setup
  public void setup() {
    this.printWriterStream = new ByteArrayOutputStream(4);
    this.outputStreamWriterStream = new ByteArrayOutputStream(4);
    this.asciiOutputStreamWriterStream = new ByteArrayOutputStream(4);
    this.printWriter = new PrintWriter(new BufferedOutputStream(printWriterStream), false, StandardCharsets.US_ASCII);
    this.outputStreamWriter = new OutputStreamWriter(new BufferedOutputStream(outputStreamWriterStream), StandardCharsets.US_ASCII);
    this.asciiOutputStreamWriter = new AsciiOutputStreamWriter(new BufferedOutputStream(asciiOutputStreamWriterStream));
  }

  @Benchmark
  public PrintWriter writeSingleCharPrintWriter() {
    for (int i = 0; i < ITERATIONS; i++) {
      this.printWriter.write(' ');
    }
    this.printWriter.flush();
    this.printWriterStream.reset();
    return this.printWriter;
  }

  @Benchmark
  public Writer writeSingleCharOutputStreamWriter() throws IOException {
    for (int i = 0; i < ITERATIONS; i++) {
      this.outputStreamWriter.write(' ');
    }
    this.outputStreamWriter.flush();
    this.outputStreamWriterStream.reset();
    return this.outputStreamWriter;
  }

  @Benchmark
  public Writer writeSingleCharAsciiOutputStreamWriter() throws IOException {
    for (int i = 0; i < ITERATIONS; i++) {
      this.asciiOutputStreamWriter.write(' ');
    }
    this.asciiOutputStreamWriter.flush();
    this.asciiOutputStreamWriterStream.reset();
    return this.asciiOutputStreamWriter;
  }
  
  @Benchmark
  public PrintWriter writeStringPrintWriter() {
    for (int i = 0; i < ITERATIONS; i++) {
      this.printWriter.write("abcd123");
    }
    this.printWriter.flush();
    this.printWriterStream.reset();
    return this.printWriter;
  }
  
  @Benchmark
  public Writer writeStringOutputStreamWriter() throws IOException {
    for (int i = 0; i < ITERATIONS; i++) {
      this.outputStreamWriter.write("abcd123");
    }
    this.outputStreamWriter.flush();
    this.outputStreamWriterStream.reset();
    return this.outputStreamWriter;
  }
  
  @Benchmark
  public Writer writeStringAsciiOutputStreamWriter() throws IOException {
    for (int i = 0; i < ITERATIONS; i++) {
      this.asciiOutputStreamWriter.write("abcd123");
    }
    this.asciiOutputStreamWriter.flush();
    this.asciiOutputStreamWriterStream.reset();
    return this.asciiOutputStreamWriter;
  }

}
