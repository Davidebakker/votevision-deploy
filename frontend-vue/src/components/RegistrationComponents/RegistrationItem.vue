<script>
import { ref } from 'vue';
import axios from 'axios'

export default {
  setup() {
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
        const response = await axios.post('http://localhost:8080/api/auth/signup', userData);
        console.log(response.data);
        alert(response.data.message || "Data submitted successfully");
      } catch (error) {
        console.error(error);
        if (error.response && error.response.data && error.response.data.message) {
          alert(error.response.data.message); // Display the specific error message
        } else {
          alert("An unexpected error occurred.");
        }
      }
    };

    return {
      name,
      username,
      email,
      password,
      region,
      handleSubmit,
    };
  },
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
