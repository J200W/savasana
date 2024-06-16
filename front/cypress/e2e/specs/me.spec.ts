/**
 * Suite de tests pour la page de profil
 * @description Vérifie que l'utilisateur peut voir son profil,
 * que l'administrateur peut voir son profil et que l'utilisateur peut se supprimer
 * et que l'utilisateur peut revenir en arrière
 * @returns {void}
 */
export default function meSpec() {
    describe('Me Spec', () => {

        // Test de redirection vers la page de connexion
        it('Redirect to loin page', () => {
            cy.visit('/me');
            cy.url().should('contain', '/login');
        });

        // Test de l'affichage des informations de l'utilisateur
        it('Display user information', () => {
            cy.intercept('GET', '/api/user/*', {
                fixture: 'me.res.json'
            }).as('me')
            cy.login();
            cy.get('[routerlink="me"]').should("exist").click();
            cy.get('mat-card-content > div')
                .should('contain', 'Name: User USER')
                .should('contain', 'Email: yoga@studio.com')
                .should('contain', 'Delete')
                .should('contain', 'May 16, 2024')
                .should('contain', 'June 16, 2024');
        });

        // Test de l'affichage des informations de l'administrateur
        it('Display admin information', () => {
            cy.intercept('GET', '/api/user/*', {
                fixture: 'me-admin.res.json'
            }).as('me-admin')
            cy.login();
            cy.get('[routerlink="me"]').should("exist").click();
            cy.get('mat-card-content > div')
                .should('contain', 'Name: Admin ADMIN')
                .should('contain', 'You are admin')
        });

        // Test de suppression de l'utilisateur
        it('Delete user', () => {
            cy.intercept('GET', '/api/user/*', {
                fixture: 'me.res.json'
            }).as('me')
            cy.intercept('DELETE', '/api/user/*', {
                statusCode: 200
            }).as('deleteUser')
            cy.login();
            cy.get('[routerlink="me"]').should("exist").click();
            cy.get('[color="warn"]').should('contain', 'Delete').click();
            cy.url().should('eq', 'http://localhost:4200/');
        });

        // Test de retour en arrière
        it('Go back', () => {
            cy.intercept('GET', '/api/user/*', {
                fixture: 'me.res.json'
            }).as('me')
            cy.login();
            cy.get('[routerlink="me"]').should("exist").click();
            cy.get('mat-card-title > div > button').should('contain', 'arrow_back').click();
        });
    });
}
