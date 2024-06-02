
export default function loginSpec() {
    describe('Login spec', () => {

        it('Login successfully', () => {
            cy.login();
            cy.url().should('include', '/sessions')
        })

        it('Login failed - error credentials', () => {
            cy.visit('/login')
            cy.get('input[formControlName=email]').type("nimportequoi@gmail.com")
            cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
            cy.get('.error').should('contain', 'An error occurred')
        });

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