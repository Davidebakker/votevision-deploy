<template>
  <div class="menu-item">
    <a href="#">
      {{ title }}
    </a>
    <transition name="fade" appear>
      <div class="sub-menu">
        <div class="nav-logo"></div>
        <div class="nav-content">
          <div class="menu-item">
            <router-link to="/frontend-vue/public">Home</router-link>
          </div>
          <div class="menu-item">
            <router-link to="/about">About</router-link>
          </div>
          <Results title="Results" />
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Results from '@/components/NavBarComponents/NavBarResults.vue';

export default {
  name: 'burger-nav',
  components: { Results },
  props: ['title'],
};
</script>

<style scoped>
.menu-item {
  position: relative;
}

/* Hide the submenu by default */
.menu-item > .sub-menu {
  position: absolute;
  background-color: #c1121f;
  top: calc(100% + 5px);
  left: 50%;
  transform: translateX(-50%);
  width: max-content;
  display: none;
  z-index: 10;
}

/* Show the immediate child submenu on hover */
.menu-item:hover > .sub-menu {
  display: block;
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s ease-out;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.sub-menu {
  cursor: pointer;
}
</style>
