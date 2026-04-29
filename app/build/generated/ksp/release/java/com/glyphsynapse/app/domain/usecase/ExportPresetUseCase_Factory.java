package com.glyphsynapse.app.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ExportPresetUseCase_Factory implements Factory<ExportPresetUseCase> {
  @Override
  public ExportPresetUseCase get() {
    return newInstance();
  }

  public static ExportPresetUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ExportPresetUseCase newInstance() {
    return new ExportPresetUseCase();
  }

  private static final class InstanceHolder {
    private static final ExportPresetUseCase_Factory INSTANCE = new ExportPresetUseCase_Factory();
  }
}
