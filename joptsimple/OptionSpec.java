package joptsimple;

import java.util.Collection;
import java.util.List;

public abstract interface OptionSpec<V>
{
  public abstract List<V> values(OptionSet paramOptionSet);

  public abstract V value(OptionSet paramOptionSet);

  public abstract Collection<String> options();

  public abstract boolean isForHelp();
}