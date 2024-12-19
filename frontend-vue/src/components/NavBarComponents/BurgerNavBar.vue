<template>
  <div>
    <button @click="toggleMenu" class="block text-gray-800 hover:text-gray-700 focus:outline-none">
      <svg class="h-6 w-6 fill-current" viewBox="0 0 24 24">
        <path
          v-if="isMenuOpen"
          d="M16.24 14.83a1 1 0 0 1-1.41 1.41L12 13.41l-2.83 2.83a1 1 0 0 1-1.41-1.41L10.59 12 7.76 9.17a1 1 0 0 1 1.41-1.41L12 10.59l2.83-2.83a1 1 0 0 1 1.41 1.41L13.41 12l2.83 2.83z"
        />
        <path
          v-else
          d="M4 5h16a1 1 0 0 1 0 2H4a1 1 0 1 1 0-2zm0 6h16a1 1 0 0 1 0 2H4a1 1 0 1 1 0-2zm0 6h16a1 1 0 0 1 0 2H4a1 1 0 1 1 0-2z"
        />
      </svg>
    </button>

    <transition name="fade" appear>
      <div
        v-if="isMenuOpen"
        class="sub-menu fixed top-0 left-0 right-0 h-2/3 z-50 flex flex-col items-center justify-center"
      >
        <button
          @click="toggleMenu"
          class="absolute top-4 right-4 text-white hover:text-gray-300 focus:outline-none"
        >
          <svg class="h-6 w-6 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path
              d="M6 18L18 6M6 6l12 12"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
          </svg>
        </button>
        <router-link to="/" @click="toggleMenu" class="menu-item">Home</router-link>
        <router-link to="/about" @click="toggleMenu" class="menu-item">About</router-link>
        <router-link to="/parties" @click="toggleMenu" class="menu-item">Parties</router-link>
        <router-link to="/registration" @click="toggleMenu" class="menu-item">Sign up</router-link>
        <router-link to="/forum" @click="toggleMenu" class="menu-item">Forum</router-link>
        <router-link v-if="isLoggedIn" to="/logout" @click="toggleMenu" class="menu-item">Logout</router-link>
        <router-link v-if="isAdmin" to="/admin" @click="toggleMenu" class="menu-item">Admin Page</router-link>
        <router-link v-if="isModerator" to="/moderator/users" @click="toggleMenu" class="menu-item">Manage Users</router-link>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'BurgerNav',
  data() {
    return {
      isMenuOpen: false,
    };
  },
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('jwtToken');
    },
    isAdmin() {
      const roles = localStorage.getItem('userRoles');
      return roles && roles.includes('ROLE_ADMIN');
    },
    isModerator() {
      const roles = localStorage.getItem('userRoles');
      return roles && roles.includes('ROLE_MODERATOR');
    },
  },
  methods: {
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    },
  },
};
</script>

<style scoped>

.menu-item {
  margin: 10px 0;
  text-align: center;
  color: white;
  text-decoration: none;
}

nav {
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: flex-start;
  width: 100%;
  height: 100%;
  color: #3b7feb;
}

.menu-item:hover {
  text-decoration: underline;
}
.sub-menu {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #1f2937;
  color: white;
  z-index: 50;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
