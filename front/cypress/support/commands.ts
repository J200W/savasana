Cypress.Commands.add('login', ({ admin = false } = {}) => {
    cy.visit('/login');

    // Intercept session and login requests
    cy.intercept('GET', '/api/session', { fixture: 'sessions-list.res' }).as('getLogin');
    let email = "yoga@studio.com"

    if (admin) {
        cy.intercept('POST', '/api/auth/login', { fixture: 'login-admin.res' });
    }
    else {
        cy.intercept('POST', '/api/auth/login', { fixture: 'login-user.res' });
        email = "yoga-user@studio.com"
    }

    // Fill in login form and submit
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    // Wait for login process to complete
    cy.wait('@getLogin');

    cy.request('POST', 'http://localhost:8080/api/auth/login', {
        email: "yoga@studio.com",
        password: "test!1234"
    })
});

Cypress.Commands.add('fillRegistrationForm', ({ firstName, lastName, email, password }) => {
    cy.get('input[formControlName=firstName]').type(firstName);
    cy.get('input[formControlName=lastName]').type(lastName);
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(`${password}{enter}`);
});

Cypress.Commands.add('showSessionDetails', ({ participate = false } = {}) => {
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