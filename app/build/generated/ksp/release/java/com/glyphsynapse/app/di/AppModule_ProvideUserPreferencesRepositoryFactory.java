package com.glyphsynapse.app.di;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideUserPreferencesRepositoryFactory implements Factory<UserPreferencesRepository> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public AppModule_ProvideUserPreferencesRepositoryFactory(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public UserPreferencesRepository get() {
    return provideUserPreferencesRepository(dataStoreProvider.get());
  }

  public static AppModule_ProvideUserPreferencesRepositoryFactory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new AppModule_ProvideUserPreferencesRepositoryFactory(dataStoreProvider);
  }

  public static UserPreferencesRepository provideUserPreferencesRepository(
      DataStore<Preferences> dataStore) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUserPreferencesRepository(dataStore));
  }
}
