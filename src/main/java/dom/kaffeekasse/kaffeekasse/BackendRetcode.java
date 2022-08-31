/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse;

/**
 * status codes.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public enum BackendRetcode {
    
    OK("OK"),
    
    ENTRY_NOT_FOUND("entryNotFound"),
    
    NO_VALID_ACCOUNT_ENTRY_KEY("invalidKeyForAccountEntry"),
    
    PARTICIPANT_NOT_FOUND("participantNotFound"),
    
    PARTICIPANT_ALREADY_EXISTS("participantAlreadyExists"),
    
    READ_ERROR("readFromBackendFailed"),
    
    WRITE_ERROR("writeToBackendFailed"),
    
    ZERO_PRICE("zeroPrice");
    
    private final String message;

    private BackendRetcode(String message) {
        this.message = message;
    }

    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMessage() {
        return message;
    }
    
}
