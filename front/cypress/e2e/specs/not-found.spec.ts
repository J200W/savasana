/**
 * Suite de tests pour la page de not found
 * @description Vérifie que l'utilisateur est redirigé vers la page 404
 * @returns {void}
 */
export default function notFoundSpec() {
    describe('Not found Spec', () => {

        // Test de redirection vers la page 404
        it('Should display not found page', () => {
            cy.login();
            cy.visit('/nimportequoi');
            cy.url().should('contain', '/404');
        });
    });
}