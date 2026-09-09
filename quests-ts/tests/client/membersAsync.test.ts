import { describe, it, expect, vi } from 'vitest';
import { createGuildKeeperClient } from '../../src/client/guildKeeperClient';
import { NotFoundError } from '../../src/client/errors';
import { jsonResponse, fetchedUrl } from './httpTestSupport';

// Chapitre 3 — « Tester une fonction asynchrone » (TP).
//
// Modèle : tests/client/guildKeeperClient.test.ts (fetchImpl simulé + async/await).
// Cible : client.members.assignments(name) et client.members.get(name).

const BASE = 'http://api.test';

function clientWith(response: Response) {
  const fetchImpl = vi.fn().mockResolvedValue(response);
  return { client: createGuildKeeperClient({ baseUrl: BASE, fetchImpl }), fetchImpl };
}

describe('members (asynchrone) — TP chapitre 3', () => {
  it('members.assignments(name) résout la liste des attributions', async () => {
    // Arrange : fetchImpl simulé qui répond 200 avec deux attributions
    const body = [
      { questId: '1', questTitle: 'Nettoyer les caves de la guilde', status: 'COMPLETED' },
      { questId: '2', questTitle: 'Escorter la caravane marchande', status: 'ASSIGNED' },
    ];
    const { client, fetchImpl } = clientWith(jsonResponse(body));

    // Act
    const assignments = await client.members.assignments('Dragan');

    // Assert
    expect(fetchedUrl(fetchImpl)).toBe('http://api.test/api/v1/members/Dragan/assignments');
    expect(assignments).toHaveLength(2);
    expect(assignments.map((a) => a.status)).toEqual(['COMPLETED', 'ASSIGNED']);
  });

  it('members.get(name) rejette avec NotFoundError pour un membre inconnu', async () => {
    // Arrange : fetchImpl simulé qui répond 404 { "error": "NOT_FOUND" }
    const { client } = clientWith(
      jsonResponse({ error: 'NOT_FOUND', message: 'Membre inconnu : Gandalf' }, { status: 404 }),
    );

    // Act & Assert
    await expect(client.members.get('Gandalf')).rejects.toThrow(NotFoundError);
  });
});
