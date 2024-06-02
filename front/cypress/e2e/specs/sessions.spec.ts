
export default function sessionsSpec() {
    describe("Sessions spec", () => {

        it("Display sessions list successfully", () => {
            cy.login();
            cy.get('.m0').should('have.text', 'Rentals available');
        });

        it("Display create and edit buttons when admin", () => {
            cy.login({admin: true});
            cy.get('button[routerLink="create"]').should('contain.text', 'Create');
            cy.get('mat-card-actions > button').should('contain.text', 'Edit');
        });

        it('Don\'t display create and edit buttons when user', () => {
            cy.login();
            cy.get('mat-card-header').should('not.contain.text', 'Create');
            cy.get('mat-card-actions > button').should('not.contain.text', 'Edit');
        });
    });
}