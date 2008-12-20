package org.hamcrest;

import org.hamcrest.internal.ReflectiveTypeFinder;

/**
 * Supporting class for matching a feature of an object. Implement <code>valueOfPartIn()</code>
 * in a subclass to pull out the feature to be matched against. 
 *
 * @param <T> The type of the object to be matched
 * @param <U> The type of the feature to be matched
 */
public abstract class FeatureMatcher<T, U> extends TypeSafeDiagnosingMatcher<T> {
  private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("featureValueOf", 1, 0); 
  private final Matcher<? super U> subMatcher;
  private final String featureDescription;
  private final String featureName;
  
  /**
   * Constructor
   * @param subMatcher The matcher to apply to the feature
   * @param featureDescription Descriptive text to use in describeTo
   * @param featureName Identifying text for mismatch message
   */
  public FeatureMatcher(Matcher<? super U> subMatcher, String featureDescription, String featureName) {
    super(TYPE_FINDER);
    this.subMatcher = subMatcher;
    this.featureDescription = featureDescription;
    this.featureName = featureName;
  }
  
  protected abstract U featureValueOf(T actual);

  @Override
  protected boolean matchesSafely(T actual, Description mismatchDescription) {
    final U featureValue = featureValueOf(actual);
    if (!subMatcher.matches(featureValue)) {
      mismatchDescription.appendText(featureName).appendText(" was ").appendValue(featureValue);
      return false;
    }
    return true;
  };
      
  public final void describeTo(Description description) {
    description.appendText(featureDescription).appendText(" ")
               .appendDescriptionOf(subMatcher);
  }
}