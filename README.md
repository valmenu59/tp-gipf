# tp-gipf
Correction du TP GIPF (DUT Informatique Maubeuge, M2106)

On considère les classes *Joueur*, *Partie* et *Tournoi*.

On place quatre associations 1-* entre *Joueur* et *Partie*, pour représenter respectivement le joueur blanc, noir, le gagnant et le perdant.

Les autres choix de conception sont évidents. Voir le fichier `src/main/sql/create.sql` pour le script de création/modèle relationnel.

L'outil Gradle permet de télécharger automatiquement le driver PostgreSQL. Il est supporté nativement par les versions récentes d'Eclipse.

La classe *Main* comporte une série de tests (scénario nominal). 

Contactez-moi pour plus d'informations.
