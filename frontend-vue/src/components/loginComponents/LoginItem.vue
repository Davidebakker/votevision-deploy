<script>
import { ref } from 'vue';
import axios from 'axios'

export default {
  setup() {
    const username = ref('');
    const password = ref('');

    const handleSubmit = async () => {
      const userData = {
        username: username.value,
        password: password.value,
      };

      try {
        const response = await axios.post(`http://localhost:8080/api/auth/login`, userData);
        console.log(response.data);
        alert("Data submitted successfully");
      } catch (error) {
        console.error(error);
      }
    };

    return {
      username,
      password,
      handleSubmit,
    };
  },
};
</script>


<template>
  <div class="Login-form">
    <h1>{{ value }}</h1>
    <form @submit.prevent="handleSubmit">
      <div class="Login">Login</div>

      <div>
        <label for="username">username:</label>
        <input type="username" id="username" v-model="username" required />
      </div>

      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" required />
      </div>

      <button type="submit">Login</button>
    </form>
  </div>
</template>

<style scoped>
.Login-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 1em;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #003049;
  color: #d37e40;
}

.Login-form div {
  margin-bottom: 1em;
}

.Login-form label {
  display: block;
  margin-bottom: 0.5em;
}

.Login-form input {
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

.Login {
  color: #d37e40;
}
</style>
