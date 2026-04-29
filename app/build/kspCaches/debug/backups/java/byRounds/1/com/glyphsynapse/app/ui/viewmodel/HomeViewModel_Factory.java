package com.glyphsynapse.app.ui.viewmodel;

import android.content.Context;
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository;
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper;
import com.glyphsynapse.app.domain.engine.AnimationPlayer;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<UserPreferencesRepository> prefsProvider;

  private final Provider<GlyphManagerWrapper> glyphManagerProvider;

  private final Provider<AnimationPlayer> animationPlayerProvider;

  public HomeViewModel_Factory(Provider<Context> contextProvider,
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GlyphManagerWrapper> glyphManagerProvider,
      Provider<AnimationPlayer> animationPlayerProvider) {
    this.contextProvider = contextProvider;
    this.prefsProvider = prefsProvider;
    this.glyphManagerProvider = glyphManagerProvider;
    this.animationPlayerProvider = animationPlayerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(contextProvider.get(), prefsProvider.get(), glyphManagerProvider.get(), animationPlayerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<Context> contextProvider,
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GlyphManagerWrapper> glyphManagerProvider,
      Provider<AnimationPlayer> animationPlayerProvider) {
    return new HomeViewModel_Factory(contextProvider, prefsProvider, glyphManagerProvider, animationPlayerProvider);
  }

  public static HomeViewModel newInstance(Context context, UserPreferencesRepository prefs,
      GlyphManagerWrapper glyphManager, AnimationPlayer animationPlayer) {
    return new HomeViewModel(context, prefs, glyphManager, animationPlayer);
  }
}
