package joptsimple;

import java.util.NoSuchElementException;

class OptionSpecTokenizer
{
  @SuppressWarnings("unused")
private static final char POSIXLY_CORRECT_MARKER = '+';
  @SuppressWarnings("unused")
private static final char HELP_MARKER = '*';
  private String specification;
  private int index;

  OptionSpecTokenizer(String specification)
  {
    if (specification == null) {
      throw new NullPointerException("null option specification");
    }
    this.specification = specification;
  }

  boolean hasMore() {
    return this.index < this.specification.length();
  }

  AbstractOptionSpec<?> next() {
    if (!hasMore()) {
      throw new NoSuchElementException();
    }

    String optionCandidate = String.valueOf(this.specification.charAt(this.index));
    this.index += 1;

    if ("W".equals(optionCandidate)) {
      AbstractOptionSpec<?> spec = handleReservedForExtensionsToken();

      if (spec != null) {
        return spec;
      }
    }
    ParserRules.ensureLegalOption(optionCandidate);
    AbstractOptionSpec<?> spec;
    if (hasMore()) {
      boolean forHelp = false;
      if (this.specification.charAt(this.index) == '*') {
        forHelp = true;
        this.index += 1;
      }
      spec = (hasMore()) && (this.specification.charAt(this.index) == ':') ? handleArgumentAcceptingOption(optionCandidate) : new NoArgumentOptionSpec(optionCandidate);

      if (forHelp)
        spec.forHelp();
    } else {
      spec = new NoArgumentOptionSpec(optionCandidate);
    }
    return spec;
  }

  void configure(OptionParser parser) {
    adjustForPosixlyCorrect(parser);

    while (hasMore())
       parser.recognize(next());
  }

  private void adjustForPosixlyCorrect(OptionParser parser) {
    if ('+' == this.specification.charAt(0)) {
      parser.posixlyCorrect(true);
      this.specification = this.specification.substring(1);
    }
  }

  private AbstractOptionSpec<?> handleReservedForExtensionsToken() {
    if (!hasMore()) {
      return new NoArgumentOptionSpec("W");
    }
    if (this.specification.charAt(this.index) == ';') {
      this.index += 1;
      return new AlternativeLongOptionSpec();
    }

    return null;
  }

  private AbstractOptionSpec<?> handleArgumentAcceptingOption(String candidate) {
    this.index += 1;

    if ((hasMore()) && (this.specification.charAt(this.index) == ':')) {
      this.index += 1;
      return new OptionalArgumentOptionSpec<Object>(candidate);
    }

    return new RequiredArgumentOptionSpec<Object>(candidate);
  }
}