import { describe, expect } from 'vitest';
import { todo } from '../todo';

// Chapitre 3 — « Tester une fonction asynchrone » (TP).
//
// Modèle : tests/client/guildKeeperClient.test.ts (fetchImpl simulé + async/await).
// Cible : client.members.assignments(name) et client.members.get(name).
// Une fois écrit, remplacer `todo(` par `it(`.
describe('members (asynchrone) — TP chapitre 3', () => {
  // TODO: fournir un fetchImpl qui répond 200 avec une liste d'attributions,
  // puis `await client.members.assignments('Dragan')` et vérifier le contenu.
  todo('members.assignments(name) résout la liste des attributions', () => {
    expect.fail('Test à compléter');
  });

  // TODO: fournir un fetchImpl qui répond 404 { "error": "NOT_FOUND" },
  // puis `await expect(client.members.get('Gandalf')).rejects.toThrow(NotFoundError)`.
  todo('members.get(name) rejette avec NotFoundError pour un membre inconnu', () => {
    expect.fail('Test à compléter');
  });
});
