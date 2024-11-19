<script>
import { ref } from 'vue';
import axios from 'axios'


import { useRouter } from 'vue-router'
import { toast } from "react-toastify";

export default {
  setup() {
    const router = useRouter();
    const username = ref('');
    const password = ref('');

    const handleSubmit = async () => {
      const userData = {
        username: username.value,
        password: password.value,
      };

      try {
        const response = await axios.post(
          `http://localhost:8080/api/auth/login`,
          userData,
          {
            withCredentials: true,
            headers: {
              'Content-Type': 'application/json',
            },
          });
        console.log(response.data);

        toast("You successfully logged in");

        await router.push({ name: 'home' });
        window.location.reload();
      } catch (error) {
        if (error.response && error.response.status === 403) {
          alert("Your account is banned!");
        } else {
          alert("Your email or password is wrong!");
        }
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
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-sm mx-auto overflow-hidden bg-white rounded-lg shadow-md dark:bg-gray-800">
      <div class="px-6 py-4">
        <h3 class="mt-3 text-xl font-medium text-center text-gray-600 dark:text-gray-200">
          Login
        </h3>

        <form @submit.prevent="handleSubmit">
          <div class="w-full mt-4">
            <input
              class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              type="text"
              placeholder="Username"
              v-model="username"
              required
            />
          </div>

          <div class="w-full mt-4">
            <input
              class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              type="password"
              placeholder="Password"
              v-model="password"
              required
            />
          </div>

          <div class="flex items-center justify-between mt-4">
            <button
              class="flex center px-6 py-2 text-sm font-medium tracking-wide text-white capitalize transition-colors duration-300 transform bg-blue-500 rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
              type="submit"
            >
              Login
            </button>
          </div>
        </form>
      </div>

      <div class="flex items-center justify-center py-4 text-center bg-gray-50 dark:bg-gray-700">
        <span class="text-sm text-gray-600 dark:text-gray-200">Don't have an account? </span>
        <a href="#" class="mx-2 text-sm font-bold text-blue-500 dark:text-blue-400 hover:underline">
          Register
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
