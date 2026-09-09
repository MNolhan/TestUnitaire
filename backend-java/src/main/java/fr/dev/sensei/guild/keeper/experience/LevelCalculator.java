package fr.dev.sensei.guild.keeper.experience;

public class LevelCalculator {
    public int levelFor(int xp) {
        if (xp < 0) {
            throw new IllegalArgumentException("Experience points cannot be negative");
        }
        return xp / 100 + 1;
    }
}
