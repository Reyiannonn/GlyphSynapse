package com.glyphsynapse.app.ui.viewmodel;

import com.glyphsynapse.app.data.datastore.UserPreferencesRepository;
import com.glyphsynapse.app.domain.usecase.DeletePresetUseCase;
import com.glyphsynapse.app.domain.usecase.ExportPresetUseCase;
import com.glyphsynapse.app.domain.usecase.ImportPresetUseCase;
import com.glyphsynapse.app.domain.usecase.LoadPresetsUseCase;
import com.glyphsynapse.app.domain.usecase.SavePresetUseCase;
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
public final class PresetsViewModel_Factory implements Factory<PresetsViewModel> {
  private final Provider<UserPreferencesRepository> prefsProvider;

  private final Provider<LoadPresetsUseCase> loadPresetsProvider;

  private final Provider<SavePresetUseCase> savePresetProvider;

  private final Provider<DeletePresetUseCase> deletePresetProvider;

  private final Provider<ExportPresetUseCase> exportPresetProvider;

  private final Provider<ImportPresetUseCase> importPresetProvider;

  public PresetsViewModel_Factory(Provider<UserPreferencesRepository> prefsProvider,
      Provider<LoadPresetsUseCase> loadPresetsProvider,
      Provider<SavePresetUseCase> savePresetProvider,
      Provider<DeletePresetUseCase> deletePresetProvider,
      Provider<ExportPresetUseCase> exportPresetProvider,
      Provider<ImportPresetUseCase> importPresetProvider) {
    this.prefsProvider = prefsProvider;
    this.loadPresetsProvider = loadPresetsProvider;
    this.savePresetProvider = savePresetProvider;
    this.deletePresetProvider = deletePresetProvider;
    this.exportPresetProvider = exportPresetProvider;
    this.importPresetProvider = importPresetProvider;
  }

  @Override
  public PresetsViewModel get() {
    return newInstance(prefsProvider.get(), loadPresetsProvider.get(), savePresetProvider.get(), deletePresetProvider.get(), exportPresetProvider.get(), importPresetProvider.get());
  }

  public static PresetsViewModel_Factory create(Provider<UserPreferencesRepository> prefsProvider,
      Provider<LoadPresetsUseCase> loadPresetsProvider,
      Provider<SavePresetUseCase> savePresetProvider,
      Provider<DeletePresetUseCase> deletePresetProvider,
      Provider<ExportPresetUseCase> exportPresetProvider,
      Provider<ImportPresetUseCase> importPresetProvider) {
    return new PresetsViewModel_Factory(prefsProvider, loadPresetsProvider, savePresetProvider, deletePresetProvider, exportPresetProvider, importPresetProvider);
  }

  public static PresetsViewModel newInstance(UserPreferencesRepository prefs,
      LoadPresetsUseCase loadPresets, SavePresetUseCase savePreset,
      DeletePresetUseCase deletePreset, ExportPresetUseCase exportPreset,
      ImportPresetUseCase importPreset) {
    return new PresetsViewModel(prefs, loadPresets, savePreset, deletePreset, exportPreset, importPreset);
  }
}
