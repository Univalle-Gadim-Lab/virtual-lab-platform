package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

/**
 * Response DTO for remote session health check.
 *
 * <p>Reports whether the VNC server inside the workspace container is reachable.
 */
public record RemoteSessionStatusResponse(
    String status,
    boolean vncReachable) {
}
