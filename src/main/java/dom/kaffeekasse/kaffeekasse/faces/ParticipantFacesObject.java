/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.faces;

import dom.kaffeekasse.kaffeekasse.entity.Participant;

/**
 * extends {@link dom.kaffeekasse.kaffeekasse.entity.Participant} entity class.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class ParticipantFacesObject {

    private final Participant participant;

    private float coffeeInAmount = 0.0F;
    private int coffeeOutAmount =  0;
    private float payInAmount = 0.0F;

    /**
     * <p>Constructor for ParticipantFacesObject.</p>
     *
     * @param participant a {@link dom.kaffeekasse.kaffeekasse.entity.Participant} object
     */
    public ParticipantFacesObject(Participant participant) {
        this.participant = participant;
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
     * <p>Getter for the field <code>coffeeInAmount</code>.</p>
     *
     * @return a float
     */
    public float getCoffeeInAmount() {
        return coffeeInAmount;
    }

    /**
     * <p>Setter for the field <code>coffeeInAmount</code>.</p>
     *
     * @param coffeeInAmount a float
     */
    public void setCoffeeInAmount(float coffeeInAmount) {
        this.coffeeInAmount = coffeeInAmount;
    }

    /**
     * <p>Getter for the field <code>coffeeOutAmount</code>.</p>
     *
     * @return a int
     */
    public int getCoffeeOutAmount() {
        return coffeeOutAmount;
    }

    /**
     * <p>Setter for the field <code>coffeeOutAmount</code>.</p>
     *
     * @param coffeeOutAmount a int
     */
    public void setCoffeeOutAmount(int coffeeOutAmount) {
        this.coffeeOutAmount = coffeeOutAmount;
    }

    /**
     * <p>Getter for the field <code>payInAmount</code>.</p>
     *
     * @return a float
     */
    public float getPayInAmount() {
        return payInAmount;
    }

    /**
     * <p>Setter for the field <code>payInAmount</code>.</p>
     *
     * @param payInAmount a float
     */
    public void setPayInAmount(float payInAmount) {
        this.payInAmount = payInAmount;
    }

    /**
     * <p>getDepositEUR.</p>
     *
     * @return a float
     */
    public float getDepositEUR() {
        return ((float) this.participant.getDeposit()) / 100.0F;
    }

    /**
     * <p>getCoffeeInCtAmount.</p>
     *
     * @return a int
     */
    public int getCoffeeInCtAmount() {
        return Math.round(100.0F * this.getCoffeeInAmount());
    }

    /**
     * <p>getPayInCtAmount.</p>
     *
     * @return a int
     */
    public int getPayInCtAmount() {
        return Math.round(100.0F * this.getPayInAmount());
    }

}
