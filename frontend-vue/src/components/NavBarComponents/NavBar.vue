<template>
  <nav id="vue" v-if="windowWidth >= 1024">
    <div class="nav-logo"></div>
    <div class="nav-content">
      <div class="menu-item">
        <router-link to="/home">Home</router-link>
      </div>
      <div class="menu-item">
        <router-link to="/about">About</router-link>
      </div>
      <div class="menu-item">
        <router-link to="/registration">Sign up</router-link>
      </div>
      <Results title="Results" />
    </div>
  </nav>

  <nav id="vue" v-else>
    <BurgerNav title="menu" />
  </nav>
</template>

<script>
import Results from '@/components/NavBarComponents/NavBarResults.vue';
import BurgerNav from '@/components/NavBarComponents/BurgerNavBar.vue';

export default {
  name: 'nav-bar',
  components: {
    Results,
    BurgerNav,
  },
  data() {
    return {
      windowWidth: window.innerWidth,
    };
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

<style>
nav {
  width: 100%; /* Use 100% instead of 100vw to avoid overflow */
  background-color: #ac0000;
  display: flex;
  align-items: center;
  justify-content: center;
}

nav .nav-content {
  display: flex;
  align-items: center;
}

nav .menu-item {
  color: #fff;
  padding: 25px 40px;
  position: relative;
  text-align: center;
  border-bottom: 5px solid transparent;
  display: flex;
  transition: 0.4s;
}

nav .menu-item.active,
nav .menu-item:hover {
  background-color: orange;
}

nav .menu-item a {
  color: inherit;
  text-decoration: none;
}
</style>
