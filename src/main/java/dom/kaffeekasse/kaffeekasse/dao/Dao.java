/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.dao;

import dom.kaffeekasse.kaffeekasse.ApplicationConfiguration;
import dom.kaffeekasse.kaffeekasse.BackendRetcode;
import dom.kaffeekasse.kaffeekasse.BackendRetcodeRef;
import dom.kaffeekasse.kaffeekasse.entity.AccountEntry;
import dom.kaffeekasse.kaffeekasse.entity.AccountPeriod;
import dom.kaffeekasse.kaffeekasse.entity.Participant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * base operations on persistence layer.
 *
 * @author jfischer
 * @version $Id: $Id
 */
@ApplicationScoped
public class Dao {

    @PersistenceContext(name = "standard_persistence_unit")
    private EntityManager em;

    @Inject
    private ApplicationConfiguration applicationConfiguration;

    @Inject
    private StateDao stateDao;

    private final String postingTextCoffeeIn = "coffee in";
    private final String postingTextCoffeeOut = "coffee out";
    private final String postingTextPay = "pay";

    /**
     * add a participant with name <code>name</code>.
     *
     * @param backendRetcodeRef container containing status code after executing this method;
     * @param name name of participant added.
     */
    public void addParticipant(BackendRetcodeRef backendRetcodeRef, String name) {
        boolean doesParticipantExist = false;
        try {
            this.em.createNamedQuery("Participant.findByName", Participant.class)
                    .setParameter("name", name)
                    .getSingleResult();
            doesParticipantExist = true;
        } catch (NoResultException ex) {
        }

        if (doesParticipantExist) {
            this.applicationConfiguration.logger().info("participant " + name + " already exists");
            backendRetcodeRef.setBackendRetcode(BackendRetcode.PARTICIPANT_ALREADY_EXISTS);
        } else {
            Participant participant = new Participant();
            participant.setIsActive(true);
            participant.setName(name);
            participant.setNrCups(0);
            participant.setDeposit(0);

            this.em.persist(participant);
        }
    }

    /**
     * get the representation of a participant with name <code>name</code>.
     *
     * @param backendRetcodeRef container containing status code after executing this method;
     * @param name a {@link java.lang.String} object;
     * @return participant with name <code>name</code>.
     */
    public Participant getParticipant(BackendRetcodeRef backendRetcodeRef, String name) {
        Participant retval = null;

        try {
            retval = this.em.createNamedQuery("Participant.findByName", Participant.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException ex) {
            this.applicationConfiguration.logger().info("participant " + name + " not found");
            backendRetcodeRef.setBackendRetcode(BackendRetcode.PARTICIPANT_NOT_FOUND);
        }

        return retval;
    }

    /**
     * end up a account period and create another one.
     */
    public void clear() {
        this.stateDao.clear();
        List<Participant> activeParticipants
                = this.em.createNamedQuery("Participant.findActive", Participant.class)
                        .getResultList();
        activeParticipants
                .stream()
                .forEach(participant -> this.clearParticipant(participant));
    }

    /**
     * credit the price of a pack of coffee given from participant to the
     * accountant.
     *
     * @param participant participant giving a pack of coffee;
     * @param amount a int;
     * @param timestamp datetime at which this action was registered.
     */
    public void coffeeIn(Participant participant, int amount, Date timestamp) {
        int newDeposit = participant.getDeposit();
        newDeposit += amount;
        participant.setDeposit(newDeposit);
        this.em.merge(participant);
        this.stateDao.addAccountEntry(
                amount,
                0,
                this.postingTextCoffeeIn,
                timestamp,
                participant
        );
    }

    /**
     * count the nr of cups of coffee pulled by participant.
     *
     * @param participant participant taking cups of coffee;
     * @param nrCups number of cups taken by participant;
     * @param timestamp datetime at which this action was registered.
     */
    public void coffeeOut(Participant participant, int nrCups, Date timestamp) {
        int newNrCups = participant.getNrCups();
        newNrCups += nrCups;
        participant.setNrCups(newNrCups);
        this.em.merge(participant);

        this.stateDao.addAccountEntry(
                0,
                nrCups,
                this.postingTextCoffeeOut,
                timestamp,
                participant
        );
    }

    /**
     * delete an account entry.
     *
     * @param accountEntry the account entry to be deleted.
     */
    public void deleteAccountEntry(AccountEntry accountEntry) {
        AccountEntry managedAccountEntry = this.em.merge(accountEntry);
        this.em.remove(managedAccountEntry);
    }

    /**
     * make participant inactive.
     *
     * @param participant to be make inactive.
     */
    public void inactivateParticipant(Participant participant) {
        participant.setIsActive(false);
        this.em.merge(participant);
    }

    /**
     * get a sorted list of all accounting periods.
     *
     * @return sorted list of all accounting periods.
     */
    public List<AccountPeriod> listAccountPeriods() {
        List<AccountPeriod> retval
                = this.em.createNamedQuery("AccountPeriod.findAll", AccountPeriod.class)
                        .getResultList();
        retval.sort(this.stateDao.getAccountPeriodComparator());

        return retval;
    }

    /**
     * list all accounting entries related to this accounting period.
     *
     * @param accountPeriodId primary key in database of this accounting period;
     * @return list of all accounting entries in current accounting period.
     */
    public List<AccountEntry> listAccountEntries(Integer accountPeriodId) {
        List<AccountEntry> retval = null;

        if (accountPeriodId != null) {
            AccountPeriod accountPeriod = this.em.find(AccountPeriod.class, accountPeriodId);
            retval = listAccountEntries(accountPeriod);
        } else {
            this.applicationConfiguration.logger().severe("account period id is null.");
        }

        return retval;
    }

    /**
     * list all accounting entries related to this accounting period.
     *
     * @param accountPeriod database entity class related to this accounting period;
     * @return list of all accounting entries in current accounting period.
     */
    public List<AccountEntry> listAccountEntries(AccountPeriod accountPeriod) {
        List<AccountEntry> retval = null;

        if (accountPeriod != null) {
            retval
                    = this.em.createNamedQuery("AccountEntry.findAllInPeriod", AccountEntry.class)
                            .setParameter("id", accountPeriod)
                            .getResultList();
            retval.sort(null);
        } else {
            this.applicationConfiguration.logger().severe("account period is null.");
        }

        return retval;
    }

    /**
     * get a sorted list of all active participants.
     *
     * @return sorted list of all active participants.
     */
    public List<Participant> listActiveParticipants() {
        List<Participant> retval
                = this.em.createNamedQuery("Participant.findActive", Participant.class)
                        .getResultList();
        retval.sort(null);

        return retval;
    }

    /**
     * settle debt.
     *
     * @param participant participant who settles debt
     * @param amount a int.
     * @param timestamp datetime at which this action was registered.
     */
    public void pay(Participant participant, int amount, Date timestamp) {
        this.stateDao.addBankDeposit(amount);

        int deposit = participant.getDeposit();
        deposit += amount;
        participant.setDeposit(deposit);
        this.em.merge(participant);

        this.stateDao.addAccountEntry(
                amount,
                0,
                this.postingTextPay,
                timestamp,
                participant
        );
    }

    /**
     * account for the cups of coffee and reset the tally.
     *
     * @param the participant whos cups of coffee taken by him is processed.
     */
    private void clearParticipant(Participant participant) {
        int currentPrice = this.stateDao.getPrice();
        int deposit = participant.getDeposit();
        deposit -= currentPrice * participant.getNrCups();
        participant.setDeposit(deposit);
        participant.setNrCups(0);
        this.em.merge(participant);
    }

}
