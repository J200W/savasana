/**
 * Suite de tests pour la page de détail de session
 * @description Vérifie que l'utilisateur peut voir les détails de la session,
 * que l'utilisateur peut participer à la session et que l'utilisateur peut ne pas participer à la session
 * @returns {void}
 */
export default function sessionsSpec() {
    describe("Sessions spec", () => {

        // Test de l'affichage de la liste des sessions - admin ou utilisateur
        it("Display sessions list successfully", () => {
            cy.login();
            cy.get('.m0').should('have.text', 'Rentals available');
        });

        // Test de l'affichage des boutons de création et d'édition - admin
        it("Display create and edit buttons when admin", () => {
            cy.login({admin: true});
            cy.get('button[routerLink="create"]').should('contain.text', 'Create');
            cy.get('mat-card-actions > button').should('contain.text', 'Edit');
        });

        // Test de la dissimulation des boutons de création et d'édition - utilisateur
        it('Don\'t display create and edit buttons when user', () => {
            cy.login();
            cy.get('mat-card-header').should('not.contain.text', 'Create');
            cy.get('mat-card-actions > button').should('not.contain.text', 'Edit');
        });
    });
}