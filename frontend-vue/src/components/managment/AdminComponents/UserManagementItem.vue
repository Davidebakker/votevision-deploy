<template>
  <div class="flex flex-col items-center bg-gray-900 min-h-screen text-white">
    <!-- Table container -->
    <div class="w-full max-w-4xl p-6 mt-6 bg-gray-800 rounded-lg shadow-lg">
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
              class="bg-yellow-500 text-white py-1 px-3 rounded hover:bg-yellow-700">
              Ban
            </button>
            <span v-else class="text-red-500 font-semibold">
              User is banned for: {{ formatBanDuration(user.banExpiration) }}
            </span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { formatDuration, intervalToDuration } from 'date-fns';

export default {
  name: "UserManagementItem",
  setup() {
    const jwtToken = localStorage.getItem('jwtToken');
    const users = ref([]);

    const fetchUsers = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/user/findAll');
        users.value = response.data.map(user => ({
          ...user,
          banExpiration: user.banExpiration ? new Date(user.banExpiration) : null,
        }));
      } catch (error) {
        console.error('Error fetching user list', error);
      }
    };

    const deleteUser = async (id) => {
      try {
        await axios.post(`http://localhost:8080/api/user/delete/${id}`, null, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            'Content-Type': 'application/json',
          },
        });
        await fetchUsers();
      } catch (error) {
        console.error('Error deleting user', error);
      }
    };

    const banUser = async (id) => {
      try {
        await axios.post(`http://localhost:8080/api/user/ban/${id}`, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            'Content-Type': 'application/json',
          }
        });
        await fetchUsers();
      } catch (error) {
        console.error('Error banning user', error);
      }
    };

    const formatBanDuration = (banExpiration) => {
      if (!banExpiration) return 'N/A';

      const now = new Date();
      const duration = intervalToDuration({ start: now, end: banExpiration });
      return `${duration.days} days and ${duration.hours} hours`;
    };

    onMounted(fetchUsers);

    return {
      deleteUser,
      banUser,
      users,
      formatBanDuration,
    };
  },
};

</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  color: black;
}

th, td {
  padding: 8px;
  border: 1px solid #ddd;
}

th {
  background-color: #f4f4f4;
  color: black;
}
</style>
