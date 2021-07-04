# OpenClassRooms 
#### Tests P9
#### Louis Poirot
## Résumé
Créer des tests unitaires Junit, et tes tests integrations en simulant une database
--------------------------------------------------------
## tests unitaires
Dans les classes
business\impl\manager\ComptabiliteManagerImplTest
myerp/myerp-model/target/test-classes/com/dummy/myerp/model/bean/comptabilite/EcritureComptableTest.class

Procédure d'installation
--------------------------------------------------------
1er: Télécharger le repertoire myerp
2ième: Décompresser le ZIP myerp.zip
3eme: créer un projet maven myerp sous eclipse. Puis on rajoute les jar sqlcontext bootstrapContext et transactioncontext 

4eme:Se placer sur la racine du projet en cours, puis faire un import de tous les fichiers dans l'application courante.

5eme: Faire un maven-clean et maven-install à la base du projet.
6eme: Faire un maven Validate
7eme: Faire un maven site afin de générer les rapports de test
------------------------------------------------------------

## Site 
### Generé avec maven, build, site
--------------------------------------------------------------------
### Accés à la couverture des tests
.../myerp/myerp-business/target/site/jacoco/index.htm
--------------------------------------------------------------
