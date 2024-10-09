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
          d="M4 5h16a1 1 0 0 1 0 2H4a1 1 0 1 1 0-2zm0 6h16a1 1 0 0 1 0 2H4a1 1 0 0 1 0-2zm0 6h16a1 1 0 0 1 0 2H4a1 1 0 1 1 0-2z"
        />
      </svg>
    </button>
    <transition name="fade" appear>
      <div v-if="isMenuOpen" class="sub-menu mt-4">
        <div class="menu-item">
          <router-link to="/home">Home</router-link>
        </div>
        <div class="menu-item">
          <router-link to="/about">About</router-link>
        </div>
        <div class="menu-item">
          <router-link to="/registration">Sign up</router-link>
        </div>
        <div class="menu-item">
          <router-link to="/dummy-settings">Settings</router-link>
        </div>
        <div class="menu-item">
          <router-link to="/dummy-profile">Profile</router-link>
        </div>
        <div class="menu-item">
          <router-link to="/dummy-logout">Logout</router-link>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'BurgerNav',
  data() {
    return {
      isMenuOpen: false
    }
  },
  methods: {
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen
    }
  }
}
</script>

<style scoped>
.menu-item {
  margin: 10px 0;
  text-align: center;
}

.sub-menu {
  background-color: #0c0c0c;
  padding: 10px;
  border-radius: 5px;
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
