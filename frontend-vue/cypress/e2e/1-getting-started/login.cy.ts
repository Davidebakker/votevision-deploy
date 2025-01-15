// cypress/e2e/login.spec.js

describe('Login Page Tests', () => {
  const baseUrl = 'http://localhost:3000'; // Adjust if the frontend runs on a different port
  const apiEndpoint = 'http://localhost:8080/api/auth/login';

  beforeEach(() => {
    cy.visit(`${baseUrl}/login`);
  });

  it('should display login form', () => {
    cy.get('input[placeholder="Username"]').should('be.visible');
    cy.get('input[placeholder="Password"]').should('be.visible');
    cy.get('button[type="submit"]').should('be.visible');
  });

  it('should show an error for empty fields', () => {
    cy.get('button[type="submit"]').click();
    cy.on('window:alert', (text) => {
      expect(text).to.contain('Your email or password is wrong!');
    });
  });

  it('should allow a user to log in with valid credentials', () => {
    cy.intercept('POST', apiEndpoint, {
      statusCode: 200,
      body: {
        token: 'sample-jwt-token',
        roles: ['ROLE_USER']
      },
    }).as('loginRequest');

    cy.get('input[placeholder="Username"]').type('bob');
    cy.get('input[placeholder="Password"]').type('123456');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginRequest').its('request.body').should('deep.equal', {
      username: 'bob',
      password: '123456',
    });

    cy.url().should('eq', `${baseUrl}/`);
    cy.getLocalStorage('jwtToken').should('equal', 'sample-jwt-token');
    cy.getLocalStorage('userRoles').should('equal', JSON.stringify(['ROLE_USER']));
  });

  it('should display an error for invalid credentials', () => {
    cy.intercept('POST', apiEndpoint, {
      statusCode: 401,
      body: { message: 'Invalid username or password' },
    }).as('loginRequest');

    cy.get('input[placeholder="Username"]').type('cypress');
    cy.get('input[placeholder="Password"]').type('cypress1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginRequest');
    cy.on('window:alert', (text) => {
      expect(text).to.contain('Your email or password is wrong!');
    });
  });

  it('should handle a banned account', () => {
    cy.intercept('POST', apiEndpoint, {
      statusCode: 403,
      body: { message: 'Your account is banned!' },
    }).as('loginRequest');

    cy.get('input[placeholder="Username"]').type('barry');
    cy.get('input[placeholder="Password"]').type('123456');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginRequest');
    cy.on('window:alert', (text) => {
      expect(text).to.contain('Your account is banned!');
    });
  });
});
