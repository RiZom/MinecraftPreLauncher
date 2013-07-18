package net.minecraft.bootstrap;

@SuppressWarnings("serial")
public class FatalBootstrapError extends RuntimeException
{
public FatalBootstrapError(String reason)
  {
    super(reason);
  }
}