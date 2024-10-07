import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ElectionResultView from '../views/results/ElectionResultView.vue'
import NationalElectionResultView from '../views/results/NationalElectionResultView.vue'
import Registration from '@/components/RegistrationComponents/Registration.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/home',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/result/election-result',
      name: 'election-result',
      component: ElectionResultView
    },
    {
      path: '/result/National-election-result',
      name: 'national-election-result',
      component: NationalElectionResultView
    },
    {
      path: '/registration',
      name: 'registration',
      component: Registration
    }

  ]
})

export default router
