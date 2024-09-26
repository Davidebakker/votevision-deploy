<template>
  <nav id="vue">
    <div class="menu-item"><router-link to="/">Home</router-link></div>
    <div class="menu-item"><router-link to="/about">About</router-link></div>
    <Services title="Services" />
  </nav>
</template>

<script>
import Services from '@/components/NavBarServices.vue'
export default {
  name: 'nav-bar',
  components: {
    Services
  },
}
</script>

<style>
nav {
  width: 100%;
  background-color: #ac0000;
  display: flex;
  align-items: center;
  justify-content: center;
}
nav .menu-item {
  color: #FFF;
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