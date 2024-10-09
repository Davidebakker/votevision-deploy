<script>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router'

export default {
  setup() {
    const router = useRouter();
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

        if (response.data.token) {
          const jwtToken = response.data.token;
          localStorage.setItem('jwtToken', jwtToken);
          axios.defaults.headers.common['Authorization'] = `Bearer ${jwtToken}`;
        }

        alert(response.data.message || "Data submitted successfully");

        router.push({ name: 'home' });
        window.location.reload();
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
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-sm mx-auto overflow-hidden bg-white rounded-lg shadow-md dark:bg-gray-800">
      <div class="px-6 py-4">
        <div class="flex justify-center mx-auto">
        </div>

        <h3 class="mt-3 text-xl font-medium text-center text-gray-600 dark:text-gray-200">
          Register
        </h3>

        <form @submit.prevent="handleSubmit">
          <div class="w-full mt-4">
            <input
              class="block w-full px-4 py-2 mt-2 text-gray-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              type="text"
              placeholder="Name"
              v-model="name"
              required
            />
          </div>

          <div class="w-full mt-4">
            <input
              class="block w-full px-4 py-2 mt-2 text-gray-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              type="text"
              placeholder="Username"
              v-model="username"
              required
            />
          </div>

          <div class="w-full mt-4">
            <input
              class="block w-full px-4 py-2 mt-2 text-gray-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              type="email"
              placeholder="Email"
              v-model="email"
              required
            />
          </div>

          <div class="w-full mt-4">
            <input
              class="block w-full px-4 py-2 mt-2 text-gray-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              type="password"
              placeholder="Password"
              v-model="password"
              required
            />
          </div>

          <div class="w-full mt-4">
            <input
              class="block w-full px-4 py-2 mt-2 text-gray-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              type="text"
              placeholder="Region"
              v-model="region"
              required
            />
          </div>

          <div class="flex items-center justify-between mt-4">
            <button
              class="flex center px-6 py-2 text-sm font-medium tracking-wide text-white capitalize transition-colors duration-300 transform bg-blue-500 rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
              type="submit"
            >
              Register
            </button>
          </div>
        </form>
      </div>

      <div class="flex items-center justify-center py-4 text-center bg-gray-50 dark:bg-gray-700">
        <span class="text-sm text-gray-600 dark:text-gray-200">Already have an account? </span>
        <a href="#" class="mx-2 text-sm font-bold text-blue-500 dark:text-blue-400 hover:underline">
          Log In
        </a>
      </div>
    </div>
  </div>
</template>

<style scoped>
.min-h-screen {
  min-height: 100vh;
}

.bg-gray-100 {
  background-color: #111827;
}
</style>
