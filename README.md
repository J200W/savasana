# Savasana

## Initialisation du projet

Cloner le projet:

> git clone https://github.com/J200W/savasana

### 1. Lancer le front-end:

1 - Aller dans le dossier front:

> cd savasana/front

2 - Installer les dépendances npm:

> npm install

3 - Lancer le front-end:

> npm run start;

### 2. Lancer le back-end:

1 - Aller dans le dossier back:

> cd savasana/back

2 - Installer les dépendances maven:

> mvn install

3 - Configurer le fichier .env:

> savasana/back/src/main/resources/.env

Remplacer les valeurs par les vôtres.
 
4 - Lancer le back-end:

> mvn spring-boot:run

### 3. Lancer le serveur MySQL:

1 - Lancer le serveur MySQL sur le port 3306 et executer le script SQL pour créer la base de données:

> savasana/ressources/sql/script.sql

2 - Par défaut le compte admin est:
- login: yoga@studio.com
- password: test!1234

## Tests

### E2E (Cypress)

Lancer les tests e2e (end-to-end):

> npm run e2e

Générer le rapport de couverture (vous devez lancer les tests e2e avant):

> npm run e2e:coverage

Le rapport est disponible ici:

> front/coverage/lcov-report/index.html

### Test unitaire (Jest)

Lancer les tests:

> npm run test

pour suivre les changements:

> npm run test:watch

### Backend

Lancer les tests:

> mvn test

Ou pour un test spécifique:

> mvn test -Dtest=TestClassName