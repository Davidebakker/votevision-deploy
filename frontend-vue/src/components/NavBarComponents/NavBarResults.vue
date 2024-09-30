<template>
  <div class="menu-item">
    <a href="#">
      {{ title }}
    </a>
    <transition name="fade" appear>
      <div class="sub-menu">
        <div class="menu-item">
          <router-link to="/result/election-result">Election</router-link>
        </div>
        <div class="menu-item">
          <router-link to="/result/national-election-result">National Election</router-link>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'nav-bar-results',
  props: ['title'],
};
</script>

<style scoped>
.menu-item {
  position: relative;
}

.sub-menu {
  position: absolute;
  background-color: #c1121f;
  top: calc(100% + 5px);
  left: 50%;
  transform: translateX(-50%);
  width: max-content;
  display: none;
  z-index: 10;
}

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
</style>
