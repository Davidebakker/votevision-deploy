<template>
  <section class="bg-gray-50 dark:bg-gray-900 min-h-screen">
    <div class="container mx-auto px-6 py-12">
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <!-- Profile Section -->
        <div>
          <div class="text-center lg:text-left">
            <img
              src="https://via.placeholder.com/150"
              alt="Profile Picture"
              class="w-32 h-32 mx-auto lg:mx-0 rounded-full shadow-lg"
            />
            <p class="mt-2 text-sm text-gray-600 dark:text-gray-400">Username: {{ userProfile.firstName.toLowerCase() + userProfile.lastName.toLowerCase() }}</p>
            <h2 class="mt-4 text-2xl font-semibold text-gray-800 dark:text-white">
              {{ userProfile.firstName }} {{ userProfile.lastName }}
            </h2>
            <p class="mt-2 text-gray-600 dark:text-gray-400">{{ userProfile.bio }}</p>
            <button
              @click="toggleEditMode"
              class="mt-4 px-4 py-2 text-sm font-medium text-white bg-blue-500 rounded-lg hover:bg-blue-400 focus:ring focus:ring-blue-300 focus:ring-opacity-50"
            >
              {{ isEditing ? "Cancel" : "Edit Profile" }}
            </button>
          </div>

          <div v-if="isEditing" class="mt-6 space-y-4">
            <div>
              <label class="block text-sm text-gray-600 dark:text-gray-200">First Name</label>
              <input
                type="text"
                v-model="userProfile.firstName"
                class="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg dark:border-gray-700 focus:ring focus:ring-blue-400"
              />
            </div>
            <div>
              <label class="block text-sm text-gray-600 dark:text-gray-200">Last Name</label>
              <input
                type="text"
                v-model="userProfile.lastName"
                class="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg dark:border-gray-700 focus:ring focus:ring-blue-400"
              />
            </div>
            <div>
              <label class="block text-sm text-gray-600 dark:text-gray-200">Email</label>
              <input
                type="email"
                v-model="userProfile.email"
                class="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg dark:border-gray-700 focus:ring focus:ring-blue-400"
              />
            </div>
            <div>
              <label class="block text-sm text-gray-600 dark:text-gray-200">Region</label>
              <input
                type="text"
                v-model="userProfile.Region"
                class="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg dark:border-gray-700 focus:ring focus:ring-blue-400"
              />
            </div>
            <div>
              <label class="block text-sm text-gray-600 dark:text-gray-200">Bio</label>
              <textarea
                v-model="userProfile.bio"
                class="w-full px-4 py-2 mt-1 border border-gray-300 rounded-lg dark:border-gray-700 focus:ring focus:ring-blue-400"
              ></textarea>
            </div>
            <button
              @click="saveProfile"
              class="w-full px-6 py-2 mt-4 text-sm font-medium text-white bg-green-500 rounded-lg hover:bg-green-400 focus:ring focus:ring-green-300"
            >
              Save Changes
            </button>
          </div>

          <div v-else class="mt-6 space-y-4">
            <div>
              <h3 class="text-sm font-medium text-gray-800 dark:text-white">Email</h3>
              <p class="text-gray-600 dark:text-gray-400">{{ userProfile.email }}</p>
            </div>
            <div>
              <h3 class="text-sm font-medium text-gray-800 dark:text-white">Region</h3>
              <p class="text-gray-600 dark:text-gray-400">{{ userProfile.Region }}</p>
            </div>
            <div>
              <h3 class="text-sm font-medium text-gray-800 dark:text-white">Bio</h3>
              <p class="text-gray-600 dark:text-gray-400">{{ userProfile.bio }}</p>
            </div>
          </div>
        </div>

        <!-- Comments Section -->
        <div>
          <h2 class="text-xl font-semibold text-gray-800 dark:text-white">My Comments</h2>
          <ul class="mt-4 space-y-4">
            <li
              v-for="(comment, index) in userComments"
              :key="index"
              class="p-4 bg-white shadow rounded-lg dark:bg-gray-800"
            >
              <h3 class="text-lg font-medium text-gray-800 dark:text-white">{{ comment.title }}</h3>
              <p class="text-gray-700 dark:text-gray-300">{{ comment.body }}</p>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { ref, onMounted } from "vue";
import axios from "axios";

export default {
  setup() {
    const userProfile = ref({
      firstName: "",
      lastName: "",
      email: "",
      Region: "",
      bio: ""
    });

    const userComments = ref([]);
    const isEditing = ref(false);

    const fetchUserDetails = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/user/getDetails", {
          headers: { Authorization: `Bearer ${localStorage.getItem("jwtToken")}` }
        });
        const data = response.data;

        console.log(data);

        userProfile.value = {
          firstName: data.firstName || "",
          lastName: data.lastName || "",
          email: data.email || "",
          Region: data.region || "",
          bio: data.bio || ""
        };

        userComments.value = data.comments.map(comment => ({
          title: comment.commentTitle || "Untitled",
          body: comment.commentText || "No content available"
        })) || [];
      } catch (error) {
        console.error("Error fetching user details:", error);
      }
    };

    const toggleEditMode = () => {
      isEditing.value = !isEditing.value;
    };

    const saveProfile = () => {
      isEditing.value = false;
      alert("Profile updated successfully!");
    };

    onMounted(fetchUserDetails);

    return {
      userProfile,
      userComments,
      isEditing,
      toggleEditMode,
      saveProfile
    };
  }
};
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
}
</style>
