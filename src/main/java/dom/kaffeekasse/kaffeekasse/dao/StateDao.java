/*
 * (C) 2022; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.kaffeekasse.kaffeekasse.dao;

import dom.kaffeekasse.kaffeekasse.entity.AccountEntry;
import dom.kaffeekasse.kaffeekasse.entity.AccountPeriod;
import dom.kaffeekasse.kaffeekasse.entity.Participant;
import dom.kaffeekasse.kaffeekasse.entity.State;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Comparator;
import java.util.Date;

/**
 * persisted global objects.
 *
 * @author jfischer
 * @version $Id: $Id
 */
@ApplicationScoped
public class StateDao {

    @PersistenceContext(name = "standard_persistence_unit")
    private EntityManager em;

    private final int DEFAULT_PRICE = 12;
    private final int STATE_ID = 1;

    private State state = null;

    /**
     * add an entry to the current acounting period into the ledger.
     *
     * @param amount credited amount / ct;
     * @param nrCups nr of cups pulled;
     * @param postingText posting text related to this entry;
     * @param timestamp datetime at which this action is registered;
     * @param participant participant to whom this entry is related.
     */
    public void addAccountEntry(
            int amount,
            int nrCups,
            String postingText,
            Date timestamp,
            Participant participant
    ) {
        AccountEntry accountEntry = new AccountEntry();

        accountEntry.setAmount(amount);
        accountEntry.setNrCups(nrCups);
        accountEntry.setPostingText(postingText);
        accountEntry.setTimestamp(timestamp);
        accountEntry.setParticipant(participant);
        accountEntry.setAccountPeriod(this.getCurrentAccountPeriod());

        this.em.persist(accountEntry);
    }

    /**
     * add cash into the bank.
     *
     * @param amount amount of cash / ct.
     */
    public void addBankDeposit(int amount) {
        int bankDeposit = this.getBankDeposit();
        bankDeposit += amount;
        this.state.setBankDeposit(bankDeposit);
        this.em.merge(this.state);
    }

    /**
     * create the next account period.
     */
    public void clear() {
        int price = this.getPrice();
        createAccountPeriod(price);
    }
    
    /**
     * the total order on account period.
     *
     * @return the total order.
     */
    public Comparator<AccountPeriod> getAccountPeriodComparator() {
        AccountPeriod currentAccountPeriod =  this.getCurrentAccountPeriod();
        return (AccountPeriod fst, AccountPeriod snd) -> {
            int currentAccountPeriodId =  currentAccountPeriod.getId();
            boolean isFstCurrent =  fst.getId().equals(currentAccountPeriodId);
            boolean isSndCurrent =  snd.getId().equals(currentAccountPeriodId);
            
            int retval =  0;
            if (isFstCurrent && isSndCurrent) {
                retval =  0;
            } else if (isFstCurrent) {
                retval =  1;
            } else if (isSndCurrent) {
                retval =  -1;
            } else {
                retval = fst.getTimestamp().compareTo(snd.getTimestamp());
            }
            
            return retval;            
        };
    }

    /**
     * retrieve the amount of cash in the bank.
     *
     * @return amount of cash / ct.
     */
    public int getBankDeposit() {
        this.load();
        return this.state.getBankDeposit();
    }

    /**
     * current accounting period.
     *
     * @return accounting period.
     */
    public AccountPeriod getCurrentAccountPeriod() {
        this.load();
        return this.state.getAccountPeriod();
    }

    /**
     * current price of a cup of coffee.
     *
     * @return price of a cup of coffee / ct.
     */
    public int getPrice() {
        AccountPeriod currentAccountPeriod = this.getCurrentAccountPeriod();
        return currentAccountPeriod.getPrice();
    }

    /**
     * determine the a newer price of a cup of coffee.
     *
     * @param price the price / ct.
     */
    public void setPrice(int price) {
        AccountPeriod currentAccountPeriod = this.getCurrentAccountPeriod();
        currentAccountPeriod.setPrice(price);
        this.em.merge(currentAccountPeriod);
    }

    /**
     * create the next accounting period.
     *
     * @param int initial price / ct.
     */
    private void createAccountPeriod(int price) {
        AccountPeriod accountPeriod = new AccountPeriod();

        accountPeriod.setPrice(price);
        accountPeriod.setTimestamp(new Date());

        this.em.persist(accountPeriod);

        this.state.setAccountPeriod(accountPeriod);
        this.em.merge(this.state);
    }

    /**
     * initialize STATE table in database.
     */
    private void load() {
        if (this.state == null) {
            this.state = this.em.find(State.class, STATE_ID);
            if (this.state == null) {
                this.state = new State();
                this.state.setId(STATE_ID);
                this.state.setBankDeposit(0);

                this.createAccountPeriod(DEFAULT_PRICE);

                this.em.persist(this.state);
            }
        }
    }

}
