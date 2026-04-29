package com.glyphsynapse.app.ui.viewmodel;

import android.content.Context;
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository;
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper;
import com.glyphsynapse.app.domain.engine.AudioAwareness;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<UserPreferencesRepository> prefsProvider;

  private final Provider<GlyphManagerWrapper> glyphManagerProvider;

  private final Provider<AudioAwareness> audioAwarenessProvider;

  public SettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GlyphManagerWrapper> glyphManagerProvider,
      Provider<AudioAwareness> audioAwarenessProvider) {
    this.contextProvider = contextProvider;
    this.prefsProvider = prefsProvider;
    this.glyphManagerProvider = glyphManagerProvider;
    this.audioAwarenessProvider = audioAwarenessProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(contextProvider.get(), prefsProvider.get(), glyphManagerProvider.get(), audioAwarenessProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GlyphManagerWrapper> glyphManagerProvider,
      Provider<AudioAwareness> audioAwarenessProvider) {
    return new SettingsViewModel_Factory(contextProvider, prefsProvider, glyphManagerProvider, audioAwarenessProvider);
  }

  public static SettingsViewModel newInstance(Context context, UserPreferencesRepository prefs,
      GlyphManagerWrapper glyphManager, AudioAwareness audioAwareness) {
    return new SettingsViewModel(context, prefs, glyphManager, audioAwareness);
  }
}
