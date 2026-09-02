package fr.dev.sensei.guild.keeper.recruitment;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Atelier du chapitre 2 — test d'effet de bord.
 *
 * <p>{@link Member#addExperience(int)} ne retourne rien : elle agit par effet de bord en
 * modifiant {@code experiencePoints}. Le test interroge donc l'état de l'objet après l'appel.
 *
 * <p>À compléter :
 * <ul>
 *   <li>{@code addExperience(60)} sur un membre à 0 XP -> {@code experiencePoints()} vaut 60 ;</li>
 *   <li>{@code addExperience(0)} ou une valeur négative -> {@link IllegalArgumentException}.</li>
 * </ul>
 *
 * <p>Une fois écrit, retirer {@code @Tag("todo")} et le {@code fail(...)}.
 */
class MemberTest {

    // TODO: Chapitre 2 — « Atelier pratique - Consolider la suite de tests GuildKeeper » (effet de bord)
    @Tag("todo")
    @Test
    void should_increase_experience_points_when_experience_is_added() {
        fail("Test à compléter");
    }

    // TODO: Chapitre 2 — « Atelier pratique - Consolider la suite de tests GuildKeeper » (effet de bord)
    @Tag("todo")
    @Test
    void should_reject_a_non_positive_experience_gain() {
        fail("Test à compléter");
    }
}
