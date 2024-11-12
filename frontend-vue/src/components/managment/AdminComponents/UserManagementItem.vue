<script>
import axios from 'axios'


import { ref, onMounted } from 'vue'

export default {
  name: "UserManagementItem",
  setup() {
    const jwtToken = localStorage.getItem('jwtToken');
    const users = ref([]);

    const fetchUsers = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/user/findAll');
        users.value = response.data;
      }
      catch (error) {
        console.error('Error fetching user list', error);
      }
    }

    const deleteUser = async (id) => {
      try {
        const response = await axios.post(`http://localhost:8080/api/user/delete/${id}`,
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              'Content-Type': 'application/json',
            },
          }
        );
        window.location.reload();
      }
      catch (error) {
        console.error('Error fetching user list', error);
      }
    }

    onMounted(() => fetchUsers());

    return {
      deleteUser,
      users
    }
  }
}
</script>

<template>
  <div>
    <table>
      <thead>
      <tr>
        <th>User ID</th>
        <th>Name</th>
        <th>Username</th>
        <th>Email</th>
        <th>delete</th>
        <th>ban</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.userId">
        <td>{{ user.userId }}</td>
        <td>{{ user.name || 'N/A' }}</td>
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td><button @click="deleteUser(user.userId)">Delete</button></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

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
