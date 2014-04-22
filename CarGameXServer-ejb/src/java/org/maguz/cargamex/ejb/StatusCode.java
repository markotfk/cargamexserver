package org.maguz.cargamex.ejb;

/**
 * Status codes for EJB functions.
 * @author Marko Karjalainen <markotfk@gmail.com>
 */
public enum StatusCode {

    /**
     * Success.
     */
    OK,

    /**
     * Unexpected error.
     */
    Error,

    /**
     * Item not found.
     */
    NotFound,

    /**
     * Item is rejected, unique constraint violated.
     */
    DuplicateEntry,

    /**
     * Access rejected due to invalid credentials (password, session id).
     */
    AuthenticationFailed,

    /**
     * Cannot complete operation (invalid input / state).
     */
    Forbidden
}
