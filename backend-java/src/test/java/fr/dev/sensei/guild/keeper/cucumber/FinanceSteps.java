package fr.dev.sensei.guild.keeper.cucumber;

import fr.dev.sensei.guild.keeper.finance.GuildAccount;
import fr.dev.sensei.guild.keeper.finance.GuildAccountRepository;
import fr.dev.sensei.guild.keeper.finance.GuildFinanceService;
import fr.dev.sensei.guild.keeper.finance.InMemoryGuildAccountRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.fr.Soit;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceSteps {

    private GuildAccountRepository accountRepository = new InMemoryGuildAccountRepository();
    private GuildFinanceService financeService = new GuildFinanceService(accountRepository);
    private GuildAccount account;

    @Soit("un compte de guilde approvisionné de {int} pièces d'or")
    public void un_compte_de_guilde_approvisionne_de(int montant) {
        account = new GuildAccount("g-1", montant);
        accountRepository.save(account);
    }

    @Quand("la guilde distribue {int} pièces d'or de butin à {string}")
    public void la_guilde_distribue_du_butin_a(int montant, String membre) {
        financeService.distributeLoot(account, montant);
    }

    @Alors("le solde du compte de guilde est de {int} pièces d'or")
    public void le_solde_du_compte_de_guilde_est_de(int soldeAttendu) {
        assertThat(account.balance()).isEqualTo(soldeAttendu);
    }
}