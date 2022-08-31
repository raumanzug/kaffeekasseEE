/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse;

import jakarta.enterprise.inject.spi.InjectionPoint;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * global objects as logger instance and resource bundle for l10n.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class ApplicationConfiguration implements Serializable {

    private final InjectionPoint injectionPoint;
    private final ResourceBundle resourceBundle;
    private final Logger logger;

    /**
     * <p>Constructor for ApplicationConfiguration.</p>
     *
     * @param injectionPoint a {@link jakarta.enterprise.inject.spi.InjectionPoint} object
     */
    public ApplicationConfiguration(InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;

        {
            String name = injectionPoint.getMember().getDeclaringClass().getCanonicalName();

            this.logger = Logger.getLogger(name);
            this.logger.setLevel(Level.ALL);
        }
        
        this.resourceBundle =  ResourceBundle.getBundle("l10n");
    }

    /**
     * bundle (a structure used for l10n).
     *
     * @return a {@link java.util.ResourceBundle} object
     */
    public ResourceBundle bundle() {
        return this.resourceBundle;
    }

    /**
     * global logger instance.
     *
     * @return a {@link java.util.logging.Logger} object
     */
    public Logger logger() {
        return this.logger;
    }

}
