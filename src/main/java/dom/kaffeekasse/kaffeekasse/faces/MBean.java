/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.faces;

import dom.kaffeekasse.kaffeekasse.ApplicationConfiguration;
import dom.kaffeekasse.kaffeekasse.BackendRetcode;
import dom.kaffeekasse.kaffeekasse.BackendRetcodeRef;
import dom.kaffeekasse.kaffeekasse.dao.Dao;
import dom.kaffeekasse.kaffeekasse.dao.StateDao;
import dom.kaffeekasse.kaffeekasse.entity.AccountPeriod;
import dom.kaffeekasse.kaffeekasse.resource.TallyPDFDownloadServlet;
import jakarta.faces.annotation.FacesConfig;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * backing bean for jsf facelets.
 *
 * @author jfischer
 * @version $Id: $Id
 */
@FacesConfig
@Named
@ViewScoped
public class MBean implements Serializable {

    @Inject
    private ApplicationConfiguration applicationConfiguration;

    @Inject
    private Dao dao;

    @Inject
    private StateDao stateDao;

    @Inject
    private TallyPDFDownloadServlet tallyPdfDownloadServlet;

    @Inject
    private FacesContext facesContext;

    private String nextParticipantsName = "";
    private Integer price = null;

    /**
     * <p>
     * Getter for the field <code>nextParticipantsName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getNextParticipantsName() {
        return nextParticipantsName;
    }

    /**
     * <p>
     * Setter for the field <code>nextParticipantsName</code>.</p>
     *
     * @param nextParticipantsName a {@link java.lang.String} object
     */
    public void setNextParticipantsName(String nextParticipantsName) {
        this.nextParticipantsName = nextParticipantsName;
    }

    /**
     * <p>
     * getTallyPdfDownloadUrl.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTallyPdfDownloadUrl() {
        return this.tallyPdfDownloadServlet.getDownloadUrl();
    }

    /**
     * <p>
     * coffeeIn.</p>
     *
     * @param participantFO a
     * {@link dom.kaffeekasse.kaffeekasse.faces.ParticipantFacesObject} object
     * @return a {@link java.lang.String} object
     */
    @Transactional
    public String coffeeIn(ParticipantFacesObject participantFO) {
        Date now = new Date();
        this.dao.coffeeIn(participantFO.getParticipant(), participantFO.getCoffeeInCtAmount(), now);

        return "success";
    }

    /**
     * <p>
     * coffeeOut.</p>
     *
     * @param participantFO a
     * {@link dom.kaffeekasse.kaffeekasse.faces.ParticipantFacesObject} object
     * @return a {@link java.lang.String} object
     */
    @Transactional
    public String coffeeOut(ParticipantFacesObject participantFO) {
        Date now = new Date();
        this.dao.coffeeOut(participantFO.getParticipant(), participantFO.getCoffeeOutAmount(), now);

        return "success";
    }

    /**
     * <p>
     * getAccountPeriods.</p>
     *
     * @return a {@link java.util.List} object
     */
    @Transactional
    public List<AccountPeriod> getAccountPeriods() {
        List<AccountPeriod> retval = this.dao.listAccountPeriods();
        Collections.reverse(retval);
        return retval;
    }

    /**
     * <p>
     * getActiveParticipants.</p>
     *
     * @return a {@link java.util.List} object
     */
    @Transactional
    public List<ParticipantFacesObject> getActiveParticipants() {
        return this.dao.listActiveParticipants()
                .parallelStream()
                .map(participant -> new ParticipantFacesObject(participant))
                .collect(Collectors.toList());
    }

    /**
     * <p>
     * getBankDeposit.</p>
     *
     * @return a float
     */
    @Transactional
    public float getBankDeposit() {
        int bankDeposit = this.stateDao.getBankDeposit();
        return ((float) bankDeposit) / 100.0F;
    }

    /**
     * <p>
     * Getter for the field <code>price</code>.</p>
     *
     * @return a float
     */
    @Transactional
    public float getPrice() {
        this.loadPrice();

        return ((float) this.price) / 100.0F;
    }

    /**
     * <p>
     * Setter for the field <code>price</code>.</p>
     *
     * @param priceFloat a float
     */
    public void setPrice(float priceFloat) {
        int price = Math.round(priceFloat * 100.0F);

        BackendRetcodeRef backendRetcodeRef = new BackendRetcodeRef();

        if (price == 0) {
            backendRetcodeRef.setBackendRetcode(BackendRetcode.ZERO_PRICE);
        } else {
            this.price = price;
        }

        String __ = this.setMessagesArea(backendRetcodeRef);
    }

    /**
     * <p>
     * submitPrice.</p>
     */
    @Transactional
    public void submitPrice() {
        AccountPeriod currentAccountPeriod = this.stateDao.getCurrentAccountPeriod();
        currentAccountPeriod.setPrice(this.price);
    }

    /**
     * <p>
     * clear.</p>
     *
     * @return a {@link java.lang.String} object
     */
    @Transactional
    public String clear() {
        this.dao.clear();
        return "success";
    }

    /**
     * <p>
     * inactivateParticipant.</p>
     *
     * @param participantFO a
     * {@link dom.kaffeekasse.kaffeekasse.faces.ParticipantFacesObject} object
     * @return a {@link java.lang.String} object
     */
    @Transactional
    public String inactivateParticipant(ParticipantFacesObject participantFO) {
        this.dao.inactivateParticipant(participantFO.getParticipant());
        return "success";
    }

    /**
     * <p>
     * pay.</p>
     *
     * @param participantFO a
     * {@link dom.kaffeekasse.kaffeekasse.faces.ParticipantFacesObject} object
     * @return a {@link java.lang.String} object
     */
    @Transactional
    public String pay(ParticipantFacesObject participantFO) {
        Date now = new Date();
        this.dao.pay(participantFO.getParticipant(), participantFO.getPayInCtAmount(), now);

        return "success";
    }

    /**
     * <p>
     * setNextParticipant.</p>
     *
     * @return a {@link java.lang.String} object
     */
    @Transactional
    public String setNextParticipant() {
        BackendRetcodeRef backendRetcodeRef = new BackendRetcodeRef();
        this.dao.addParticipant(backendRetcodeRef, this.nextParticipantsName);

        return this.setMessagesArea(backendRetcodeRef);
    }

    private FacesMessage getFacesMessage(BackendRetcodeRef backendRetcodeRef) {
        ResourceBundle l10nBundle = this.applicationConfiguration.bundle();
        String message = l10nBundle.getString(backendRetcodeRef.getBackendRetcode().getMessage());
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
    }

    private void loadPrice() {
        if (this.price == null) {
            this.price = this.stateDao.getPrice();
        }
    }

    private String setMessagesArea(BackendRetcodeRef backendRetcodeRef) {
        String retval = "success";

        if (!backendRetcodeRef.isOk()) {
            retval = "failure";
            this.facesContext.addMessage(null, this.getFacesMessage(backendRetcodeRef));
        }

        return retval;
    }

}
