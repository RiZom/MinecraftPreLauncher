package joptsimple;

import java.util.Collection;

class OptionArgumentConversionException extends OptionException
{
  private static final long serialVersionUID = -1L;
  private final String argument;
  private final Class<?> valueType;

  OptionArgumentConversionException(Collection<String> options, String argument, Class<?> valueType, Throwable cause)
  {
    super(options, cause);

    this.argument = argument;
    this.valueType = valueType;
  }

  public String getMessage()
  {
    return "Cannot convert argument '" + this.argument + "' of option " + multipleOptionMessage() + " to " + this.valueType;
  }
}