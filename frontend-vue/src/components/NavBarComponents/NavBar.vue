<template>
  <aside
    v-if="windowWidth >= 768"
    class="flex flex-col w-64 h-screen px-4 py-8 overflow-y-auto bg-white border-r dark:bg-gray-900 dark:border-gray-700"
  >
    <div class="flex flex-col items-center mt-6 -mx-2">
<!--      <img-->
<!--        class="object-cover w-24 h-24 mx-2 rounded-full"-->
<!--        src="https://images.unsplash.com/photo-1531427186611-ecfd6d936c79?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"-->
<!--        alt="avatar"-->
<!--      />-->
      <h4 class="mx-2 mt-2 font-medium text-gray-800 dark:text-gray-200">John Doe</h4>
      <p class="mx-2 mt-1 text-sm font-medium text-gray-600 dark:text-gray-400">john@example.com</p>
    </div>

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
          to="/Forum"
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
        <span class="font-medium">log in</span>
      </router-link>

      <router-link
        v-if="isLoggedIn"
        to="/dummy-settings"
        class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">Settings</span>
      </router-link>

      <router-link
        v-if="isLoggedIn"
        to="/dummy-profile"
        class="flex items-center justify-start px-4 py-2 text-gray-600 rounded-lg dark:text-gray-400 hover:bg-gray-200 dark:hover:bg-gray-700"
      >
        <span class="font-medium">Profile</span>
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
    BurgerNav
  },
  data() {
    return {
      windowWidth: window.innerWidth
    }
  },
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('jwtToken'); // Check if the token exists
    }
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    handleResize() {
      this.windowWidth = window.innerWidth
    }
  }
}
</script>

<style scoped>
aside {
  width: 15%;
  background-color: #f8f9fa;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 100vh;
}

nav {
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: flex-start;
  width: 100%;
}

nav > a {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 10px;
  transition: all 0.3s ease;
}

nav > a:hover {
  background-color: black;
}

.space-y-5 > * + * {
  margin-top: 1.5rem;
}

img {
  width: 45%;
  height: auto;
  object-fit: cover;
}
</style>
