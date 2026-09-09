package fr.dev.sensei.guild.keeper.experience;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.assertj.core.api.Assertions;

/**

Chapitre 5 — live coding « TDD sur le calcul de niveau » — <b>à développer entièrement en TDD</b>.*
<p>La classe de production {@code LevelCalculator} n'existe pas encore : c'est le seul
exercice vraiment "from scratch" du cours. On la fait naitre d'un test, en suivant le
cycle red / green / refactor.*
<p>Règle visée : {@code levelFor(int experiencePoints)} renvoie le niveau d'affichage
d'un membre, dérivé de ses points d'expérience.
<ul>
<li>0 XP -> niveau 1 ;</li>
<li>+1 niveau tous les 100 XP (100 -> 2, 250 -> 3, ...) ;</li>
<li>expérience négative -> {@link IllegalArgumentException}.</li>
</ul>*
<p>Classe à créer dans ce package ({@code experience}), non câblée au reste du domaine.
Une fois un test écrit, retirer {@code @Tag("todo")} et le {@code fail(...)}.*/

class LevelCalculatorTest {

    // TODO: red -> écrire ce test, créer LevelCalculator, le faire passer au vert
    // @Tag("todo")
    @ParameterizedTest
    @CsvSource ( value = {
         "0, 1",
         "100, 2",
         "200, 3",
         "500, 6"
    })
    void should_return_level_1_for_zero_experience(int xp, int expectedLevel) {
        //Arrange
        LevelCalculator levelCalculator = new LevelCalculator();

        //Act
        int computedlvl = levelCalculator.levelFor(xp);

        //Assert
        Assertions.assertThat(computedlvl).isEqualTo(expectedLevel);
    }

    // // TODO: refactor -> ce deuxième palier force à généraliser la formule
    // @Tag("todo")
    // @Test
    // void should_return_level_2_from_100_experience_points() {
    //     fail("Test à compléter");
    // }

    // TODO: cas limite
    // @Tag("todo")
    @Test
    void should_reject_negative_experience() {
        //Arrange
        LevelCalculator levelCalculator = new LevelCalculator();

        //Act & Assert
        Assertions.assertThatThrownBy(() -> levelCalculator.levelFor(-1))
                  .isInstanceOf(IllegalArgumentException.class);
    }
}
