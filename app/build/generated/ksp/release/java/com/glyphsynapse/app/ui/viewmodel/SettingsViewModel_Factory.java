package com.glyphsynapse.app.ui.viewmodel;

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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UserPreferencesRepository> prefsProvider;

  public SettingsViewModel_Factory(Provider<UserPreferencesRepository> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(prefsProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<UserPreferencesRepository> prefsProvider) {
    return new SettingsViewModel_Factory(prefsProvider);
  }

  public static SettingsViewModel newInstance(UserPreferencesRepository prefs) {
    return new SettingsViewModel(prefs);
  }
}
