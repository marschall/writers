package com.github.marschall.writers.benchmark;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {

  public static void main(String[] args) throws RunnerException {
    String fileName = "writers-result.txt";
    Options options = new OptionsBuilder()
        .include(".*WriterBenchmark.*")
//        .include(".*MethodBenchmark.*")
        .warmupIterations(5)
        .measurementIterations(5)
        .resultFormat(ResultFormatType.TEXT)
        .result(fileName)
        .build();
    new Runner(options).run();
  }

}
