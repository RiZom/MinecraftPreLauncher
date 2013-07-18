package joptsimple;

import java.util.Collection;

class OptionMissingRequiredArgumentException extends OptionException
{
  private static final long serialVersionUID = -1L;

  OptionMissingRequiredArgumentException(Collection<String> options)
  {
    super(options);
  }

  public String getMessage()
  {
    return "Option " + multipleOptionMessage() + " requires an argument";
  }
}