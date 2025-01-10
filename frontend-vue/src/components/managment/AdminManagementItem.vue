<script>
import axios from "axios";
import { ref, onMounted } from "vue";

export default {
  name: "adminManagement",
  setup() {
    const jwtToken = localStorage.getItem("jwtToken");
    const admins = ref([]);

    const fetchAdmins = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/user/findAll/role_admin", {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        admins.value = response.data.map((admin) => ({...admin}));
      } catch (error) {
        console.error("Error fetching admin list", error);
      }
    };

    const demoteAdmin = async (userId) => {
      try {
        await axios.post(`http://localhost:8080/api/user/demote/admin/${userId}`, null, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        await fetchAdmins();
      } catch (error) {
        console.error("Error deleting admin role", error);
      }
    };

    onMounted(fetchAdmins);

    return {
      admins,
      demoteAdmin,
    };
  },
};
</script>


<template>
  <div class="flex flex-col items-center bg-gray-900 min-h-screen text-white">
    <!-- Table Container -->
    <div class="w-full max-w-6xl p-6 mt-6 bg-gray-800 rounded-lg shadow-lg">
      <h2 class="text-2xl font-semibold mb-4">Moderator Management</h2>
      <table class="w-full text-left rounded-lg overflow-hidden">
        <thead>
        <tr class="bg-gray-700">
          <th class="p-4 font-semibold">User ID</th>
          <th class="p-4 font-semibold">Name</th>
          <th class="p-4 font-semibold">Username</th>
          <th class="p-4 font-semibold">Email</th>
          <th class="p-4 font-semibold">demote</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="admin in admins" :key="admin.userId" class="bg-gray-600 even:bg-gray-700">
          <td class="p-4">{{ admin.userId }}</td>
          <td class="p-4">{{ admin.name || 'N/A' }}</td>
          <td class="p-4">{{ admin.username }}</td>
          <td class="p-4">{{ admin.email }}</td>
          <td class="p-4">
            <button
              @click="demoteAdmin(admin.userId)"
              class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-700"
            >
              demote
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
/* General table styling */
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

tbody tr:nth-child(even) {
  background-color: #1f2937;
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
</style>
