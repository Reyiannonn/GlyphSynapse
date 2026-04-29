package com.glyphsynapse.app.service;

import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper;
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
public final class GlyphNotificationListenerService_MembersInjector implements MembersInjector<GlyphNotificationListenerService> {
  private final Provider<GlyphManagerWrapper> glyphManagerProvider;

  public GlyphNotificationListenerService_MembersInjector(
      Provider<GlyphManagerWrapper> glyphManagerProvider) {
    this.glyphManagerProvider = glyphManagerProvider;
  }

  public static MembersInjector<GlyphNotificationListenerService> create(
      Provider<GlyphManagerWrapper> glyphManagerProvider) {
    return new GlyphNotificationListenerService_MembersInjector(glyphManagerProvider);
  }

  @Override
  public void injectMembers(GlyphNotificationListenerService instance) {
    injectGlyphManager(instance, glyphManagerProvider.get());
  }

  @InjectedFieldSignature("com.glyphsynapse.app.service.GlyphNotificationListenerService.glyphManager")
  public static void injectGlyphManager(GlyphNotificationListenerService instance,
      GlyphManagerWrapper glyphManager) {
    instance.glyphManager = glyphManager;
  }
}
