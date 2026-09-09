package fr.dev.sensei.guild.keeper.finance;

import fr.dev.sensei.guild.keeper.recruitment.Member;
import fr.dev.sensei.guild.keeper.recruitment.MemberRank;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Member, Integer> distributeDividends(GuildAccount account, List<Member> members, int percentage) {
        // Règle 1 : un pourcentage nul ou négatif est interdit, sans borne haute
        if (percentage <= 0) {
            throw new InvalidAmountException(percentage);
        }

        Map<Member, Integer> shares = new LinkedHashMap<>();
        // Règle 7 : guilde sans membre, rien n'est distribué, le compte est inchangé, la répartition retournée est vide
        if (members.isEmpty()) {
            return shares;
        }

        // Règle 1 : l'enveloppe totale vaut floor(solde * p / 100)
        int envelope = account.balance() * percentage / 100;
        // Règle 2 : poids par rang, la part d'un membre est proportionnelle au poids de son rang
        int totalWeight = members.stream().mapToInt(member -> weightOf(member.rank())).sum();

        // Règle 3 : part individuelle = floor(enveloppe * poids du rang / somme des poids), en division entière
        int totalShares = 0;
        for (Member member : members) {
            int share = envelope * weightOf(member.rank()) / totalWeight;
            shares.put(member, share);
            totalShares += share;
        }

        // Règle 5 : avant tout débit, réutiliser checkSolvency ; si false, lever InsufficientFundsException sans rien débiter ni retourner
        if (!checkSolvency(account, totalShares)) {
            throw new InsufficientFundsException(account.balance(), totalShares);
        }

        // Règle 6 : le compte est débité de la somme des parts puis persisté, le solde ne devient jamais négatif
        // Règle 4 : le reliquat (enveloppe - somme des parts) reste sur le compte de la guilde
        account.decreaseBy(totalShares);
        guildAccountRepository.save(account);
        return shares;
    }

    // Règle 2 : poids de chaque rang
    private static int weightOf(MemberRank rank) {
        return switch (rank) {
            case NOVICE -> 1;
            case APPRENTICE -> 2;
            case VETERAN -> 3;
            case ELITE -> 4;
            case GUILD_MASTER -> 5;
        };
    }
}
