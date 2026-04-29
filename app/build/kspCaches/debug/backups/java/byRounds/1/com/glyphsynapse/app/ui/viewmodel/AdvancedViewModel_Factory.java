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
public final class AdvancedViewModel_Factory implements Factory<AdvancedViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<UserPreferencesRepository> prefsProvider;

  private final Provider<GlyphManagerWrapper> glyphManagerProvider;

  public AdvancedViewModel_Factory(Provider<Context> contextProvider,
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GlyphManagerWrapper> glyphManagerProvider) {
    this.contextProvider = contextProvider;
    this.prefsProvider = prefsProvider;
    this.glyphManagerProvider = glyphManagerProvider;
  }

  @Override
  public AdvancedViewModel get() {
    return newInstance(contextProvider.get(), prefsProvider.get(), glyphManagerProvider.get());
  }

  public static AdvancedViewModel_Factory create(Provider<Context> contextProvider,
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GlyphManagerWrapper> glyphManagerProvider) {
    return new AdvancedViewModel_Factory(contextProvider, prefsProvider, glyphManagerProvider);
  }

  public static AdvancedViewModel newInstance(Context context, UserPreferencesRepository prefs,
      GlyphManagerWrapper glyphManager) {
    return new AdvancedViewModel(context, prefs, glyphManager);
  }
}
