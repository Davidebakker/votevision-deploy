<script setup lang="ts"></script>

<template>
  <main class="container">
    <section class="row election">
      <h2>
        <router-link to="/election-result">Election</router-link>
      </h2>
      <p class="hover-text">The election overview and results...</p>
    </section>
    <section class="row conversations">
      <h2>
        <router-link to="/conversations">Conversations</router-link>
      </h2>
      <p class="hover-text">See what people think about the election and be part of it...</p>
    </section>
    <section class="row about">
      <h2>
        <router-link to="/about">About</router-link>
      </h2>
      <p class="hover-text">Read about what the election process and more about our site...</p>
    </section>
  </main>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: calc(96vh - 60px);
  width: 100%;
  overflow: hidden;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.row {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 33.33%;
  position: relative;
  transition: all 0.3s ease;
  font-size: 2rem;
  color: white;
  font-weight: bold;
  overflow: hidden;
  box-sizing: border-box;
}

.election {
  background-color: red;
}

.conversations {
  background-color: white;
  color: black;
}

.about {
  background-color: blue;
}

.row:hover {
  flex-grow: 2;
}

.container:hover .row {
  flex-grow: 0.5;
}

.hover-text {
  position: absolute;
  bottom: -50px;
  opacity: 0;
  transition:
    opacity 0.3s ease,
    bottom 0.3s ease;
  font-size: 1rem;
  color: #fff;
}

.row:hover .hover-text {
  bottom: 20px;
  opacity: 1;
}

.conversations:hover .hover-text {
  color: black;
}

h2 a {
  cursor: pointer;
  color: inherit;
  text-decoration: none;
  font-size: inherit;
  font-weight: inherit;
  background-color: inherit;
}

body,
html {
  margin: 0;
  padding: 0;
  overflow-x: hidden;
  height: 100%;
  box-sizing: border-box;
}
</style>
