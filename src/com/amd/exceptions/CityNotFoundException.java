package com.amd.exceptions;

/**
 * @author George Lykoudis
 * @version 29/04/2021
 */
public class CityNotFoundException extends Exception {

    public CityNotFoundException() {
        super("Could Not Find the City.");
    }
}
