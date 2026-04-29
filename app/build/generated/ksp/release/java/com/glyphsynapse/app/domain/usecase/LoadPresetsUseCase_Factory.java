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
public final class LoadPresetsUseCase_Factory implements Factory<LoadPresetsUseCase> {
  private final Provider<UserPreferencesRepository> prefsProvider;

  public LoadPresetsUseCase_Factory(Provider<UserPreferencesRepository> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public LoadPresetsUseCase get() {
    return newInstance(prefsProvider.get());
  }

  public static LoadPresetsUseCase_Factory create(
      Provider<UserPreferencesRepository> prefsProvider) {
    return new LoadPresetsUseCase_Factory(prefsProvider);
  }

  public static LoadPresetsUseCase newInstance(UserPreferencesRepository prefs) {
    return new LoadPresetsUseCase(prefs);
  }
}
