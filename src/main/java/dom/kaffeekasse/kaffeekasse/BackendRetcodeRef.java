/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse;

/**
 * container containing a status code.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class BackendRetcodeRef {

    private BackendRetcode backendRetcode = BackendRetcode.OK;

    /**
     * <p>Getter for the field <code>backendRetcode</code>.</p>
     *
     * @return a {@link dom.kaffeekasse.kaffeekasse.BackendRetcode} object
     */
    public BackendRetcode getBackendRetcode() {
        return backendRetcode;
    }

    /**
     * if status code means that no errors have occured.
     *
     * @return true if no errors have occured; false otherwise.
     */
    public boolean isOk() {
        return this.backendRetcode == BackendRetcode.OK;
    }

    /**
     * <p>Setter for the field <code>backendRetcode</code>.</p>
     *
     * @param backendRetcode a {@link dom.kaffeekasse.kaffeekasse.BackendRetcode} object
     */
    public void setBackendRetcode(BackendRetcode backendRetcode) {
        this.backendRetcode = backendRetcode;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "BackendRetcodeBean{" + "backendRetcode=" + backendRetcode + '}';
    }

}
