import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from '../views/AboutView.vue'
import ElectionResultView from '../views/results/ElectionResultView.vue'
import Registration from '@/components/RegistrationComponents/RegistrationItem.vue'
import Login from '@/components/loginComponents/LoginItem.vue'
import Logout from '@/components/loginComponents/LogoutItem.vue'
import PartiesOverview from '@/components/PartyOverviewComponents/PartiesOverview.vue'
import Candidate from '@/components/Elections/TestCandidateItem.vue'
import PostForum from "@/components/ForumComponents/PostForum.vue";
import ForumItem from "@/components/ForumComponents/ForumItem.vue";
import AdminHomeItem from '@/components/managment/AdminComponents/AdminHomeItem.vue'
import UserManagementItem from '@/components/managment/AdminComponents/UserManagementItem.vue'
import UnauthorizedItem from '@/components/unauthorizedComponents/UnauthorizedItem.vue'
import PartyDetails from '@/components/PartyOverviewComponents/PartyDetails.vue'
import AdminManagementItem from '@/components/managment/ModComponents/AdminManagementItem.vue'
import ModeratorHomeItem from '@/components/managment/ModComponents/ModeratorHomeItem.vue'
import ProfilePageComponent from '@/components/profileComponents/ProfilePageComponent.vue'

function isLoggedIn() {
  return !!localStorage.getItem('jwtToken');
}

function isAdmin() {
  const userRoles = localStorage.getItem('userRoles');
  return userRoles && userRoles.includes('ROLE_ADMIN') || userRoles.includes('ROLE_MODERATOR');
}

function isMod() {
  const userRoles = localStorage.getItem('userRoles');
    return userRoles && userRoles.includes('ROLE_MODERATOR');
}

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/about', name: 'about', component: AboutView },
    { path: '/result/election-result', name: 'election-result', component: ElectionResultView, meta: { userOnly: true } },
    // Authentication
    { path: '/registration', name: 'registration', component: Registration, meta: { guestOnly: true } },
    { path: '/login', name: 'login', component: Login, meta: { guestOnly: true } },
    { path: '/logout', name: 'logout', component: Logout, meta: { userOnly: true } },
    { path: '/profile', name: 'profile', component: ProfilePageComponent, meta: { userOnly: true }},
    // Elections
    { path: '/party/:name', component: PartyDetails, name: 'PartyDetails', props: true, },
    { path: '/kandidaat/:candidateName', name: 'candidate', component: Candidate, props: true },
    { path: '/parties', name: 'parties', component: PartiesOverview },
    { path: '/forum', name: 'ForumItem', component: ForumItem },
    { path: '/onderwerp/:onderwerpNummer', name: 'ForumPost', component: PostForum, meta: { userOnly: true } },
    // Admin
    { path: '/moderator', name: 'Moderator', component: AdminHomeItem, meta: { adminOnly: true } },
    { path: '/moderator/users', name: 'userManagement', component: UserManagementItem, meta: { adminOnly: true } },
    // Moderator
    { path: '/admin', name: 'Moderator', component: ModeratorHomeItem, meta: { modOnly: true } },
    { path: '/admin/moderators', name: 'moderatorManagement', component: AdminManagementItem, meta: { modOnly: true } },
    // Unauthorized
    { path: '/unauthorized', name: 'unauthorized', component: UnauthorizedItem },
  ]
});

// Global navigation guard to handle route access
router.beforeEach((to, from, next) => {
  const userOnly = to.matched.some(record => record.meta.userOnly);
  const guestOnly = to.matched.some(record => record.meta.guestOnly);
  const adminOnly = to.matched.some(record => record.meta.adminOnly);
  const modOnly = to.matched.some(record => record.meta.modOnly);

  if (userOnly && !isLoggedIn()) {
    next({ name: 'login' });
  } else if (guestOnly && isLoggedIn()) {
    next({ name: 'home' });
  } else if (adminOnly && (!isLoggedIn() || !isAdmin())) {
    next({ name: 'unauthorized' });
  } else if (modOnly && (!isLoggedIn() || !isMod())) {
    next({ name: 'unauthorized' });
  } else {
    next();
  }
});

export default router;
