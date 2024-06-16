/**
 * Commandes personnalisées pour Login
 */
Cypress.Commands.add('login', ({ admin = false } = {}) => {
    cy.visit('/login');

    // Intercepter la requête de session pour attendre la fin du processus de connexion
    cy.intercept('GET', '/api/session', { fixture: 'sessions-list.res' }).as('getLogin');
    let email = "yoga@studio.com"

    if (admin) {
        cy.intercept('POST', '/api/auth/login', { fixture: 'login-admin.res' });
    }
    else {
        cy.intercept('POST', '/api/auth/login', { fixture: 'login-user.res' });
        email = "yoga-user@studio.com"
    }

    // Remplir le formulaire de connexion
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    // Attendre la fin de la requête de connexion
    cy.wait('@getLogin');

    cy.request('POST', 'http://localhost:8080/api/auth/login', {
        email: "yoga@studio.com",
        password: "test!1234"
    })
});

/**
 * Commande personnalisée pour remplir le formulaire d'inscription
 */
Cypress.Commands.add('fillRegistrationForm', ({ firstName, lastName, email, password }) => {
    cy.get('input[formControlName=firstName]').type(firstName);
    cy.get('input[formControlName=lastName]').type(lastName);
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(`${password}{enter}`);
});

/**
 * Commande personnalisée pour afficher les détails d'une session
 */
Cypress.Commands.add('showSessionDetails', ({ participate = false } = {}) => {
    // Intercepter la requête de session en fonction de la participation
    // pour attendre la fin du chargement

    if (participate) {
        cy.intercept('GET', '/api/session/*', { fixture: 'session-participated.res' }).as('getSession');
    }
    else {
        cy.intercept('GET', '/api/session/*', { fixture: 'session.res' }).as('getSession');
    }

    cy.intercept('GET', /\/api\/teacher\/\d+/, { fixture: 'teacher.res' }).as('getTeacher');
    cy.login();
    cy.contains('mat-card-actions button', 'Detail').should('be.visible').click();
    cy.wait('@getSession');
    cy.wait('@getTeacher');
});