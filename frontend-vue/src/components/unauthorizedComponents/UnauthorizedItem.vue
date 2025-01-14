<!-- UnauthorizedPage.vue -->
<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

// Function to navigate to the login page
const goToLogin = () => {
  router.push('/login'); // Adjust the path if your login route is different
};
</script>

<template>
  <div class="unauthorized-container">
    <h1>Unauthorized Access</h1>
    <p>You do not have permission to view this page. Please log in to continue.</p>
    <button @click="goToLogin">Go to Login</button>
  </div>
</template>

<style scoped>
.unauthorized-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f9f9f9;
  color: #333;
  text-align: center;
}

h1 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
}

button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  color: white;
  background-color: #007bff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
