/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.faces;

import dom.kaffeekasse.kaffeekasse.ApplicationConfiguration;
import dom.kaffeekasse.kaffeekasse.dao.Dao;
import dom.kaffeekasse.kaffeekasse.entity.AccountEntry;
import jakarta.faces.annotation.FacesConfig;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * backing bean for account period related state.
 *
 * @author jfischer
 * @version $Id: $Id
 */
@FacesConfig
@Named
@ViewScoped
public class AccountPeriodMBean implements Serializable {

    @Inject
    private ApplicationConfiguration applicationConfiguration;

    @Inject
    private Dao dao;

    @Inject
    private FacesContext facesContext;
    
    private Integer accountPeriodId;

    /**
     * <p>Getter for the field <code>accountPeriodId</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getAccountPeriodId() {
        return accountPeriodId;
    }

    /**
     * <p>Setter for the field <code>accountPeriodId</code>.</p>
     *
     * @param accountPeriodId a {@link java.lang.Integer} object
     */
    public void setAccountPeriodId(Integer accountPeriodId) {
        if (accountPeriodId == null) {
            this.applicationConfiguration.logger().severe("someone uses null account period.");
        }

        this.accountPeriodId = accountPeriodId;
    }

    /**
     * <p>deleteAccountEntry.</p>
     *
     * @param accountEntry a {@link dom.kaffeekasse.kaffeekasse.entity.AccountEntry} object
     * @return a {@link java.lang.String} object
     */
    @Transactional
    public String deleteAccountEntry(AccountEntry accountEntry) {
        this.dao.deleteAccountEntry(accountEntry);
        return "success";
    }

    /**
     * <p>getAccountEntries.</p>
     *
     * @return a {@link java.util.List} object
     */
    @Transactional
    public List<AccountEntry> getAccountEntries() {
        List<AccountEntry> retval =  this.dao.listAccountEntries(this.accountPeriodId);
        Collections.reverse(retval);
        return retval;
    }

}
