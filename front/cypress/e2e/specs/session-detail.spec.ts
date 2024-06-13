
export default function sessionDetailSpec() {
    describe("Session Detail spec", () => {

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

        it("Display teacher detail successfully", () => {
            cy.showSessionDetails();
            cy.get('.ml3').should('contain.text', 'Firstname LASTNAME');
        });

        it("Participate successfully", () => {
            // Intercept the participate network request
            cy.intercept('POST', '**/participate/**', {
                statusCode: 200,
                body: {
                    message: 'Participation successful'
                },
            }).as('postParticipate');

            // Show session details
            cy.showSessionDetails();

            // Ensure the participate button is present and click it
            cy.get('button[color="primary"]').should('contain.text', 'Participate').click();

            // Wait for the intercepted request to complete
            cy.wait('@postParticipate').its('response.statusCode').should('eq', 200);
        });

        it("Unparticipate successfully", () => {
            // Intercept the participate network request
            cy.intercept('DELETE', '**/participate/**', {
                statusCode: 200,
                body: {
                    message: 'Unparticipation successful'
                },
            }).as('deleteParticipate');

            // Show session details
            cy.showSessionDetails({ participate: true });

            // Ensure the participate button is present and click it
            cy.get('button[color="warn"]').should('contain.text', 'Do not participate').click();

            // Wait for the intercepted request to complete
            cy.wait('@deleteParticipate').its('response.statusCode').should('eq', 200);
        });
    });
}