package joptsimple;

import java.util.Collection;
import java.util.Collections;

class UnconfiguredOptionException extends OptionException
{
  private static final long serialVersionUID = -1L;

  UnconfiguredOptionException(String option)
  {
    this(Collections.singletonList(option));
  }

  UnconfiguredOptionException(Collection<String> options) {
    super(options);
  }

  public String getMessage()
  {
    return "Option " + multipleOptionMessage() + " has not been configured on this parser";
  }
}