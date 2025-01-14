<template>
  <div class="flex flex-col items-center bg-gray-900 min-h-screen text-white">
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      @close="showAlert = false"
    />

    <ConfirmDialog
      v-if="showConfirmDialog"
      :title="confirmData.title"
      :message="confirmData.message"
      @confirm="confirmData.onConfirm(); showConfirmDialog = false;"
      @cancel="showConfirmDialog = false"
    />

    <div v-if="loading" class="flex justify-center items-center mt-10">
      <div class="spinner-border animate-spin inline-block w-8 h-8 border-4 rounded-full"></div>
    </div>

    <div v-else class="w-full max-w-6xl p-6 mt-6 bg-gray-800 rounded-lg shadow-lg">
      <h2 class="text-2xl font-semibold mb-4">User Management</h2>
      <table class="w-full text-left rounded-lg overflow-hidden">
        <thead>
        <tr class="bg-gray-700">
          <th class="p-4 font-semibold">User ID</th>
          <th class="p-4 font-semibold">Name</th>
          <th class="p-4 font-semibold">Username</th>
          <th class="p-4 font-semibold">Email</th>
          <th class="p-4 font-semibold">disable</th>
          <th class="p-4 font-semibold">(Un)Ban</th>
          <th class="p-4 font-semibold">Ban Duration</th>
          <th class="p-4 font-semibold" v-if="isMod">Add Moderator</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in users" :key="user.userId" class="bg-gray-600 even:bg-gray-700">
          <td class="p-4">{{ user.userId }}</td>
          <td class="p-4">{{ user.name || "N/A" }}</td>
          <td class="p-4">{{ user.username }}</td>
          <td class="p-4">{{ user.email }}</td>
          <td class="p-4">
            <button
              v-if="user.active"
              @click="disableUser(user.userId)"
              class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-700"
              aria-label="disable User"
            >
              disable
            </button>
            <span v-else class="text-gray-400 font-semibold">user is disabled</span>
          </td>
          <td class="p-4">
            <button
              v-if="!user.banned"
              @click="banUser(user.userId)"
              class="bg-yellow-500 text-white py-1 px-3 rounded hover:bg-yellow-700"
              aria-label="Ban User"
            >
              Ban
            </button>
            <button
              v-else
              @click="unbanUser(user.userId)"
              class="bg-yellow-500 text-white py-1 px-3 rounded hover:bg-yellow-700"
              aria-label="Unban User"
            >
              Unban
            </button>
          </td>
          <td class="p-4">
              <span
                v-if="user.banned"
                class="text-red-500 font-semibold"
                aria-label="Ban Duration"
              >
                {{ formatBanDuration(user.banExpiration) }}
              </span>
            <span v-else class="text-gray-400 font-semibold">Not Banned</span>
          </td>
          <td class="p-4" v-if="isMod && !user.banned && user.active">
            <button
              @click="addAsAdmin(user.userId)"
              class="bg-green-500 text-white py-1 px-3 rounded hover:bg-green-700"
              aria-label="Add Moderator"
            >
              Add Moderator
            </button>
          </td>
          <td class="p-4 text-gray-400" v-else-if="isMod">
            Add Moderator not possible
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { ref, onMounted, computed } from "vue";
import { intervalToDuration, formatDuration } from "date-fns";
import CustomAlert from "@/components/CustomAlert.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";

export default {
  name: "UserManagement",
  components: {
    CustomAlert,
    ConfirmDialog,
  },
  setup() {
    const jwtToken = localStorage.getItem("jwtToken");
    const users = ref([]);
    const loading = ref(true);
    const showAlert = ref(false);
    const alertData = ref({
      title: "",
      message: "",
    });
    const showConfirmDialog = ref(false);
    const confirmData = ref({
      title: "",
      message: "",
      onConfirm: null,
    });

    const fetchUsers = async () => {
      loading.value = true;
      try {
        const response = await axios.get(
          "http://localhost:8080/api/user/findAll/role_user",
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
            },
          }
        );
        users.value = response.data.map((user) => ({
          ...user,
          banExpiration: user.banExpiration ? new Date(user.banExpiration) : null,
        }));
      } catch (error) {
        console.error("Error fetching user list", error);
      } finally {
        loading.value = false;
      }
    };

    const disableUser = (id) => {
      confirmData.value = {
        title: "disable User",
        message: "Are you sure you want to disable this user? This action cannot be undone.",
        onConfirm: () => handleDisableUser(id),
      };
      showConfirmDialog.value = true;
    };

    const handleDisableUser = async (id) => {
      try {
        await axios.post(`http://localhost:8080/api/user/disable/${id}`, null, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        alertData.value = { title: "Success", message: "User successfully disabled." };
        showAlert.value = true;
        await fetchUsers();
      } catch (error) {
        alertData.value = { title: "Error", message: "Failed to d user." };
        showAlert.value = true;
        console.error("Error deleting user", error);
      }
    };

    const banUser = (id) => {
      confirmData.value = {
        title: "Ban User",
        message: "Are you sure you want to ban this user? They will lose access temporarily.",
        onConfirm: () => handleBanUser(id),
      };
      showConfirmDialog.value = true;
    };

    const unbanUser = (id) => {
      confirmData.value = {
        title: "Unban User",
        message: "Are you sure you want to unban this user? They will regain access to the application.",
        onConfirm: () => handleUnbanUser(id),
      };
      showConfirmDialog.value = true;
    };

    const handleBanUser = async (id) => {
      try {
        await axios.post(`http://localhost:8080/api/user/ban/${id}`, null, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        alertData.value = { title: "Success", message: "User successfully banned." };
        showAlert.value = true;
        await fetchUsers();
      } catch (error) {
        alertData.value = { title: "Error", message: "Failed to ban user." };
        showAlert.value = true;
        console.error("Error banning user", error);
      }
    };

    const handleUnbanUser = async (id) => {
      try {
        await axios.put(`http://localhost:8080/api/user/unban/${id}`, null, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        alertData.value = { title: "Success", message: "User successfully unbanned." };
        showAlert.value = true;
        await fetchUsers();
      } catch (error) {
        alertData.value = { title: "Error", message: "Failed to unban user." };
        showAlert.value = true;
        console.error("Error unbanning user", error);
      }
    };

    const addAsAdmin = async (id) => {
      try {
        await axios.post(`http://localhost:8080/api/user/add/admin/${id}`, null, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        alertData.value = { title: "Success", message: "User successfully promoted to moderator." };
        showAlert.value = true;
        await fetchUsers();
      } catch (error) {
        alertData.value = { title: "Error", message: "Failed to promote user to moderator." };
        showAlert.value = true;
        console.error("Error adding admin", error);
      }
    };

    const formatBanDuration = (banExpiration) => {
      if (!banExpiration) return "N/A";
      const now = new Date();
      const duration = intervalToDuration({ start: now, end: banExpiration });
      return formatDuration(duration, { delimiter: ", " });
    };

    onMounted(fetchUsers);

    return {
      users,
      loading,
      disableUser,
      banUser,
      unbanUser,
      addAsAdmin,
      formatBanDuration,
      showAlert,
      alertData,
      showConfirmDialog,
      confirmData,
    };
  },
  computed: {
    isMod() {
      const userRoles = localStorage.getItem("userRoles");
      return userRoles && userRoles.includes("ROLE_MODERATOR");
    },
  },
};
</script>

<style scoped>
.spinner-border {
  border-top-color: transparent;
  border-right-color: white;
}
</style>
