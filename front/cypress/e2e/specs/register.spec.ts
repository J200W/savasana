/**
 * Suite de tests pour la page de register
 * @description Vérifie que l'utilisateur peut s'inscrire avec succès,
 * que l'utilisateur ne peut pas s'inscrire avec des identifiants incorrects
 * et que l'utilisateur ne peut pas s'inscrire
 * @returns {void}
 */
export default function registerSpec() {
    describe('Register spec', () => {

        // Test d'inscription réussie
        it('Register successfully', () => {
            cy.visit('/register');

            cy.intercept('POST', '/api/auth/register', {
                body: {
                    id: 1,
                    username: 'userName',
                    firstName: 'firstName',
                    lastName: 'lastName',
                    admin: true
                },
            });

            cy.fillRegistrationForm({
                firstName: 'firstName',
                lastName: 'lastName',
                email: 'yoga@studio.com',
                password: 'test!1234'
            });

            cy.url().should('include', '/login');
        });

        // Test d'inscription échouée - mauvais identifiants
        it('Register failed - error credentials', () => {
            cy.visit('/register');

            cy.intercept('POST', '/api/auth/register', {
                statusCode: 401,
                body: {
                    message: 'Invalid credentials'
                },
            });

            cy.fillRegistrationForm({
                firstName: 'firstName',
                lastName: 'lastName',
                email: 'nimportequoi@gmail.com',
                password: 'te'
            });

            cy.get('.error').should('contain', 'An error occurred');
        });

        // Test d'inscription échouée - champ manquant
        it('Register failed - missing field', () => {
            cy.visit('/register');

            cy.intercept('POST', '/api/auth/register', {
                statusCode: 500,
                body: {
                    message: 'Server error'
                },
            });

            cy.fillRegistrationForm({
                firstName: ' ',
                lastName: 'lastname',
                email: 'nimportequoi@gmail.com',
                password: 'test!1234'
            });

            cy.get('.error').should('contain', 'An error occurred');
        });
    });
}