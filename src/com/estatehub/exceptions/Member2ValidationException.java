package com.estatehub.exceptions;

/** Indicates invalid input or an invalid customer action in Member 2 services. */
public class Member2ValidationException extends Exception {
    public Member2ValidationException(String message) { super(message); }
}
