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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

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
    // @Tag("todo")
    @Test
    void should_throw_DuplicateMemberException_when_name_already_exists() {
        // Arrange
        Member Nolhan = Member.novice("1", "Nolhan", 1);
        when(memberRepository.findByName("Nolhan")).thenReturn(Optional.of(Nolhan));

        // Act et Assert
        assertThatThrownBy(() -> recruitmentService.recruit("Nolhan"))
                .isInstanceOf(DuplicateMemberException.class)
                .hasMessageContaining("Nolhan");
            
        verify(memberRepository, never()).save(any());
    }

    // TODO: Chapitre 4 — « TP guidé - Isoler le service de recrutement »
    //       (Given/When/Then posés en Chapitre 1 — « Atelier pratique - Premiers pas sur GuildKeeper »)
    // @Tag("todo")
    @Test
    void should_reject_candidate_when_name_is_blank() {
        // Arrange
        Member blankNameMember = Member.novice("1", "", 1);

        // Act et Assert
        assertThatThrownBy(() -> recruitmentService.recruit(blankNameMember.name()))
                .isInstanceOf(IllegalArgumentException.class);
        
        verify(memberRepository, never()).findByName(any());
        verify(memberRepository, never()).save(any());
    }
}
