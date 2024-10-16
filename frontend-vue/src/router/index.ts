import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ElectionResultView from '../views/results/ElectionResultView.vue'
import NationalElectionResultView from '../views/results/NationalElectionResultView.vue'
import Registration from '@/components/RegistrationComponents/RegistrationItem.vue'
import Login from '@/components/loginComponents/LoginItem.vue'
import Logout from '@/components/loginComponents/LogoutItem.vue'
import Party from '@/components/Elections/TestPartyItem.vue'
import Candidate from '@/components/Elections/TestCandidateItem.vue'

// Utility function to check if the user is logged in
function isLoggedIn() {
  return !!localStorage.getItem('jwtToken');
}

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/result/election-result',
      name: 'election-result',
      component: ElectionResultView,
      meta: { userOnly: true }
    },
    {
      path: '/result/National-election-result',
      name: 'national-election-result',
      component: NationalElectionResultView,
      meta: { userOnly: true } 
    },
    // Authentication
    {
      path: '/registration',
      name: 'registration',
      component: Registration,
      meta: { guestOnly: true } 
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: { guestOnly: true } 
    },
    {
      path: '/logout',
      name: 'logout',
      component: Logout,
      meta: { userOnly: true }
    },
    // Elections
    {
      path: '/party/:partyName',
      name: 'party',
      component: Party,
      props: true
    },
    {
      path: '/candidate/:candidateName',
      name: 'candidate',
      component: Candidate,
      props: true
    },
  ]
});

// Global navigation guard to handle route access
router.beforeEach((to, from, next) => {
  const userOnly = to.matched.some(record => record.meta.userOnly);
  const guestOnly = to.matched.some(record => record.meta.guestOnly);

  if (userOnly && !isLoggedIn()) {
    next({ name: 'login' });
  } else if (guestOnly && isLoggedIn()) {
    next({ name: 'home' });
  } else {
    next();
  }
});

export default router;
