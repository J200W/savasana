/**
 * Suite de tests pour la page de login
 * @description Vérifie que l'utilisateur peut se connecter avec succès,
 * que l'utilisateur ne peut pas se connecter avec des identifiants incorrects
 * et que l'utilisateur ne peut pas se connect
 * @returns {void}
 */
export default function loginSpec() {
    describe('Login spec', () => {

        // Test de connexion réussie
        it('Login successfully', () => {
            cy.login();
            cy.url().should('include', '/sessions')
        })

        // Test de connexion échouée - mauvais identifiants
        it('Login failed - error credentials', () => {
            cy.visit('/login')
            cy.get('input[formControlName=email]').type("nimportequoi@gmail.com")
            cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
            cy.get('.error').should('contain', 'An error occurred')
        });

        // Test de connexion échouée - champ manquant
        it('Login failed - missing field', () => {
            cy.visit('/login')

            cy.intercept('POST', '/api/auth/login', {
                statusCode: 500,
                body: {
                    message: 'Server error'
                },
            })

            cy.get('input[formControlName=email]').type(" ")
            cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
            cy.get('.error').should('contain', 'An error occurred')
        });
    })
}