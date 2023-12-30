package com.costular.atomtasks.tasks.usecase;

import com.costular.atomtasks.data.settings.IsAutoforwardTasksSettingEnabledUseCase;
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
    "KotlinInternalInJava"
})
public final class AutoforwardTasksUseCase_Factory implements Factory<AutoforwardTasksUseCase> {
  private final Provider<IsAutoforwardTasksSettingEnabledUseCase> isAutoforwardTasksSettingEnabledUseCaseProvider;

  private final Provider<ObserveTasksUseCase> observeTasksUseCaseProvider;

  private final Provider<UpdateTaskUseCase> updateTaskUseCaseProvider;

  public AutoforwardTasksUseCase_Factory(
      Provider<IsAutoforwardTasksSettingEnabledUseCase> isAutoforwardTasksSettingEnabledUseCaseProvider,
      Provider<ObserveTasksUseCase> observeTasksUseCaseProvider,
      Provider<UpdateTaskUseCase> updateTaskUseCaseProvider) {
    this.isAutoforwardTasksSettingEnabledUseCaseProvider = isAutoforwardTasksSettingEnabledUseCaseProvider;
    this.observeTasksUseCaseProvider = observeTasksUseCaseProvider;
    this.updateTaskUseCaseProvider = updateTaskUseCaseProvider;
  }

  @Override
  public AutoforwardTasksUseCase get() {
    return newInstance(isAutoforwardTasksSettingEnabledUseCaseProvider.get(), observeTasksUseCaseProvider.get(), updateTaskUseCaseProvider.get());
  }

  public static AutoforwardTasksUseCase_Factory create(
      Provider<IsAutoforwardTasksSettingEnabledUseCase> isAutoforwardTasksSettingEnabledUseCaseProvider,
      Provider<ObserveTasksUseCase> observeTasksUseCaseProvider,
      Provider<UpdateTaskUseCase> updateTaskUseCaseProvider) {
    return new AutoforwardTasksUseCase_Factory(isAutoforwardTasksSettingEnabledUseCaseProvider, observeTasksUseCaseProvider, updateTaskUseCaseProvider);
  }

  public static AutoforwardTasksUseCase newInstance(
      IsAutoforwardTasksSettingEnabledUseCase isAutoforwardTasksSettingEnabledUseCase,
      ObserveTasksUseCase observeTasksUseCase, UpdateTaskUseCase updateTaskUseCase) {
    return new AutoforwardTasksUseCase(isAutoforwardTasksSettingEnabledUseCase, observeTasksUseCase, updateTaskUseCase);
  }
}
