package edu.stanford.owl2lpg.translator.shared;

import com.google.auto.value.AutoValue;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@AutoValue
public abstract class ByteArray {

  @Nonnull
  public static ByteArray of(byte[] byteArray) {
    return new AutoValue_ByteArray(byteArray);
  }

  @Nonnull
  @SuppressWarnings("mutable")
  public abstract byte[] asArray();

  @Nonnull
  public String asDigestString() {
    return Hashing.sha256().hashBytes(asArray()).toString();
  }

  @Nonnull
  public String asDigestString(HashFunction hashFunction) {
    return hashFunction.hashBytes(asArray()).toString();
  }

  @Override
  public String toString() {
    return asDigestString();
  }
}