package fr.dev.sensei.guild.keeper.finance;

/**
 * Operations financieres sur le compte d'une guilde.
 *
 * <p>Perimetre volontairement limite a ce qui est specifie : depot, distribution
 * de butin, verification de solvabilite. La distribution de dividendes par rang
 * n'est PAS implementee ici : elle est a developper en TDD par les etudiants.
 * Les instructions a suivre sont donnees avec le projet final.
 */
public class GuildFinanceService {

    private final GuildAccountRepository guildAccountRepository;

    public GuildFinanceService(GuildAccountRepository guildAccountRepository) {
        this.guildAccountRepository = guildAccountRepository;
    }

    /**
     * Credite le compte de la guilde.
     *
     * @throws InvalidAmountException si {@code amount <= 0}
     */
    public void deposit(GuildAccount account, int amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        account.increaseBy(amount);
        guildAccountRepository.save(account);
    }

    /**
     * Debite le compte du montant de butin distribue a un membre.
     *
     * @throws InsufficientFundsException si le compte n'est pas solvable pour ce montant
     */
    public void distributeLoot(GuildAccount account, int amount) {
        if (!checkSolvency(account, amount)) {
            throw new InsufficientFundsException(account.balance(), amount);
        }
        account.decreaseBy(amount);
        guildAccountRepository.save(account);
    }

    /** @return {@code true} si le solde couvre {@code amount}. */
    public boolean checkSolvency(GuildAccount account, int amount) {
        return account.balance() >= amount;
    }
}
