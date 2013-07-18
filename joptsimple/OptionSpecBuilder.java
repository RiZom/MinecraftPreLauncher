package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OptionSpecBuilder extends NoArgumentOptionSpec
{
  private final OptionParser parser;

  OptionSpecBuilder(OptionParser parser, Collection<String> options, String description)
  {
    super(options, description);

    this.parser = parser;
    attachToParser();
  }

  private void attachToParser() {
    this.parser.recognize(this);
  }

  public ArgumentAcceptingOptionSpec<String> withRequiredArg()
  {
    ArgumentAcceptingOptionSpec<String> newSpec = new RequiredArgumentOptionSpec<String>(options(), description());

    this.parser.recognize(newSpec);

    return newSpec;
  }

  public ArgumentAcceptingOptionSpec<String> withOptionalArg()
  {
    ArgumentAcceptingOptionSpec<String> newSpec = new OptionalArgumentOptionSpec<String>(options(), description());

    this.parser.recognize(newSpec);

    return newSpec;
  }

  public OptionSpecBuilder requiredIf(String dependent, String[] otherDependents)
  {
    List<String> dependents = new ArrayList<String>();
    dependents.add(dependent);
    Collections.addAll(dependents, otherDependents);

    for (String each : dependents) {
      if (!this.parser.isRecognized(each)) {
        throw new UnconfiguredOptionException(each);
      }
      this.parser.requiredIf(options(), dependent);
    }

    return this;
  }

  public OptionSpecBuilder requiredIf(OptionSpec<?> dependent, OptionSpec<?>[] otherDependents)
  {
    this.parser.requiredIf(options(), dependent);
    for (OptionSpec<?> each : otherDependents) {
      this.parser.requiredIf(options(), each);
    }
    return this;
  }
}