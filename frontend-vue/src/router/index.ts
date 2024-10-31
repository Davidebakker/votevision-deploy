import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ElectionResultView from '../views/results/ElectionResultView.vue'
import NationalElectionResultView from '../views/results/NationalElectionResultView.vue'
import Registration from '@/components/RegistrationComponents/RegistrationItem.vue'
import Login from '@/components/loginComponents/LoginItem.vue'
import Logout from '@/components/loginComponents/LogoutItem.vue'
import PartiesOverview from '@/components/PartyOverviewComponents/PartiesOverview.vue'
import Party from '@/components/Elections/TestPartyItem.vue'
import Candidate from '@/components/Elections/TestCandidateItem.vue'
import PostForum from "@/components/ForumComponents/PostForum.vue";
import ForumItem from "@/components/ForumComponents/ForumItem.vue";

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
      path: '/partij/:partyName',
      name: 'party',
      component: Party,
      props: true
    },
    {
      path: '/kandidaat/:candidateName',
      name: 'candidate',
      component: Candidate,
      props: true
    },
    {
      path: '/parties',
      name: 'parties',
      component: PartiesOverview,

    },
    {
      path: '/forum',
      name: 'ForumItem',
      component: ForumItem,
      meta: { userOnly: true }
    },
    {
      path:'/onderwerp/:onderwerpNummer',
      name:'ForumPost',
      component: PostForum,
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
