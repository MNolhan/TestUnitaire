import { describe, it, expect } from 'vitest';
import {
  availableQuests,
  findQuestById,
  findQuestByTitle,
  isQuestUnlocked,
} from '../../src/planner/unlockTree';
import type { QuestDto } from '../../src/client/dto';
import { todo } from '../todo';
import questsFixture from '../../fixtures/quests.json';

const quests = questsFixture as QuestDto[];
const [cellar, caravan, dragon] = quests;

describe('findQuestById / findQuestByTitle', () => {
  it('returns the matching quest when it exists', () => {
    expect(findQuestById(quests, '2')?.title).toBe('Escorter la caravane marchande');
    expect(findQuestByTitle(quests, 'Terrasser le dragon des cimes')?.id).toBe('3');
  });

  // TODO: Chapitre 3 — « Atelier pratique - Consolider les tests Vitest du module quêtes »
  it('returns undefined when the list is empty', () => {

    // 2 test dans un seul pas bon !!

    // Act
    const resultByID = findQuestById([], '1');
    // Assert
    expect(resultByID).toBeUndefined();

    // Act
    const resultByTitle = findQuestByTitle([], 'Nonexistent Quest');
    // Assert
    expect(resultByTitle).toBeUndefined();
  });
});

describe('isQuestUnlocked', () => {
  it('unlocks a quest that has no prerequisite', () => {
    expect(isQuestUnlocked(cellar!, [])).toBe(true);
  });

  it('unlocks a quest whose prerequisite has been completed', () => {
    expect(isQuestUnlocked(caravan!, ['1'])).toBe(true);
  });

  // TODO: Chapitre 3 — « Atelier pratique - Consolider les tests Vitest du module quêtes »
  it('locks a quest whose prerequisite has not been completed', () => {
    expect(isQuestUnlocked(caravan!, [])).toBe(false);
    expect(isQuestUnlocked(dragon!, ['1'])).toBe(false);
  });
});

describe('availableQuests', () => {
  it('lists only unlocked, not-yet-completed quests', () => {
    expect(availableQuests(quests, []).map((q) => q.id)).toEqual(['1']);
    expect(availableQuests(quests, ['1']).map((q) => q.id)).toEqual(['2']);
    expect(availableQuests(quests, ['1', '2']).map((q) => q.id)).toEqual(['3']);
    expect(availableQuests(quests, ['1', '2', '3'])).toEqual([]);
  });

  it('ignores the dragon quest until the caravan is done', () => {
    expect(availableQuests(quests, ['1']).map((q) => q.title)).not.toContain(dragon!.title);
  });
});
