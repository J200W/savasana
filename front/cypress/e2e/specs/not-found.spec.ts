
export default function notFoundSpec() {
    describe('Not found Spec', () => {

        it('Should display not found page', () => {
            cy.login();
            cy.visit('/nimportequoi');
            cy.url().should('contain', '/404');
        });
    });
}