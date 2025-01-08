<script>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import CustomAlert from "@/components/CustomAlert.vue";

export default {
  components: {
    CustomAlert,
  },
  setup() {
    const router = useRouter();

    const showAlert = ref(false);
    const alertData = ref({
      title: "",
      message: "",
    });

    onMounted(async () => {
      try {
        await axios.post("http://localhost:8080/api/auth/logout", {
          username: "your_username",
        });

        localStorage.removeItem("jwtToken");
        localStorage.removeItem("userRoles");
        axios.defaults.headers.common["Authorization"] = null;

        // Show success alert
        alertData.value = {
          title: "Success",
          message: "You are successfully logged out!",
        };
        showAlert.value = true;

        await router.push({ name: "login" });
        window.location.reload();
      } catch (error) {
        alertData.value = {
          title: "Error",
          message: "An error occurred while logging out.",
        };
        showAlert.value = true;

        console.error("Error during sign out:", error);
      }
    });

    return {
      showAlert,
      alertData,
    };
  },
};
</script>

<template>
  <div>
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      @close="showAlert = false"
    />
  </div>
</template>
