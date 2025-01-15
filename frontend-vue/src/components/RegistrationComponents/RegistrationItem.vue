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

    const isDarkMode = ref(false);

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

    const termsMessage = ref(`
      <h2 class="text-xl font-bold mb-4">User Behavior and Responsibilities</h2>
      <p><strong>Respectful Communication:</strong> Users must interact respectfully and must not post offensive, discriminatory, violent, or hateful messages.</p>
      <p><strong>No Misinformation:</strong> Users must not post incorrect or misleading information, especially about elections or candidates.</p>
      <p><strong>No Spam or Promotions:</strong> Unauthorized advertisements, spam, or promotional content are prohibited.</p>
      <p><strong>Account Responsibility:</strong> Users are responsible for activities occurring from their account.</p>
      <h2 class="text-xl font-bold mb-4 mt-4">Moderation and Content Management</h2>
      <p><strong>Right to Remove Content:</strong> Administrators may remove inappropriate posts or accounts without prior notice.</p>
      <p><strong>Report Functionality:</strong> Users can report inappropriate behavior or content.</p>
      <p><strong>No Liability for Discussions:</strong> The website is not responsible for user opinions or statements.</p>
      <h2 class="text-xl font-bold mb-4 mt-4">Liability</h2>
      <p><strong>No Accuracy Guarantee:</strong> The website does not guarantee that information (such as election results) is accurate and up-to-date.</p>
      <p><strong>Limitation of Liability:</strong> The website is not responsible for damages resulting from using the site.</p>
      <h2 class="text-xl font-bold mb-4 mt-4">Changes to Terms</h2>
      <p>The terms may be changed, and users will be notified of updates.</p>
    `);


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
      isDarkMode,
      termsMessage,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 dark:bg-gray-900">
    <div class="w-full max-w-sm mx-auto overflow-hidden bg-white rounded-lg shadow-md dark:bg-gray-800">
      <div class="px-6 py-4">
        <h3 class="mt-3 text-xl font-medium text-center text-gray-600 dark:text-gray-200">Register</h3>

        <form @submit.prevent="handleSubmit">
          <input
            v-model="name"
            type="text"
            placeholder="Name"
            required
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
          />
          <input
            v-model="username"
            type="text"
            placeholder="Username"
            required
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
          />
          <input
            v-model="email"
            type="email"
            placeholder="Email"
            required
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
          />
          <input
            v-model="password"
            type="password"
            placeholder="Password"
            required
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
          />
          <input
            v-model="region"
            type="text"
            placeholder="Region"
            required
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
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
      title="Terms and Conditions"
      :message="termsMessage"
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
