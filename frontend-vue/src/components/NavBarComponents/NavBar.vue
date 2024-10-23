<template>
  <aside
    v-if="windowWidth >= 768"
    class="sticky-sidebar flex flex-col w-64 px-4 py-8 bg-white dark:bg-gray-900 dark:border-gray-700 border-r"
  >
    <nav class="flex flex-col justify-start flex-1 mt-6 space-y-5">
      <router-link
        to="/"
        class="flex items-center justify-start px-4 py-2 text-gray-700 bg-gray-100 rounded-lg dark:bg-gray-800 dark:text-gray-200 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">Home</span>
      </router-link>

      <router-link
        to="/about"
        class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">About</span>
      </router-link>

      <router-link
        to="/parties"
        class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">Parties</span>
      </router-link>

      <router-link
        to="/forum"
        class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">Forum</span>
      </router-link>

      <router-link
        v-if="!isLoggedIn"
        to="/registration"
        class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">Sign up</span>
      </router-link>

      <router-link
        v-if="!isLoggedIn"
        to="/login"
        class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">Log in</span>
      </router-link>

      <router-link
        v-if="isLoggedIn"
        to="/logout"
        class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">Logout</span>
      </router-link>
    </nav>
  </aside>
  <nav v-else>
    <BurgerNav title="Menu" />
  </nav>
</template>

<script>
import BurgerNav from '@/components/NavBarComponents/BurgerNavBar.vue';

export default {
  name: 'NavBar',
  components: {
    BurgerNav,
  },
  data() {
    return {
      windowWidth: window.innerWidth,
    };
  },
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('jwtToken'); // Check if the token exists
    },
  },
  mounted() {
    window.addEventListener('resize', this.handleResize);
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.handleResize);
  },
  methods: {
    handleResize() {
      this.windowWidth = window.innerWidth;
    },
  },
};
</script>

<style scoped>
.sticky-sidebar {
  position: sticky;
  top: 0; /* Sticks the sidebar at the top when scrolling down */
  height: 100vh; /* Makes sure the sidebar covers the entire viewport height */
  max-height: 100vh;
  width: 15%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  background-color: #f8f9fa;
}

nav {
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: flex-start;
  width: 100%;
  height: 100%;
}

nav > a {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 10px;
  transition: all 0.3s ease;
}

nav > a:hover {
  background-color: #4b5563;
}
</style>
