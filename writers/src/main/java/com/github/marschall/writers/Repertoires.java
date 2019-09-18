package com.github.marschall.writers;

final class Repertoires {

  private Repertoires() {
    throw new AssertionError("not instantiable");
  }

  static boolean fitsInAscii(char c) {
    return c <= 127;
  }

  static boolean fitsInAscii(char[] cbuf, int off, int len) {
    for (int i = off; i < len; i++) {
      if (!fitsInAscii(cbuf[i])) {
        return false;
      }
    }
    return true;
  }

  static boolean fitsInAscii(CharSequence csq, int off, int len) {
    for (int i = off; i < len; i++) {
      if (!fitsInAscii(csq.charAt(i))) {
        return false;
      }
    }
    return true;
  }

}
