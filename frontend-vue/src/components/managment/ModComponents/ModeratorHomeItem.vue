<template>
  <div class="moderator-home">
    <h1>Moderator Dashboard</h1>

    <div class="dashboard-sections">
      <div class="section content-moderation">
        <h2>Content Moderation</h2>
        <p>Review and manage reported posts and comments.</p>
        <button @click="navigateTo('content-moderation')">Go to Content Moderation</button>
      </div>

      <div class="section user-reports">
        <h2>User Reports</h2>
        <p>Handle user-submitted reports and warnings.</p>
        <button @click="navigateTo('user-reports')">Go to User Reports</button>
      </div>

      <div class="section community-guidelines">
        <h2>Community Guidelines</h2>
        <p>Update and ensure adherence to community rules.</p>
        <button @click="navigateTo('community-guidelines')">Go to Guidelines</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ModeratorHome",
  methods: {
    navigateTo(section) {
      this.$router.push({ name: section });
    }
  }
};
</script>

<style scoped>
.moderator-home {
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
  background-color: #f9f9f9;
}

button {
  margin-top: 10px;
  padding: 10px 20px;
  cursor: pointer;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
}
p{
  text-align: center;
  color: Black;
}
h2{
  text-align: center;
  color: darkblue;
  font-weight: bold;
  background-color: darkgray;
  padding: 10px;
  border-radius: 10px;
}
</style>
