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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * <p>Participant class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
@Entity
@Table(name = "PARTICIPANT")
@NamedQueries({
    @NamedQuery(name = "Participant.findByName", query = "SELECT p FROM Participant p WHERE p.name = :name"),
    @NamedQuery(name = "Participant.findActive", query = "SELECT p FROM Participant p WHERE p.isActive = true")
})
public class Participant implements Comparable<Participant> {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @Column(name = "deposit")
    private int deposit;

    @Basic(optional = false)
    @Column(name = "nrCups")
    private int nrCups;

    @Basic(optional = false)
    @Column(name = "isActive")
    private boolean isActive;

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
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>deposit</code>.</p>
     *
     * @return a int
     */
    public int getDeposit() {
        return deposit;
    }

    /**
     * <p>Setter for the field <code>deposit</code>.</p>
     *
     * @param deposit a int
     */
    public void setDeposit(int deposit) {
        this.deposit = deposit;
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
     * <p>isIsActive.</p>
     *
     * @return a boolean
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * <p>Setter for the field <code>isActive</code>.</p>
     *
     * @param isActive a boolean
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Participant{" + "id=" + id + ", name=" + name + ", deposit=" + deposit + ", nrCups=" + nrCups + ", isActive=" + isActive + '}';
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Participant other) {
        return this.getName().compareTo(other.getName());
    }

}
