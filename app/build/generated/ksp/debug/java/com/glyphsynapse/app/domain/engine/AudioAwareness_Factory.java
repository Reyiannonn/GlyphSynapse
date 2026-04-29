package com.glyphsynapse.app.domain.engine;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class AudioAwareness_Factory implements Factory<AudioAwareness> {
  private final Provider<Context> contextProvider;

  public AudioAwareness_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AudioAwareness get() {
    return newInstance(contextProvider.get());
  }

  public static AudioAwareness_Factory create(Provider<Context> contextProvider) {
    return new AudioAwareness_Factory(contextProvider);
  }

  public static AudioAwareness newInstance(Context context) {
    return new AudioAwareness(context);
  }
}
