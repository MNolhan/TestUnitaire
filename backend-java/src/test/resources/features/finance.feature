# language: fr
Fonctionnalité: Distribution de butin de la guilde
  En tant que maître de guilde
  Je veux distribuer du butin depuis la trésorerie commune vers un membre
  Afin de récompenser ce membre sans jamais mettre le compte de guilde à découvert

  Règle: Le butin est débité de la trésorerie commune et le compte ne devient jamais négatif

    Contexte:
      Soit un compte de guilde approvisionné de 500 pièces d'or

    Scénario: Distribution de butin depuis la trésorerie vers un membre
      Quand la guilde distribue 120 pièces d'or de butin à "Dragan"
      Alors le solde du compte de guilde est de 380 pièces d'or
