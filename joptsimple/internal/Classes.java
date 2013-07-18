package joptsimple.internal;

import java.util.HashMap;
import java.util.Map;

public final class Classes
{
  private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap<Class<?>, Class<?>>(13);

  private Classes()
  {
    throw new UnsupportedOperationException();
  }

  public static String shortNameOf(String className)
  {
    return className.substring(className.lastIndexOf('.') + 1);
  }

  @SuppressWarnings("unchecked")
public static <T> Class<T> wrapperOf(Class<T> clazz)
  {
    return (Class<T>) (clazz.isPrimitive() ? (Class<?>)WRAPPERS.get(clazz) : clazz);
  }

  static
  {
    WRAPPERS.put(Boolean.TYPE, Boolean.class);
    WRAPPERS.put(Byte.TYPE, Byte.class);
    WRAPPERS.put(Character.TYPE, Character.class);
    WRAPPERS.put(Double.TYPE, Double.class);
    WRAPPERS.put(Float.TYPE, Float.class);
    WRAPPERS.put(Integer.TYPE, Integer.class);
    WRAPPERS.put(Long.TYPE, Long.class);
    WRAPPERS.put(Short.TYPE, Short.class);
    WRAPPERS.put(Void.TYPE, Void.class);
  }
}