package joptsimple;

public abstract interface ValueConverter<V>
{
  public abstract V convert(String paramString);

  public abstract Class<V> valueType();

  public abstract String valuePattern();
}