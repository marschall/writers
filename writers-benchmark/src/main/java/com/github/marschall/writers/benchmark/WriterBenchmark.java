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

  private PrintWriter printWriter;

  private Writer outputStreamWriter;

  private Writer asciiOutputStreamWriter;

  @Setup
  public void setup() {
    this.printWriter = new PrintWriter(new BufferedOutputStream(new ByteArrayOutputStream(4)), false, StandardCharsets.US_ASCII);
    this.outputStreamWriter = new OutputStreamWriter(new BufferedOutputStream(new ByteArrayOutputStream(4)), StandardCharsets.US_ASCII);
    this.asciiOutputStreamWriter = new AsciiOutputStreamWriter(new BufferedOutputStream(new ByteArrayOutputStream(4)));
  }

  @Benchmark
  public PrintWriter writeSingleCharPrintWriter() {
    this.printWriter.write(' ');
    return this.printWriter;
  }

  @Benchmark
  public Writer writeSingleCharOutputStreamWriter() throws IOException {
    this.outputStreamWriter.write(' ');
    return this.outputStreamWriter;
  }

  @Benchmark
  public Writer writeSingleCharAsciiOutputStreamWriter() throws IOException {
    this.asciiOutputStreamWriter.write("abcd123");
    return this.asciiOutputStreamWriter;
  }
  
  @Benchmark
  public PrintWriter writeStringPrintWriter() {
    this.printWriter.write("abcd123");
    return this.printWriter;
  }
  
  @Benchmark
  public Writer writeStringOutputStreamWriter() throws IOException {
    this.outputStreamWriter.write("abcd123");
    return this.outputStreamWriter;
  }
  
  @Benchmark
  public Writer writeStringAsciiOutputStreamWriter() throws IOException {
    this.asciiOutputStreamWriter.write("abcd123");
    return this.asciiOutputStreamWriter;
  }

}
