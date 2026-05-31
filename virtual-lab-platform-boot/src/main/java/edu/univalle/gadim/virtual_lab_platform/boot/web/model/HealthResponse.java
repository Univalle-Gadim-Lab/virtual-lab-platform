package edu.univalle.gadim.virtual_lab_platform.boot.web.model;

/**
 * Response payload returned by the health endpoint.
 *
 * @param status the application status, typically {@code "UP"}
 */
public record HealthResponse(String status) {
}
