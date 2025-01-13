/// <reference types="cypress" />

describe('Party Overview Page', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/elections/parties', { fixture: 'parties.json' }).as('fetchParties');
    cy.visit('/'); //
    cy.contains('Parties').click();
    cy.wait('@fetchParties');
  });

  it('displays the main heading', () => {
    cy.contains('Parties').should('be.visible');
  });

  it('shows the subtext describing the page', () => {
    cy.contains('View the different parties and their information here').should('be.visible');
  });

  it('displays a list of parties', () => {
    cy.get('div.grid > div').should('have.length.greaterThan', 0);
  });

  it('displays party logos and names', () => {
    cy.get('.router-link-wrapper img').each(($img) => {
      cy.wrap($img).should('be.visible');
      cy.wrap($img).should('have.attr', 'src').and('include', 'logo');
    });

    cy.get('.router-link-wrapper h2').each(($name) => {
      cy.wrap($name).should('be.visible');
    });
  });

  it('navigates to the correct party details page on click', () => {
    cy.get('.router-link-wrapper').first().click();
    cy.location('pathname').should('include', '/party/');
  });
});
