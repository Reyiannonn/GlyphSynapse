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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<UserPreferencesRepository> prefsProvider;

  public BootReceiver_MembersInjector(Provider<UserPreferencesRepository> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  public static MembersInjector<BootReceiver> create(
      Provider<UserPreferencesRepository> prefsProvider) {
    return new BootReceiver_MembersInjector(prefsProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectPrefs(instance, prefsProvider.get());
  }

  @InjectedFieldSignature("com.glyphsynapse.app.service.BootReceiver.prefs")
  public static void injectPrefs(BootReceiver instance, UserPreferencesRepository prefs) {
    instance.prefs = prefs;
  }
}
