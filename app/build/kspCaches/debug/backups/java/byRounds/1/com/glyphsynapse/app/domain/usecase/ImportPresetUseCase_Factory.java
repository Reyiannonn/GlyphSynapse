package com.glyphsynapse.app.domain.usecase;

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
public final class ImportPresetUseCase_Factory implements Factory<ImportPresetUseCase> {
  private final Provider<SavePresetUseCase> savePresetProvider;

  public ImportPresetUseCase_Factory(Provider<SavePresetUseCase> savePresetProvider) {
    this.savePresetProvider = savePresetProvider;
  }

  @Override
  public ImportPresetUseCase get() {
    return newInstance(savePresetProvider.get());
  }

  public static ImportPresetUseCase_Factory create(Provider<SavePresetUseCase> savePresetProvider) {
    return new ImportPresetUseCase_Factory(savePresetProvider);
  }

  public static ImportPresetUseCase newInstance(SavePresetUseCase savePreset) {
    return new ImportPresetUseCase(savePreset);
  }
}
