<script>
import { ref } from 'vue';
import axios from 'axios'
import { useRouter } from 'vue-router'
import { toast } from "react-toastify";
import CustomAlert from "@/components/CustomAlert.vue";

export default {
  components: {
    CustomAlert,
  },
  setup() {
    const router = useRouter();
    const username = ref("");
    const password = ref("");
    const showAlert = ref(false);
    const alertData = ref({
      title: "Error",
      message: "",
    });

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
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        console.log(response.data);

        if (response.data.token) {
          const jwtToken = response.data.token;
          localStorage.setItem("jwtToken", jwtToken);
          axios.defaults.headers.common["Authorization"] = `Bearer ${jwtToken}`;
        }

        if (response.data.roles) {
          const userRoles = response.data.roles;
          localStorage.setItem("userRoles", JSON.stringify(userRoles));
        }

        toast("You successfully logged in");
        router.push({ name: "home" });
        window.location.reload();
      } catch (error) {
        if (error.response && error.response.status === 403) {
          alertData.value.message = "Your account is banned!";
        } else {
          alertData.value.message = "Your email or password is wrong!";
        }
        showAlert.value = true;
        console.error(error);
      }
    };

    return {
      username,
      password,
      handleSubmit,
      showAlert,
      alertData,
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
              type="submit"
              class="w-full mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-400"
            >
              Login
            </button>
          </div>
        </form>
      </div>

      <div class="flex items-center justify-center py-4 text-center bg-gray-50 dark:bg-gray-700">
        <span class="text-sm text-gray-600 dark:text-gray-200">Don't have an account? </span>
        <a href="/registration" class="mx-2 text-sm font-bold text-blue-500 dark:text-blue-400 hover:underline">
          Register
        </a>
      </div>
    </div>

    <!-- Custom Alert -->
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      :type="alertData.type"
      @close="showAlert = false" />
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
