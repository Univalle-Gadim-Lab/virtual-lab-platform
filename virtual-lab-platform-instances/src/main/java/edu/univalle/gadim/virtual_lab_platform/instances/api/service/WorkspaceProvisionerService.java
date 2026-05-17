package edu.univalle.gadim.virtual_lab_platform.instances.api.service;

import javax.annotation.Nonnull;

public interface WorkspaceProvisionerService {

  @Nonnull
  String createWorkspace(String userId, boolean isPersistent);

  void stopWorkSpace(String containerId);
}
