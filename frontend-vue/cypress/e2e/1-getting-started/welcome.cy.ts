/// <reference types="cypress" />

describe('Landing Page: VoteVision', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('displays the main heading', () => {
    cy.contains('Welcome to VoteVision').should('be.visible')
  })

  it('shows the subtext about the Dutch election', () => {
    cy.contains(
      'Lets get you started with how the dutch election works and start a conversation from people all around the world!!'
    ).should('be.visible')
  })

  it('has a "Get Started" button/link that leads to /result/election-result', () => {
    cy.get('a[href="/result/election-result"]')
      .should('exist')
      .and('contain.text', 'Get Started')
  })

  it('navigates to /result/election-result on "Get Started" click', () => {
    cy.get('a[href="/result/election-result"]').click()

    cy.location('pathname').should('eq', '/result/election-result')
  })

  it('displays the correct background image', () => {
    cy.get('div[style*="src/assets/img/election.png"]').should('exist')
  })
})
