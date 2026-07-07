package com.rentingframework.core.exception;
 
/**
 * Thrown by every core service's "not found" case (user, listing, request,
 * group, task). Kept as one type across all four services so a single
 * @ControllerAdvice — shared or copied into each app — can map this to
 * HTTP 404 without inspecting exception messages.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}