/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

/**
 * <p>AccountEntry class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
@Entity
@Table(name = "ACCOUNT_ENTRY")
@NamedQueries({
    @NamedQuery(name = "AccountEntry.findAllInPeriod", query =  "SELECT a FROM AccountEntry a WHERE a.accountPeriod = :id")
})
public class AccountEntry implements Comparable<AccountEntry> {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Basic(optional = false)
    @Column(name = "amount")
    private int amount;

    @Basic(optional = false)
    @Column(name = "nrCups")
    private int nrCups;

    @Basic(optional = false)
    @Column(name = "postingText")
    private String postingText;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    private Participant participant;

    @ManyToOne(optional = false)
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
     * <p>Getter for the field <code>timestamp</code>.</p>
     *
     * @return a {@link java.util.Date} object
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * <p>Setter for the field <code>timestamp</code>.</p>
     *
     * @param timestamp a {@link java.util.Date} object
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * <p>Getter for the field <code>amount</code>.</p>
     *
     * @return a int
     */
    public int getAmount() {
        return amount;
    }

    /**
     * <p>Setter for the field <code>amount</code>.</p>
     *
     * @param amount a int
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * <p>Getter for the field <code>nrCups</code>.</p>
     *
     * @return a int
     */
    public int getNrCups() {
        return nrCups;
    }

    /**
     * <p>Setter for the field <code>nrCups</code>.</p>
     *
     * @param nrCups a int
     */
    public void setNrCups(int nrCups) {
        this.nrCups = nrCups;
    }

    /**
     * <p>Getter for the field <code>postingText</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPostingText() {
        return postingText;
    }

    /**
     * <p>Setter for the field <code>postingText</code>.</p>
     *
     * @param postingText a {@link java.lang.String} object
     */
    public void setPostingText(String postingText) {
        this.postingText = postingText;
    }

    /**
     * <p>Getter for the field <code>participant</code>.</p>
     *
     * @return a {@link dom.kaffeekasse.kaffeekasse.entity.Participant} object
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * <p>Setter for the field <code>participant</code>.</p>
     *
     * @param participant a {@link dom.kaffeekasse.kaffeekasse.entity.Participant} object
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
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
    public int compareTo(AccountEntry other) {
        return this.getTimestamp().compareTo(other.getTimestamp());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "AccountEntry{" + "id=" + id + ", timestamp=" + timestamp + ", amount=" + amount + ", nrCups=" + nrCups + ", postingText=" + postingText + ", participant=" + participant + ", accountPeriod=" + accountPeriod + '}';
    }
    
}
