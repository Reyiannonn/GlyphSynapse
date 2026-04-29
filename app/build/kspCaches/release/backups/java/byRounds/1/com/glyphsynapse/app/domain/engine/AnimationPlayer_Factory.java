package com.glyphsynapse.app.domain.engine;

import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AnimationPlayer_Factory implements Factory<AnimationPlayer> {
  private final Provider<GlyphManagerWrapper> glyphManagerProvider;

  public AnimationPlayer_Factory(Provider<GlyphManagerWrapper> glyphManagerProvider) {
    this.glyphManagerProvider = glyphManagerProvider;
  }

  @Override
  public AnimationPlayer get() {
    return newInstance(glyphManagerProvider.get());
  }

  public static AnimationPlayer_Factory create(Provider<GlyphManagerWrapper> glyphManagerProvider) {
    return new AnimationPlayer_Factory(glyphManagerProvider);
  }

  public static AnimationPlayer newInstance(GlyphManagerWrapper glyphManager) {
    return new AnimationPlayer(glyphManager);
  }
}
