package com.svenwesterlaken.gemeentebreda.logic.exceptions;

/**
 * Created by Sven Westerlaken on 7-6-2017.
 */

public class NoLocationMetaException extends Exception {

    public NoLocationMetaException() {
        super("No location found for this media");
    }
}
