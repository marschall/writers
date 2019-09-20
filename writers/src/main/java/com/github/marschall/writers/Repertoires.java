package com.github.marschall.writers;

final class Repertoires {

  private Repertoires() {
    throw new AssertionError("not instantiable");
  }
  
  static boolean fitsInAscii(int i) {
    return i <= 127;
  }

  static boolean fitsInAscii(char c) {
    return c <= 127;
  }

  static boolean fitsInAscii(char[] cbuf, int offset, int length) {
    if (offset < 0 || length < 0) {
      // will throw later
      return true;
    }
    for (int i = offset; i < length; i++) {
      if (!fitsInAscii(cbuf[i])) {
        return false;
      }
    }
    return true;
  }

  static boolean fitsInAsciiOffsetLength(String s, int offset, int length) {
    if (offset < 0 || length < 0) {
      // will throw later
      return true;
    }
    for (int i = offset; i < length; i++) {
      if (!fitsInAscii(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }
  
  static boolean fitsInAsciiStartEnd(CharSequence csq, int start, int end) {
    if (start < 0 || end > csq.length()) {
      // will throw later
      return true;
    }
    for (int i = start; i < end; i++) {
      if (!fitsInAscii(csq.charAt(i))) {
        return false;
      }
    }
    return true;
  }

}
