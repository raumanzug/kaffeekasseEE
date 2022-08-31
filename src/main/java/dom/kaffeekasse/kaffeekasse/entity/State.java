/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * <p>State class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
@Entity
@Table(name = "STATE")
public class State {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "bankDeposit")
    private Integer bankDeposit;

    @ManyToOne
    @JoinColumn(name = "accountPeriod_id", referencedColumnName = "id")
    private AccountPeriod accountPeriod;

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.Integer} object
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>bankDeposit</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getBankDeposit() {
        return bankDeposit;
    }

    /**
     * <p>Setter for the field <code>bankDeposit</code>.</p>
     *
     * @param bankDeposit a {@link java.lang.Integer} object
     */
    public void setBankDeposit(Integer bankDeposit) {
        this.bankDeposit = bankDeposit;
    }

    /**
     * <p>Getter for the field <code>accountPeriod</code>.</p>
     *
     * @return a {@link dom.kaffeekasse.kaffeekasse.entity.AccountPeriod} object
     */
    public AccountPeriod getAccountPeriod() {
        return accountPeriod;
    }

    /**
     * <p>Setter for the field <code>accountPeriod</code>.</p>
     *
     * @param accountPeriod a {@link dom.kaffeekasse.kaffeekasse.entity.AccountPeriod} object
     */
    public void setAccountPeriod(AccountPeriod accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "State{" + "id=" + id + ", bankDeposit=" + bankDeposit + ", accountPeriod=" + accountPeriod + '}';
    }

}
