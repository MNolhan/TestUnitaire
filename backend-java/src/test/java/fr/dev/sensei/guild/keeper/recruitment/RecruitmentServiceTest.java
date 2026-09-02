package fr.dev.sensei.guild.keeper.recruitment;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RecruitmentService recruitmentService;

    @Test
    void should_recruit_candidate_when_name_is_valid_and_not_taken() {
        // Arrange
        when(memberRepository.findByName("Dorian")).thenReturn(Optional.empty());

        // Act
        Member recruit = recruitmentService.recruit("Dorian");

        // Assert
        assertThat(recruit.name()).isEqualTo("Dorian");
        assertThat(recruit.rank()).isEqualTo(MemberRank.NOVICE);
        assertThat(recruit.experiencePoints()).isZero();

        ArgumentCaptor<Member> savedMember = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(savedMember.capture());
        assertThat(savedMember.getValue().name()).isEqualTo("Dorian");
    }

    // TODO: Chapitre 4 — « TP guidé - Isoler le service de recrutement »
    //       (Given/When/Then posés en Chapitre 1 — « Atelier pratique - Premiers pas sur GuildKeeper »)
    @Tag("todo")
    @Test
    void should_throw_DuplicateMemberException_when_name_already_exists() {
        fail("Test à compléter");
    }

    // TODO: Chapitre 4 — « TP guidé - Isoler le service de recrutement »
    //       (Given/When/Then posés en Chapitre 1 — « Atelier pratique - Premiers pas sur GuildKeeper »)
    @Tag("todo")
    @Test
    void should_reject_candidate_when_name_is_blank() {
        fail("Test à compléter");
    }
}
