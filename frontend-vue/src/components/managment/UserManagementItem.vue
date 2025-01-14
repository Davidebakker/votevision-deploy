<template>
  <div class="flex flex-col items-center bg-white dark:bg-gray-900 min-h-screen text-white">

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


    <div class="w-full max-w-6xl p-6 mt-6 bg-gray-800 rounded-lg shadow-lg">
      <h2 class="text-2xl font-semibold mb-4">User Management</h2>
      <table class="w-full text-left rounded-lg overflow-hidden">
        <thead>
        <tr class="bg-gray-700">
          <th class="p-4 font-semibold">User ID</th>
          <th class="p-4 font-semibold">Name</th>
          <th class="p-4 font-semibold">Username</th>
          <th class="p-4 font-semibold">Email</th>
          <th class="p-4 font-semibold">Delete</th>
          <th class="p-4 font-semibold">Ban</th>
          <th class="p-4 font-semibold" v-if="isMod">Add Moderator</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in users" :key="user.userId" class="bg-gray-600 even:bg-gray-700">
          <td class="p-4">{{ user.userId }}</td>
          <td class="p-4">{{ user.name || 'N/A' }}</td>
          <td class="p-4">{{ user.username }}</td>
          <td class="p-4">{{ user.email }}</td>
          <td class="p-4">
            <button
              @click="deleteUser(user.userId)"
              class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-700"
            >
              Delete
            </button>
          </td>
          <td class="p-4">
            <button
              v-if="!user.banned"
              @click="banUser(user.userId)"
              class="bg-yellow-500 text-white py-1 px-3 rounded hover:bg-yellow-700"
            >
              Ban
            </button>
            <span v-else class="text-red-500 font-semibold">
                User is banned for: {{ formatBanDuration(user.banExpiration) }}
              </span>
          </td>
          <td class="p-4" v-if="isMod && !user.banned">
            <button
              @click="addAsAdmin(user.userId)"
              class="bg-green-500 text-white py-1 px-3 rounded hover:bg-green-700"
            >
              Add Moderator
            </button>
          </td>
          <td class="p-4" v-else-if="isMod">
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
import { ref, onMounted } from "vue";
import { intervalToDuration } from "date-fns";
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
      }
    };

    const deleteUser = (id) => {
      confirmData.value = {
        title: "Delete User",
        message: "Are you sure you want to delete this user? This action cannot be undone.",
        onConfirm: () => handleDeleteUser(id),
      };
      showConfirmDialog.value = true;
    };

    const handleDeleteUser = async (id) => {
      try {
        await axios.post(`http://localhost:8080/api/user/delete/${id}`, null, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        alertData.value = { title: "Success", message: "User successfully deleted." };
        showAlert.value = true;
        await fetchUsers();
      } catch (error) {
        alertData.value = { title: "Error", message: "Failed to delete user." };
        showAlert.value = true;
        console.error("Error deleting user", error);
      }
      showConfirmDialog.value = false; // Ensure dialog is closed after action
    };

    const banUser = (id) => {
      confirmData.value = {
        title: "Ban User",
        message: "Are you sure you want to ban this user? They will lose access temporarily.",
        onConfirm: () => handleBanUser(id),
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
      showConfirmDialog.value = false; // Ensure dialog is closed after action
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
      return `${duration.days || 0} days and ${duration.hours || 0} hours`;
    };

    onMounted(fetchUsers);

    return {
      users,
      deleteUser,
      banUser,
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
/* General Table Styling */
table {
  width: 100%;
  border-collapse: collapse;
}

thead th {
  text-align: left;
  background-color: #374151;
  color: #ffffff;
  padding: 1rem;
}

tbody tr {
  background-color: #1f2937;
}

tbody tr:nth-child(even) {
  background-color: #374151;
}

td {
  padding: 1rem;
  border-bottom: 1px solid #374151;
}

button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 5px;
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  opacity: 0.9;
}

.flex {
  align-items: center;
  min-height: 100vh;
}

.shadow-lg {
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}
</style>
