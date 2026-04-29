package com.glyphsynapse.app.service;

import com.glyphsynapse.app.data.datastore.UserPreferencesRepository;
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper;
import com.glyphsynapse.app.domain.engine.AnimationPlayer;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GlyphAnimationService_MembersInjector implements MembersInjector<GlyphAnimationService> {
  private final Provider<GlyphManagerWrapper> glyphManagerProvider;

  private final Provider<AnimationPlayer> animationPlayerProvider;

  private final Provider<UserPreferencesRepository> prefsProvider;

  public GlyphAnimationService_MembersInjector(Provider<GlyphManagerWrapper> glyphManagerProvider,
      Provider<AnimationPlayer> animationPlayerProvider,
      Provider<UserPreferencesRepository> prefsProvider) {
    this.glyphManagerProvider = glyphManagerProvider;
    this.animationPlayerProvider = animationPlayerProvider;
    this.prefsProvider = prefsProvider;
  }

  public static MembersInjector<GlyphAnimationService> create(
      Provider<GlyphManagerWrapper> glyphManagerProvider,
      Provider<AnimationPlayer> animationPlayerProvider,
      Provider<UserPreferencesRepository> prefsProvider) {
    return new GlyphAnimationService_MembersInjector(glyphManagerProvider, animationPlayerProvider, prefsProvider);
  }

  @Override
  public void injectMembers(GlyphAnimationService instance) {
    injectGlyphManager(instance, glyphManagerProvider.get());
    injectAnimationPlayer(instance, animationPlayerProvider.get());
    injectPrefs(instance, prefsProvider.get());
  }

  @InjectedFieldSignature("com.glyphsynapse.app.service.GlyphAnimationService.glyphManager")
  public static void injectGlyphManager(GlyphAnimationService instance,
      GlyphManagerWrapper glyphManager) {
    instance.glyphManager = glyphManager;
  }

  @InjectedFieldSignature("com.glyphsynapse.app.service.GlyphAnimationService.animationPlayer")
  public static void injectAnimationPlayer(GlyphAnimationService instance,
      AnimationPlayer animationPlayer) {
    instance.animationPlayer = animationPlayer;
  }

  @InjectedFieldSignature("com.glyphsynapse.app.service.GlyphAnimationService.prefs")
  public static void injectPrefs(GlyphAnimationService instance, UserPreferencesRepository prefs) {
    instance.prefs = prefs;
  }
}
