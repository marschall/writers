package com.github.marschall.writers.benchmark;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MethodBenchmark {

  @Benchmark
  public boolean fitsInAsciiMask() throws IOException {
    return fitsInAsciiMask("abcd123");
  }

  @Benchmark
  public boolean fitsInAsciiIf() throws IOException {
    return fitsInAsciiIf("abcd123");
  }

  static boolean fitsInAsciiMask(String s) {
    int mask = 0;
    for (int i = 0; i < s.length(); i++) {
      mask |= s.charAt(i) & 0b1111111110000000;
    }
    return mask != 0;
  }

  static boolean fitsInAsciiIf(String s) {
    for (int i = 0; i < s.length(); i++) {
      if (!fitsInAscii(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }



  static boolean fitsInAscii(char c) {
    return c <= 127;
  }

}
