package joptsimple.internal;

import java.util.Map;
import java.util.TreeMap;

public class AbbreviationMap<V>
{
  private String key;
  private V value;
  private final Map<Character, AbbreviationMap<V>> children = new TreeMap<Character, AbbreviationMap<V>>();
  private int keysBeyond;

  public boolean contains(String aKey)
  {
    return get(aKey) != null;
  }

  public V get(String aKey)
  {
    char[] chars = charsOf(aKey);

    AbbreviationMap<V> child = this;
    for (char each : chars) {
      child = (AbbreviationMap<V>)child.children.get(Character.valueOf(each));
      if (child == null) {
        return null;
      }
    }
    return (V) child.value;
  }

  public void put(String aKey, V newValue)
  {
    if (newValue == null)
      throw new NullPointerException();
    if (aKey.length() == 0) {
      throw new IllegalArgumentException();
    }
    char[] chars = charsOf(aKey);
    add(chars, newValue, 0, chars.length);
  }

  public void putAll(Iterable<String> keys, V newValue)
  {
    for (String each : keys)
      put(each, newValue);
  }

  private boolean add(char[] chars, V newValue, int offset, int length) {
    if (offset == length) {
      this.value = newValue;
      boolean wasAlreadyAKey = this.key != null;
      this.key = new String(chars);
      return !wasAlreadyAKey;
    }

    char nextChar = chars[offset];
    AbbreviationMap<V> child = (AbbreviationMap<V>)this.children.get(Character.valueOf(nextChar));
    if (child == null) {
      child = new AbbreviationMap<V>();
      this.children.put(Character.valueOf(nextChar), child);
    }

    boolean newKeyAdded = child.add(chars, newValue, offset + 1, length);

    if (newKeyAdded) {
      this.keysBeyond += 1;
    }
    if (this.key == null) {
      this.value = (this.keysBeyond > 1 ? null : newValue);
    }
    return newKeyAdded;
  }

  public void remove(String aKey)
  {
    if (aKey.length() == 0) {
      throw new IllegalArgumentException();
    }
    char[] keyChars = charsOf(aKey);
    remove(keyChars, 0, keyChars.length);
  }

  private boolean remove(char[] aKey, int offset, int length) {
    if (offset == length) {
      return removeAtEndOfKey();
    }
    char nextChar = aKey[offset];
    AbbreviationMap<?> child = (AbbreviationMap<?>)this.children.get(Character.valueOf(nextChar));
    if ((child == null) || (!child.remove(aKey, offset + 1, length))) {
      return false;
    }
    this.keysBeyond -= 1;
    if (child.keysBeyond == 0)
      this.children.remove(Character.valueOf(nextChar));
    if ((this.keysBeyond == 1) && (this.key == null)) {
      setValueToThatOfOnlyChild();
    }
    return true;
  }

  @SuppressWarnings("unchecked")
private void setValueToThatOfOnlyChild() {
    @SuppressWarnings("rawtypes")
	Map.Entry entry = (Map.Entry)this.children.entrySet().iterator().next();
    AbbreviationMap<?> onlyChild = (AbbreviationMap<?>)entry.getValue();
    this.value = (V) onlyChild.value;
  }

  private boolean removeAtEndOfKey() {
    if (this.key == null) {
      return false;
    }
    this.key = null;
    if (this.keysBeyond == 1)
      setValueToThatOfOnlyChild();
    else {
      this.value = null;
    }
    return true;
  }

  public Map<String, V> toJavaUtilMap()
  {
    Map<String, V> mappings = new TreeMap<String, V>();
    addToMappings(mappings);
    return mappings;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
private void addToMappings(Map<String, V> mappings) {
    if (this.key != null) {
      mappings.put(this.key, this.value);
    }
    for (AbbreviationMap<?> each : this.children.values())
      each.addToMappings((Map) mappings);
  }

  private static char[] charsOf(String aKey) {
    char[] chars = new char[aKey.length()];
    aKey.getChars(0, aKey.length(), chars, 0);
    return chars;
  }
}

/* Location:           C:\Users\Romain\Downloads\Minecraft.jar
 * Qualified Name:     joptsimple.internal.AbbreviationMap
 * JD-Core Version:    0.6.2
 */