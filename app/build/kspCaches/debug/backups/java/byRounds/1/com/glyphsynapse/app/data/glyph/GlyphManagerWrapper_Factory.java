package com.glyphsynapse.app.data.glyph;

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
public final class GlyphManagerWrapper_Factory implements Factory<GlyphManagerWrapper> {
  private final Provider<Context> contextProvider;

  public GlyphManagerWrapper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public GlyphManagerWrapper get() {
    return newInstance(contextProvider.get());
  }

  public static GlyphManagerWrapper_Factory create(Provider<Context> contextProvider) {
    return new GlyphManagerWrapper_Factory(contextProvider);
  }

  public static GlyphManagerWrapper newInstance(Context context) {
    return new GlyphManagerWrapper(context);
  }
}
