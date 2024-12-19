<template>
  <div>
    <!-- Desktop Sidebar (md and up) -->
    <aside
      v-if="windowWidth >= 768"
      class="hidden md:flex flex-col w-64 bg-white border-r border-gray-200 dark:bg-gray-900 dark:border-gray-700 h-screen sticky top-0"
    >
      <nav class="mt-10 px-4 space-y-2">
        <router-link
          to="/"
          class="flex items-center px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 dark:bg-gray-800 dark:text-gray-200 dark:hover:bg-gray-700"
        >
          Home
        </router-link>
        <router-link
          to="/about"
          class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
        >
          About
        </router-link>
        <router-link
          to="/parties"
          class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
        >
          Parties
        </router-link>

        <template v-if="!isLoggedIn">
          <router-link
            to="/registration"
            class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
          >
            Sign up
          </router-link>
          <router-link
            to="/login"
            class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
          >
            Log in
          </router-link>
        </template>

        <template v-else>
          <router-link
            to="/forum"
            class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
          >
            Forum
          </router-link>
          <router-link
            to="/logout"
            class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
          >
            Logout
          </router-link>

          <!-- Admin or Moderator -->
          <template v-if="isAdmin || isModerator">
            <router-link
              to="/moderator/users"
              class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
            >
              Manage users
            </router-link>
          </template>

          <!-- Moderator only -->
          <template v-if="isModerator">
            <router-link
              to="/admin"
              class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
            >
              Admin page
            </router-link>
            <router-link
              to="/admin/moderators"
              class="flex items-center px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
            >
              Manage moderators
            </router-link>
          </template>
        </template>
      </nav>
    </aside>

    <!-- Mobile Top Bar (below md) -->
    <div v-else class="md:hidden flex items-center justify-between bg-white border-b border-gray-200 py-3 px-4 dark:bg-gray-900 dark:border-gray-700">
      <div class="flex items-center space-x-2">
        <span class="font-bold text-gray-800 dark:text-gray-200">Menu</span>
      </div>
      <button @click="toggleMenu" class="text-gray-800 dark:text-gray-200 focus:outline-none">
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
    </div>

    <!-- Mobile slide-out menu -->
    <transition name="slide-fade">
      <div v-if="isMenuOpen && windowWidth < 768" class="fixed inset-0 flex z-50">
        <!-- Backdrop -->
        <div class="fixed inset-0 bg-black bg-opacity-50" @click="toggleMenu"></div>

        <!-- Slide out menu -->
        <div class="relative bg-white w-64 max-w-full p-6 overflow-auto dark:bg-gray-900 h-screen">
          <div class="flex items-center justify-between mb-4">
            <span class="font-bold text-gray-800 dark:text-gray-200">Menu</span>
            <button @click="toggleMenu" class="text-gray-800 dark:text-gray-200 focus:outline-none">
              <svg class="h-6 w-6 fill-current" viewBox="0 0 24 24">
                <path
                  d="M16.24 14.83a1 1 0 0 1-1.41 1.41L12 13.41l-2.83 2.83a1 1 0 0 1-1.41-1.41L10.59 12 7.76 9.17a1 1 0 0 1 1.41-1.41L12 10.59l2.83-2.83a1 1 0 0 1 1.41 1.41L13.41 12l2.83 2.83z"
                />
              </svg>
            </button>
          </div>
          <nav class="space-y-2">
            <router-link
              to="/"
              class="block px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 dark:bg-gray-800 dark:text-gray-200 dark:hover:bg-gray-700"
              @click="toggleMenu"
            >
              Home
            </router-link>
            <router-link
              to="/about"
              class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
              @click="toggleMenu"
            >
              About
            </router-link>
            <router-link
              to="/parties"
              class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
              @click="toggleMenu"
            >
              Parties
            </router-link>

            <template v-if="!isLoggedIn">
              <router-link
                to="/registration"
                class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
                @click="toggleMenu"
              >
                Sign up
              </router-link>
              <router-link
                to="/login"
                class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
                @click="toggleMenu"
              >
                Log in
              </router-link>
            </template>

            <template v-else>
              <router-link
                to="/forum"
                class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
                @click="toggleMenu"
              >
                Forum
              </router-link>
              <router-link
                to="/logout"
                class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
                @click="toggleMenu"
              >
                Logout
              </router-link>

              <template v-if="isAdmin || isModerator">
                <router-link
                  to="/moderator/users"
                  class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
                  @click="toggleMenu"
                >
                  Manage users
                </router-link>
              </template>

              <template v-if="isModerator">
                <router-link
                  to="/admin"
                  class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
                  @click="toggleMenu"
                >
                  Admin page
                </router-link>
                <router-link
                  to="/admin/moderators"
                  class="block px-4 py-2 text-gray-600 rounded-lg hover:bg-gray-200 dark:text-gray-300 dark:hover:bg-gray-700"
                  @click="toggleMenu"
                >
                  Manage moderators
                </router-link>
              </template>
            </template>
          </nav>
        </div>
      </div>
    </transition>
  </div>
</template>


<script>
export default {
  name: 'NavBar',
  data() {
    return {
      windowWidth: window.innerWidth,
      isMenuOpen: false
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
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    }
  }
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
.slide-fade-enter-active, .slide-fade-leave-active {
  transition: transform 0.3s ease, opacity 0.3s ease;
}
.slide-fade-enter-from, .slide-fade-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}
</style>
