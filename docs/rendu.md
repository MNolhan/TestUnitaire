# GuildKeeper - Projet final

**Nom :**
**Date :**
**Dépôt Git :**

> Ce fichier a deux rôles : la checklist ci-dessous sert de suivi pendant les 3 heures, la synthèse en fin de fichier est le livrable 5. Garder la synthèse sur une page maximum.

---

## Suivi des tâches

Le détail de chaque livrable est dans les slides du projet. Cette checklist ne reprend que la progression TDD des dividendes, où l'oubli d'un cas coûte des points, et les contrôles à passer avant le rendu.

### Livrables (cocher quand terminé)

- [x] Livrable 1 : suite de tests complète de `GuildFinanceService`
- [x] Livrable 2 : `finance.feature` et ses step definitions Cucumber
- [x] Livrable 3 : `distributeDividends` développé en TDD (détail ci-dessous)
- [x] Livrable 4 : rapport de couverture généré
- [x] Livrable 5 : synthèse écrite ci-dessous

### Livrable 3 - Progression TDD des dividendes

Un cycle rouge -> vert -> refactor à chaque palier, chaque test écrit avant le code de production.

- [x] palier 1 : guilde vide -> répartition retournée vide, compte inchangé (test rouge imposé, à écrire en premier)
- [x] palier 2 : un seul membre -> il reçoit toute l'enveloppe, le compte est débité d'autant
- [x] palier 3 : deux membres de rangs différents -> parts au prorata des poids, reliquat laissé sur le compte
- [x] palier 4 : `@ParameterizedTest` sur `p` invalide (`0`, `-5`) -> `InvalidAmountException`, compte inchangé
- [x] palier 5 : `p > 100`, un seul membre, solde `100`, `p = 200` -> `checkSolvency` renvoie `false` -> `InsufficientFundsException`, compte inchangé
- [x] palier 6 : le solde ne devient jamais négatif

### Contrôles avant rendu

- [x] `./mvnw test` et `npm test` verts
- [x] `./mvnw test -Ptodo` vert : plus aucun message « Test à compléter »
- [x] `npm run test:todo` vert
- [x] couverture du module `finance` supérieure ou égale à 80 %
- [x] aucun test flaky : la suite passe aussi quand l'ordre des tests change
- [x] méthodes existantes de `GuildFinanceService` non modifiées (hors `distributeDividends`)

---

## Synthèse écrite (livrable 5, une page maximum)

### Niveau de couverture retenu

Couverture obtenue sur le module finance : 90 %

Je ne suis pas monté plus haut volontairement. Les lignes qui restent sont soit des cas d'erreur qui ne peuvent pas se produire dans le déroulement normal, soit du code déjà présent que je n'avais pas à retester. Les couvrir juste pour le chiffre n'aurait servi à rien.

### Choix de stratégie de test

Toute la logique du service est testée en unitaire avec JUnit, AssertJ et Mockito. Le repository est mocké parce que les opérations n'ont rien à lire dedans, elles font seulement de l'écriture : je vérifie donc que la sauvegarde est appelée sur les cas normaux, et qu'elle ne l'est pas quand une exception est levée. La méthode checkSolvency n'a pas de dépendance, je la teste sans mock.

Les cas limites qui ne font varier qu'une valeur (montant nul ou négatif, solde insuffisant, pourcentage invalide) sont regroupés en tests paramétrés plutôt qu'un test par valeur.

Cucumber ne couvre qu'un scénario, la distribution de butin, joué avec le vrai repository en mémoire. C'est le seul cas testé de bout en bout ; les cas d'erreur restent en unitaire pour éviter de tout écrire deux fois.

Les comptes et les membres sont créés directement dans chaque test, sans données partagées, avec des valeurs simples choisies pour pouvoir recalculer le résultat de tête. Pas d'horloge ni de tirage aléatoire, et j'ai relancé la suite dans un ordre aléatoire pour m'assurer qu'aucun test ne dépend d'un autre.

### Problèmes rencontrés et solutions

Au palier 5, quand je débitais le compte sans contrôle préalable, c'est une IllegalStateException qui était levée au lieu de InsufficientFundsException. J'ai ajouté l'appel à checkSolvency avant le débit, comme l'impose la règle 5.

La commande mvn verify échouait sur la vérification de format (Spotless) alors que tous les tests passaient. mvn spotless:apply a réglé le problème.
