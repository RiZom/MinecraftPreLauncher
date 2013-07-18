package joptsimple;

import java.util.Collections;

class AlternativeLongOptionSpec extends ArgumentAcceptingOptionSpec<String>
{
  AlternativeLongOptionSpec()
  {
    super(Collections.singletonList("W"), true, "Alternative form of long options");

    describedAs("opt=value");
  }

  protected void detectOptionArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions)
  {
    if (!arguments.hasMore()) {
      throw new OptionMissingRequiredArgumentException(options());
    }
    arguments.treatNextAsLongOption();
  }
}