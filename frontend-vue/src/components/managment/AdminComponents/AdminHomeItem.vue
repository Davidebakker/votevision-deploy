<template>
  <div class="admin-home">
    <h1>Admin Dashboard</h1>

    <div class="dashboard-sections">
      <!-- User Management Section -->
      <div class="section user-management">
        <h2>User Management</h2>
        <p>Manage users, view details, and assign roles.</p>
        <button @click="navigateTo('user-management')">Go to User Management</button>
      </div>

      <!-- Settings Section -->
      <div class="section settings">
        <h2>Settings</h2>
        <p>Configure site settings and preferences.</p>
        <button @click="navigateTo('settings')">Go to Settings</button>
      </div>

      <!-- Analytics Section -->
      <div class="section analytics">
        <h2>Analytics</h2>
        <p>View site analytics and performance reports.</p>
        <button @click="navigateTo('analytics')">Go to Analytics</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminHome",
  methods: {
    navigateTo(section) {
      this.$router.push({ name: section });
    }
  }
};
</script>

<style scoped>
.admin-home {
  padding: 20px;
  text-align: center;
}

.dashboard-sections {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-top: 20px;
}

.section {
  border: 1px solid #ccc;
  padding: 20px;
  width: 200px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

button {
  margin-top: 10px;
  padding: 10px 20px;
  cursor: pointer;
}
</style>
