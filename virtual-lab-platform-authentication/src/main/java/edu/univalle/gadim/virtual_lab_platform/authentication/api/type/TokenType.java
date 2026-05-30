package edu.univalle.gadim.virtual_lab_platform.authentication.api.type;

/**
 * Enumeration of JWT token categories.
 *
 * <p>Distinguishes between short-lived access tokens used for API authorization
 * and long-lived refresh tokens used to obtain new access tokens.
 */
public enum TokenType {

  /** Short-lived token authorizing API requests. */
  ACCESS,

  /** Long-lived token used to obtain new access tokens. */
  REFRESH
}
