/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

/**
 * emits an {@link dom.kaffeekasse.kaffeekasse.ApplicationConfiguration} instance
 * so that CDI can inject it.
 *
 * @author jfischer
 * @version $Id: $Id
 */
@ApplicationScoped
public class ApplicationConfigurationProducer {
        
    @Produces
    ApplicationConfiguration getApplicationConfiguration(InjectionPoint p) {
        return new ApplicationConfiguration(p);
    }

}
