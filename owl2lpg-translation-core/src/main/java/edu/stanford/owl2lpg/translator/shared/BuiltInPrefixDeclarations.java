package edu.stanford.owl2lpg.translator.shared;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@AutoValue
public abstract class BuiltInPrefixDeclarations {

  @Nonnull
  public static BuiltInPrefixDeclarations get(@Nonnull ImmutableMap<String, String> prefixDeclarations) {
    return new AutoValue_BuiltInPrefixDeclarations(prefixDeclarations);
  }

  @Nonnull
  public abstract ImmutableMap<String, String> asMap();

  @Nullable
  public String getPrefixName(String iriPrefix) {
    return asMap().get(iriPrefix);
  }
}
