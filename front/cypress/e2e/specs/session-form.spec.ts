/**
 * Suite de tests pour la page de détail de session
 * @description Vérifie que l'utilisateur peut voir les détails de la session,
 * que l'utilisateur peut participer à la session et que l'utilisateur peut ne pas participer à la session
 * @returns {void}
 */
export default function sessionFormSpec() {
    describe('Session Form spec', () => {

        // Avant chaque test, on se connecte et on intercepte les requêtes
        beforeEach(() => {
            cy.login({admin: true});
            cy.intercept('GET', '/api/teacher', {
                fixture: 'teachers.res.json'
            }).as('teachers');
            cy.intercept('POST', '/api/session', {
                fixture: 'session-participated.res.json'
            }).as('sessionCreate');
        });

        // Test de la création d'une session
        it('should create a session successfully', () => {
            cy.get('button[routerlink="create"]').click();
            cy.get('input[formcontrolname="name"]').type('session 1');
            cy.get('input[formcontrolname="date"]').type('2024-06-10');
            cy.get('mat-select[formcontrolname="teacher_id"]').click();
            cy.get('mat-option').contains('Margot DELAHAYE').click();
            cy.get('textarea[formcontrolname="description"]').type('my description');
            cy.get('button[type="submit"]').click();
            cy.get('snack-bar-container').should('contain', 'Session created');
        });

        // Test de la mise à jour d'une session
        it('should edit a session successfully', () => {
            cy.intercept('GET', '/api/session/*', {
                fixture: 'session.res.json'
            }).as('session-get');
            cy.intercept('PUT', '/api/session/*', {
                fixture: 'session.res.json'
            }).as('session');
            cy.contains('button', 'Edit').click();
            cy.get('input[formcontrolname="name"]').clear().type('session 1 updated')
            cy.get('button[type="submit"]').click();
            cy.get('snack-bar-container').should('contain', 'Session updated');
        });
    })
}