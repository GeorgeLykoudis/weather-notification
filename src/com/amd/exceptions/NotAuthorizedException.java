package com.amd.exceptions;

/**
 * @author George Lykoudis
 * @version 29/04/2021
 */
public class NotAuthorizedException extends Exception {

    public NotAuthorizedException() {
        super("Not Authorized");
    }
}
