package com.glyphsynapse.app.domain.usecase;

import com.glyphsynapse.app.data.datastore.UserPreferencesRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SavePresetUseCase_Factory implements Factory<SavePresetUseCase> {
  private final Provider<UserPreferencesRepository> prefsProvider;

  private final Provider<LoadPresetsUseCase> loadPresetsProvider;

  public SavePresetUseCase_Factory(Provider<UserPreferencesRepository> prefsProvider,
      Provider<LoadPresetsUseCase> loadPresetsProvider) {
    this.prefsProvider = prefsProvider;
    this.loadPresetsProvider = loadPresetsProvider;
  }

  @Override
  public SavePresetUseCase get() {
    return newInstance(prefsProvider.get(), loadPresetsProvider.get());
  }

  public static SavePresetUseCase_Factory create(Provider<UserPreferencesRepository> prefsProvider,
      Provider<LoadPresetsUseCase> loadPresetsProvider) {
    return new SavePresetUseCase_Factory(prefsProvider, loadPresetsProvider);
  }

  public static SavePresetUseCase newInstance(UserPreferencesRepository prefs,
      LoadPresetsUseCase loadPresets) {
    return new SavePresetUseCase(prefs, loadPresets);
  }
}
