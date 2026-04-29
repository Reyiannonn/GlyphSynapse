package com.glyphsynapse.app.ui.viewmodel;

import android.content.Context;
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository;
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper;
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
public final class AnimationsViewModel_Factory implements Factory<AnimationsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<UserPreferencesRepository> prefsProvider;

  private final Provider<GlyphManagerWrapper> glyphManagerProvider;

  public AnimationsViewModel_Factory(Provider<Context> contextProvider,
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GlyphManagerWrapper> glyphManagerProvider) {
    this.contextProvider = contextProvider;
    this.prefsProvider = prefsProvider;
    this.glyphManagerProvider = glyphManagerProvider;
  }

  @Override
  public AnimationsViewModel get() {
    return newInstance(contextProvider.get(), prefsProvider.get(), glyphManagerProvider.get());
  }

  public static AnimationsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GlyphManagerWrapper> glyphManagerProvider) {
    return new AnimationsViewModel_Factory(contextProvider, prefsProvider, glyphManagerProvider);
  }

  public static AnimationsViewModel newInstance(Context context, UserPreferencesRepository prefs,
      GlyphManagerWrapper glyphManager) {
    return new AnimationsViewModel(context, prefs, glyphManager);
  }
}
