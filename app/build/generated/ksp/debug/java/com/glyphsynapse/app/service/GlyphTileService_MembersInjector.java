package com.glyphsynapse.app.service;

import com.glyphsynapse.app.data.datastore.UserPreferencesRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GlyphTileService_MembersInjector implements MembersInjector<GlyphTileService> {
  private final Provider<UserPreferencesRepository> prefsProvider;

  public GlyphTileService_MembersInjector(Provider<UserPreferencesRepository> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  public static MembersInjector<GlyphTileService> create(
      Provider<UserPreferencesRepository> prefsProvider) {
    return new GlyphTileService_MembersInjector(prefsProvider);
  }

  @Override
  public void injectMembers(GlyphTileService instance) {
    injectPrefs(instance, prefsProvider.get());
  }

  @InjectedFieldSignature("com.glyphsynapse.app.service.GlyphTileService.prefs")
  public static void injectPrefs(GlyphTileService instance, UserPreferencesRepository prefs) {
    instance.prefs = prefs;
  }
}
