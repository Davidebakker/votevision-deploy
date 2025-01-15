<script>
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
import ConfirmDialog from "@/components/ConfirmDialog.vue";
import CustomAlert from "@/components/CustomAlert.vue";

export default {
  components: { ConfirmDialog, CustomAlert },
  setup() {
    const router = useRouter();
    const name = ref("");
    const username = ref("");
    const email = ref("");
    const password = ref("");
    const region = ref("");
    const showModal = ref(false);

    // Alert states
    const showAlert = ref(false);
    const alertData = ref({
      title: "",
      message: "",
      type: "", // 'success' or 'error'
    });

    const showAlertMessage = (title, message, type) => {
      alertData.value = { title, message, type };
      showAlert.value = true;
    };

    const handleSubmit = async () => {
      showModal.value = true;
    };

    const acceptTermsAndSubmit = async () => {
      const userData = {
        name: name.value,
        username: username.value,
        email: email.value,
        password: password.value,
        region: region.value,
      };

      try {
        const response = await axios.post(
          "http://localhost:8080/api/auth/signup",
          userData
        );

        if (response.data.token) {
          const jwtToken = response.data.token;
          localStorage.setItem("jwtToken", jwtToken);
          axios.defaults.headers.common["Authorization"] = `Bearer ${jwtToken}`;
        }

        localStorage.setItem("userRoles", "USER_ROLE");

        showAlertMessage(
          "Success",
          response.data.message || "Registration successful!",
          "success"
        );

        router.push({ name: "home" });
        window.location.reload();
      } catch (error) {
        if (error.response?.data?.message) {
          showAlertMessage("Error", error.response.data.message, "error");
        } else {
          showAlertMessage("Error", "An unexpected error occurred.", "error");
        }
      }
    };

    const closeModal = () => {
      showModal.value = false;
    };

    return {
      name,
      username,
      email,
      password,
      region,
      handleSubmit,
      showModal,
      closeModal,
      acceptTermsAndSubmit,
      showAlert,
      alertData,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-sm mx-auto overflow-hidden bg-white rounded-lg shadow-md">
      <div class="px-6 py-4">
        <h3 class="mt-3 text-xl font-medium text-center text-gray-600">Register</h3>

        <form @submit.prevent="handleSubmit">
          <input
            v-model="name"
            type="text"
            placeholder="Name"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <input
            v-model="username"
            type="text"
            placeholder="Username"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <input
            v-model="email"
            type="email"
            placeholder="Email"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <input
            v-model="password"
            type="password"
            placeholder="Password"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <input
            v-model="region"
            type="text"
            placeholder="Region"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <button
            type="submit"
            class="w-full mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-400"
          >
            Register
          </button>
        </form>
      </div>
    </div>

    <!-- Modal -->
    <ConfirmDialog
      v-if="showModal"
      title="Algemene Voorwaarden"
      message="Gebruikers moeten respectvol communiceren en zijn verantwoordelijk voor hun accountactiviteiten."
      @confirm="acceptTermsAndSubmit"
      @cancel="closeModal"
    />

    <!-- Alert -->
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      :type="alertData.type"
      @close="showAlert = false"
    />
  </div>
</template>



<style scoped>

.fixed {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.bg-black {
  background-color: rgba(0, 0, 0, 0.5);
}

.bg-white {
  background-color: white;
}

.overflow-y-auto {
  overflow-y: auto; /* Maakt verticale scroll mogelijk */
}
</style>


