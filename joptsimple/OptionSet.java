package joptsimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import joptsimple.internal.Objects;

public class OptionSet
{
  private final List<OptionSpec<?>> detectedSpecs;
  private final Map<String, AbstractOptionSpec<?>> detectedOptions;
  private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments;
  private final Map<String, List<?>> defaultValues;

  OptionSet(Map<String, List<?>> defaults)
  {
    this.detectedSpecs = new ArrayList();
    this.detectedOptions = new HashMap();
    this.optionsToArguments = new IdentityHashMap();
    this.defaultValues = new HashMap(defaults);
  }

  public boolean hasOptions()
  {
    return !this.detectedOptions.isEmpty();
  }

  public boolean has(String option)
  {
    return this.detectedOptions.containsKey(option);
  }

  public boolean has(OptionSpec<?> option)
  {
    return this.optionsToArguments.containsKey(option);
  }

  public boolean hasArgument(String option)
  {
    AbstractOptionSpec spec = (AbstractOptionSpec)this.detectedOptions.get(option);
    return (spec != null) && (hasArgument(spec));
  }

  public boolean hasArgument(OptionSpec<?> option)
  {
    Objects.ensureNotNull(option);

    List values = (List)this.optionsToArguments.get(option);
    return (values != null) && (!values.isEmpty());
  }

  public Object valueOf(String option)
  {
    Objects.ensureNotNull(option);

    AbstractOptionSpec spec = (AbstractOptionSpec)this.detectedOptions.get(option);
    if (spec == null) {
      List defaults = defaultValuesFor(option);
      return defaults.isEmpty() ? null : defaults.get(0);
    }

    return valueOf(spec);
  }

  public <V> V valueOf(OptionSpec<V> option)
  {
    Objects.ensureNotNull(option);

    List values = valuesOf(option);
    switch (values.size()) {
    case 0:
      return null;
    case 1:
      return (V) values.get(0);
    }
    throw new MultipleArgumentsForOptionException(option.options());
  }

  public List<?> valuesOf(String option)
  {
    Objects.ensureNotNull(option);

    AbstractOptionSpec spec = (AbstractOptionSpec)this.detectedOptions.get(option);
    return spec == null ? defaultValuesFor(option) : valuesOf(spec);
  }

  public <V> List<V> valuesOf(OptionSpec<V> option)
  {
    Objects.ensureNotNull(option);

    List<String> values = (List)this.optionsToArguments.get(option);
    if ((values == null) || (values.isEmpty())) {
      return defaultValueFor(option);
    }
    AbstractOptionSpec spec = (AbstractOptionSpec)option;
    List convertedValues = new ArrayList();
    for (String each : values) {
      convertedValues.add(spec.convert(each));
    }
    return Collections.unmodifiableList(convertedValues);
  }

  public List<OptionSpec<?>> specs()
  {
    List specs = this.detectedSpecs;
    specs.remove(this.detectedOptions.get("[arguments]"));

    return Collections.unmodifiableList(specs);
  }

  public List<?> nonOptionArguments()
  {
    return Collections.unmodifiableList(valuesOf((OptionSpec)this.detectedOptions.get("[arguments]")));
  }

  void add(AbstractOptionSpec<?> spec) {
    addWithArgument(spec, null);
  }

  void addWithArgument(AbstractOptionSpec<?> spec, String argument) {
    this.detectedSpecs.add(spec);

    for (String each : spec.options()) {
      this.detectedOptions.put(each, spec);
    }
    List optionArguments = (List)this.optionsToArguments.get(spec);

    if (optionArguments == null) {
      optionArguments = new ArrayList();
      this.optionsToArguments.put(spec, optionArguments);
    }

    if (argument != null)
      optionArguments.add(argument);
  }

  public boolean equals(Object that)
  {
    if (this == that) {
      return true;
    }
    if ((that == null) || (!getClass().equals(that.getClass()))) {
      return false;
    }
    OptionSet other = (OptionSet)that;
    Map thisOptionsToArguments = new HashMap(this.optionsToArguments);

    Map otherOptionsToArguments = new HashMap(other.optionsToArguments);

    return (this.detectedOptions.equals(other.detectedOptions)) && (thisOptionsToArguments.equals(otherOptionsToArguments));
  }

  public int hashCode()
  {
    Map thisOptionsToArguments = new HashMap(this.optionsToArguments);

    return this.detectedOptions.hashCode() ^ thisOptionsToArguments.hashCode();
  }

  @SuppressWarnings("unchecked")
private <V> List<V> defaultValuesFor(String option) {
    if (this.defaultValues.containsKey(option)) {
      return (List)this.defaultValues.get(option);
    }
    return Collections.emptyList();
  }

  private <V> List<V> defaultValueFor(OptionSpec<V> option) {
    return defaultValuesFor((String)option.options().iterator().next());
  }
}