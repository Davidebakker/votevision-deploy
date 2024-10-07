<script setup lang="ts">
import { ref } from 'vue';

const name = ref('');
const username = ref('');
const email = ref('');
const password = ref('');
const region = ref('');

const handleSubmit = async () => {
  const userData = {
    name: name.value,
    username: username.value,
    email: email.value,
    password: password.value,
    region: region.value,
  };

  try {
    const response = await fetch('http://localhost:8080/api/users', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });

    if (response.ok) {
      alert('Registration successful!');
    } else {
      alert('Failed to register user.');
    }
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred while registering.');
  }
};
</script>


<template>
  <div class="registration-form">
    <h1>{{ value }}</h1>
    <form @submit.prevent="handleSubmit">
      <div class="registration">Registration</div>
      <div>
        <label for="name">Name:</label>
        <input type="text" id="name" v-model="name" required />
      </div>

      <div>
        <label for="username">Username:</label>
        <input type="text" id="username" v-model="username" required />
      </div>

      <div>
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="email" required />
      </div>

      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" required />
      </div>

      <div>
        <label for="region">Region:</label>
        <input type="text" id="region" v-model="region" required />
      </div>

      <button type="submit">Register</button>
    </form>
  </div>
</template>

<style scoped>
.registration-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 1em;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #003049;
  color: #d37e40;
}

.registration-form div {
  margin-bottom: 1em;
}

.registration-form label {
  display: block;
  margin-bottom: 0.5em;
}

.registration-form input {
  width: 100%;
  padding: 0.5em;
  border: 1px solid #ccc;
  border-radius: 3px;
  color: #c1121f ;
  background-color: #fdf0d5;
}

button {
  padding: 0.7em 1.5em;
  background-color: #f7934c;
  color: #fdf0d5;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #d37e40;
}

.registration {
  color: #d37e40;
}
</style>
