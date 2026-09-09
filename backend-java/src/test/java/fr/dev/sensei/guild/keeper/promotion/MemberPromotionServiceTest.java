package fr.dev.sensei.guild.keeper.promotion;

import fr.dev.sensei.guild.keeper.recruitment.Member;
import fr.dev.sensei.guild.keeper.recruitment.MemberRank;
import fr.dev.sensei.guild.keeper.recruitment.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberPromotionServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Test
    void should_promote_novice_to_apprentice_when_threshold_is_reached() {
        // Arrange
        Member dorian = new Member("m-1", "Dorian", MemberRank.NOVICE, 100, 4);
        when(memberRepository.findById("m-1")).thenReturn(Optional.of(dorian));
        MemberPromotionService service = new MemberPromotionService(memberRepository);

        // Act
        Optional<MemberRank> newRank = service.promoteIfEligible("m-1");

        // Assert
        assertThat(newRank).contains(MemberRank.APPRENTICE);
        assertThat(dorian.rank()).isEqualTo(MemberRank.APPRENTICE);
    }

    // TODO: Chapitre 5 — « Atelier pratique - Promouvoir un membre de la guilde »
    // @Tag("todo")
    // @ParameterizedTest(name = "{0} XP -> {1}")
    // @MethodSource("promotionThresholds")
    // void should_promote_to_next_rank_when_threshold_is_reached(MemberRank startingRank, int experiencePoints, MemberRank expectedRank) {
    //     fail("Test à compléter");
    // }

    static Stream<Arguments> promotionThresholds() {
        // TODO: Chapitre 5 — compléter avec les seuils VETERAN (300), ELITE (700) et GUILD_MASTER (1500)
        return Stream.of(
                Arguments.of(MemberRank.APPRENTICE, 0, MemberRank.APPRENTICE)
        );
    }

    // TODO: Chapitre 5 — « Atelier pratique - Promouvoir un membre de la guilde »
    // @Tag("todo")
    // @Test
    // void should_not_promote_member_who_is_already_guild_master() {
    //     fail("Test à compléter");
    // }
}
