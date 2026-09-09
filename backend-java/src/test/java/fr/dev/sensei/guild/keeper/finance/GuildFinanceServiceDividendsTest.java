package fr.dev.sensei.guild.keeper.finance;

import fr.dev.sensei.guild.keeper.recruitment.Member;
import fr.dev.sensei.guild.keeper.recruitment.MemberRank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GuildFinanceServiceDividendsTest {

    @Mock
    private GuildAccountRepository accountRepository;

    @InjectMocks
    private GuildFinanceService service;

    @Test
    void should_distribute_nothing_when_guild_has_no_members() {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 1_000);

        // Act
        Map<Member, Integer> shares = service.distributeDividends(account, List.of(), 10);

        // Assert
        assertThat(shares).isEmpty();
        assertThat(account.balance()).isEqualTo(1_000);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void should_give_the_whole_envelope_to_the_single_member() {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 1000);
        Member solo = new Member("m-1", "Dragan", MemberRank.VETERAN, 0, 5);

        // Act
        Map<Member, Integer> shares = service.distributeDividends(account, List.of(solo), 10);

        // Assert
        assertThat(shares).containsExactly(entry(solo, 100));
        assertThat(account.balance()).isEqualTo(1000-100);
        verify(accountRepository).save(account);
    }

    @Test
    void should_split_the_envelope_by_rank_weight_and_leave_the_remainder_on_the_account() {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 1000);
        Member apprentice = new Member("m-1", "Bree", MemberRank.APPRENTICE, 0, 5);
        Member elite = new Member("m-2", "Kael", MemberRank.ELITE, 0, 5);

        // Act
        Map<Member, Integer> shares = service.distributeDividends(account, List.of(apprentice, elite), 10);

        // Assert
        assertThat(shares).containsOnly(entry(apprentice, 33), entry(elite, 66));
        assertThat(shares.values().stream().mapToInt(Integer::intValue).sum()).isEqualTo(99);
        assertThat(account.balance()).isEqualTo(1000-99);
        verify(accountRepository).save(account);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5})
    void should_throw_InvalidAmountException_when_percentage_is_not_positive(int percentage) {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 1000);
        Member member = new Member("m-1", "Dragan", MemberRank.VETERAN, 0, 5);

        // Act + Assert
        assertThatThrownBy(() -> service.distributeDividends(account, List.of(member), percentage))
                .isInstanceOf(InvalidAmountException.class);

        assertThat(account.balance()).isEqualTo(1000);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void should_throw_InsufficientFundsException_when_envelope_exceeds_balance() {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 100);
        Member member = new Member("m-1", "Dragan", MemberRank.VETERAN, 0, 5);

        // Act + Assert
        assertThatThrownBy(() -> service.distributeDividends(account, List.of(member), 200))
                .isInstanceOf(InsufficientFundsException.class);

        assertThat(account.balance()).isEqualTo(100);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void should_never_let_the_balance_go_negative() {
        // Arrange
        GuildAccount account = new GuildAccount("g-1", 250);
        Member member = new Member("m-1", "Dragan", MemberRank.VETERAN, 0, 5);

        // Act
        Map<Member, Integer> shares = service.distributeDividends(account, List.of(member), 100);

        // Assert
        assertThat(shares).containsExactly(entry(member, 250));
        assertThat(account.balance()).isZero();
        assertThat(account.balance()).isNotNegative();
    }
}
