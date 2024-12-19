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
      <template v-if="!isLoggedIn">
        <router-link
          to="/registration"
          class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
        >
          <span class="font-medium">Sign up</span>
        </router-link>
        <router-link
          to="/login"
          class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
        >
          <span class="font-medium">Log in</span>
        </router-link>
      </template>
      <template v-else>
        <router-link
          to="/forum"
          class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
        >
          <span class="font-medium">Forum</span>
        </router-link>
        <router-link
          to="/logout"
          class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
        >
          <span class="font-medium">Logout</span>
        </router-link>
        <template v-if="isAdmin || isModerator">
          <router-link
            to="/moderator/users"
            class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
          >
            <span class="font-medium">Manage users</span>
          </router-link>
        </template>
        <template v-if="isModerator">
          <router-link
            to="/admin"
            class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
          >
            <span class="font-medium">Admin page</span>
          </router-link>
          <router-link
            to="/admin/moderators"
            class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
          >
            <span class="font-medium">Manage moderators</span>
          </router-link>
        </template>
      </template>
    </nav>
  </aside>
  <nav v-else class="flex justify-center items-center bg-white w-full h-screen">
    <BurgerNav title="Menu" />
  </nav>
</template>


<script>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import BurgerNav from '@/components/NavBarComponents/BurgerNavBar.vue';
// import { getCookie } from '@/stores/cookies.ts';

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
      return !!localStorage.getItem('jwtToken');
    },
    isAdmin() {
      const userRoles = localStorage.getItem('userRoles');
      return userRoles && userRoles.includes('ROLE_ADMIN');
    },
    isModerator() {
      const userRoles = localStorage.getItem('userRoles');
      return userRoles && userRoles.includes('ROLE_MODERATOR');
    }
  },
  mounted() {
    window.addEventListener('resize', this.handleResize);
    this.handleResize();
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

onMounted(() => {
  window.addEventListener('resize', handleResize);
  jwt.value = localStorage.getItem('jwt');
  userRoles.value = localStorage.getItem('userRoles');
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>

.sticky-sidebar {
  position: sticky;
  top: 0;
  height: 100vh;
  max-height: 100vh;
  width: 15%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  background-color: #1f2937;
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
  color: #3b7feb;
}

nav > a:hover {
  background-color: #4b5563;
}

.avatar {
  background-color: #0c0c0c;
  width: 100%;
  border-radius: 20px;
}
</style>
