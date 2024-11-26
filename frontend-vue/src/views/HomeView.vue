<template>
  <div class="w-full min-h-screen h-full">
    <div class="flex bg-white h-full">
      <div class="flex items-center text-center lg:text-left px-8 md:px-12 lg:w-1/2 h-full">
        <div>
          <h2 class="text-3xl font-semibold text-gray-800 md:text-4xl">
            Welcome to <span class="text-indigo-600">VoteVision</span>
          </h2>
          <p class="mt-2 text-sm text-gray-500 md:text-base">
           Lets get you started with how the dutch election works and start a conversation from people
            all around the world!!
          </p>
          <div class="flex justify-center lg:justify-start mt-6">
            <router-link to="/result/election-result">

            <span
              class="px-4 py-3 bg-gray-900 text-gray-200 text-xs font-semibold rounded hover:bg-gray-800"
              >Get Started</span
            >
            </router-link>
          </div>
        </div>
      </div>

      <div
        class="hidden lg:block lg:w-1/2 h-full"
        style="clip-path: polygon(10% 0, 100% 0%, 100% 100%, 0 100%)"
      >
        <div
          class="h-full object-cover"
          style="
            background-image: url('src/assets/img/election.png');
            background-size: cover;
            background-position: center;
          "
        >
          <div class="h-full bg-black opacity-25"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
   setup() {
    const isMenuOpen = ref(false)

    const toggleMenu = () => {
      isMenuOpen.value = !isMenuOpen.value
    }

    return {
      isMenuOpen,
      toggleMenu
    }
  }
}
</script>

<style scoped>
html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
}

#app {
  height: 100%;
}
</style>
