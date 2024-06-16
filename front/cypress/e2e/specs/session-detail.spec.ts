/**
 * Suite de tests pour la page de détail de session
 * @description Vérifie que l'utilisateur peut voir les détails de la session,
 * que l'utilisateur peut participer à la session et que l'utilisateur peut ne pas participer à la session
 * @returns {void}
 */
export default function sessionDetailSpec() {
    describe("Session Detail spec", () => {

        // Test de redirection vers la page de détail de session
        it("Display session detail successfully", () => {
            cy.showSessionDetails();
            cy.url().should('include', '/session');
            cy.get('button[color="primary"]').should('contain.text', 'Participate');
            cy.get('h1').should('contain.text', 'Session 1');
            cy.get('.my2 > div[fxLayoutAlign="start center"]').should('contain.text', "0 attendees");
            cy.get('mat-card-content > div:not([fxLayout="row"])').should('contain.text', 'my description');
            cy.get('div[fxLayoutAlign="space-between center"].my2 > div:not([fxLayoutAlign="start center"])').should('contain.text', 'Create at:  May 26, 2024');
            cy.get('div[fxLayoutAlign="space-between center"].my2 > div:not([fxLayoutAlign="start center"])').should('contain.text', 'Last update:  June 5, 2024');
            cy.get('.my2 > div > .ml1').should('contain.text', 'June 10, 2024');
        });

        // Test de l'affichage des informations de l'enseignant
        it("Display teacher detail successfully", () => {
            cy.showSessionDetails();
            cy.get('.ml3').should('contain.text', 'Firstname LASTNAME');
        });

        // Test de participation à la session
        it("Participate successfully", () => {
            cy.intercept('POST', '**/participate/**', {
                statusCode: 200,
                body: {
                    message: 'Participation successful'
                },
            }).as('postParticipate');

            cy.showSessionDetails();

            cy.get('button[color="primary"]').should('contain.text', 'Participate').click();

            cy.wait('@postParticipate').its('response.statusCode').should('eq', 200);
        });

        // Test de non-participation à la session
        it("Unparticipate successfully", () => {
            cy.intercept('DELETE', '**/participate/**', {
                statusCode: 200,
                body: {
                    message: 'Unparticipation successful'
                },
            }).as('deleteParticipate');

            cy.showSessionDetails({ participate: true });

            cy.get('button[color="warn"]').should('contain.text', 'Do not participate').click();

            cy.wait('@deleteParticipate').its('response.statusCode').should('eq', 200);
        });
    });
}