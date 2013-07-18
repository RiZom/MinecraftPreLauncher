package joptsimple;

import java.util.Collection;

class MissingRequiredOptionException extends OptionException
{
  private static final long serialVersionUID = -1L;

  protected MissingRequiredOptionException(Collection<String> options)
  {
    super(options);
  }

  public String getMessage()
  {
    return "Missing required option(s) " + multipleOptionMessage();
  }
}