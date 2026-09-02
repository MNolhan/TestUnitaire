package fr.dev.sensei.guild.keeper.missions;

import fr.dev.sensei.guild.keeper.recruitment.Member;
import fr.dev.sensei.guild.keeper.recruitment.MemberRank;
import fr.dev.sensei.guild.keeper.recruitment.MemberRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestAssignmentServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private QuestRepository questRepository;

    private final QuestAssignmentRepository assignmentRepository = new InMemoryQuestAssignmentRepository();

    @Test
    void should_assign_quest_when_member_is_free_and_prerequisites_are_met() {
        // Arrange
        Member alberic = new Member("m-1", "Albéric", MemberRank.NOVICE, 0, 5);
        Quest quest = Quest.standalone("q-1", "Escorter la caravane", QuestDifficulty.MEDIUM, 120, 60);
        when(memberRepository.findById("m-1")).thenReturn(Optional.of(alberic));
        when(questRepository.findById("q-1")).thenReturn(Optional.of(quest));
        QuestAssignmentService service =
                new QuestAssignmentService(memberRepository, questRepository, assignmentRepository);

        // Act
        QuestAssignment assignment = service.assign("m-1", "q-1");

        // Assert
        assertThat(assignment.member()).isEqualTo(alberic);
        assertThat(assignment.quest()).isEqualTo(quest);
        assertThat(assignment.status()).isEqualTo(QuestAssignmentStatus.ASSIGNED);
        assertThat(assignmentRepository.findByMember("m-1")).containsExactly(assignment);
    }

    // TODO: Chapitre 5 — « Attribuer une quête en TDD » (approche par les interactions)
    @Tag("todo")
    @Test
    void should_reject_assignment_when_member_already_has_an_assigned_quest() {
        fail("Test à compléter");
    }

    // TODO: Chapitre 5 — « Attribuer une quête en TDD » (approche par les interactions)
    @Tag("todo")
    @Test
    void should_reject_assignment_when_a_prerequisite_quest_is_not_completed() {
        fail("Test à compléter");
    }
}
