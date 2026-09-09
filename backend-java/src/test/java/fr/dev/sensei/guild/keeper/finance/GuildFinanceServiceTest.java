package fr.dev.sensei.guild.keeper.finance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GuildFinanceServiceTest {

    @Mock
    private GuildAccountRepository accountRepository;

    @InjectMocks
    private GuildFinanceService service;

    @Test
    void should_increase_balance_and_persist_account_when_deposit_is_valid() {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 100);

        // Act
        service.deposit(account, 50);

        // Assert
        assertThat(account.balance()).isEqualTo(150);
        verify(accountRepository).save(account);
    }

    @Test
    void should_decrease_balance_and_persist_account_when_loot_distribution_is_valid() {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 100);

        // Act
        service.distributeLoot(account, 30);

        // Assert
        assertThat(account.balance()).isEqualTo(70);
        verify(accountRepository).save(account);
    }

    @Test
    void should_be_solvent_when_balance_covers_the_amount() {
        GuildAccount account = new GuildAccount("g-1", 100);

        assertThat(service.checkSolvency(account, 100)).isTrue();
    }

    @Test
    void should_not_be_solvent_when_amount_exceeds_balance() {
        GuildAccount account = new GuildAccount("g-1", 100);

        assertThat(service.checkSolvency(account, 101)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void should_throw_InvalidAmountException_when_deposit_amount_is_not_positive(int amount) {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 100);

        // Act + Assert
        assertThatThrownBy(() -> service.deposit(account, amount))
                .isInstanceOf(InvalidAmountException.class);

        // Assert
        assertThat(account.balance()).isEqualTo(100);
        verify(accountRepository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 100, 1_000})
    void should_throw_InsufficientFundsException_when_loot_amount_exceeds_balance(int amount) {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 50);

        // Act + Assert
        assertThatThrownBy(() -> service.distributeLoot(account, amount))
                .isInstanceOf(InsufficientFundsException.class);

        // Assert
        assertThat(account.balance()).isEqualTo(50);
        assertThat(account.balance()).isNotNegative();
        verify(accountRepository, never()).save(any());
    }

    @Test
    void should_allow_loot_distribution_of_the_whole_balance_without_going_negative() {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 80);

        // Act
        service.distributeLoot(account, 80);

        // Assert
        assertThat(account.balance()).isZero();
        assertThat(account.balance()).isNotNegative();
        verify(accountRepository).save(account);
    }
}
