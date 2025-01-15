<script>
import { ref } from "vue";
import axios from "axios";
import { useRoute, useRouter } from "vue-router";
import CustomAlert from "@/components/CustomAlert.vue";

export default {
  components: {
    CustomAlert,
  },
  setup() {
    const newComment = ref({ title: "", commentText: "" });
    const posts = ref([]);
    const jwtToken = localStorage.getItem("jwtToken");
    const route = useRoute();
    const router = useRouter();
    const onderwerpNummer = route.params.onderwerpNummer;
    const isSubmitting = ref(false);

    const showAlert = ref(false);
    const alertData = ref({
      title: "",
      message: "",
    });

    const handleSubmit = async () => {
      if (!jwtToken) {
        alertData.value = {
          title: "Authentication Required",
          message: "You must be logged in to post.",
        };
        showAlert.value = true;
        return;
      }

      if (!newComment.value.title || !newComment.value.commentText) {
        alertData.value = {
          title: "Validation Error",
          message: "Please fill in all fields.",
        };
        showAlert.value = true;
        return;
      }

      isSubmitting.value = true;
      try {
        const response = await axios.post(
          `http://localhost:8080/api/chat/topic/${onderwerpNummer}/comment/post`,
          {
            title: newComment.value.title,
            commentText: newComment.value.commentText,
          },
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              "Content-Type": "application/json",
            },
          }
        );

        const savedComment = response.data;

        posts.value.unshift({
          commentId: savedComment.commentId,
          title: savedComment.commentTitle,
          commentText: savedComment.commentText,
          date: new Date(savedComment.createdAt).toLocaleString(),
        });

        newComment.value.title = "";
        newComment.value.commentText = "";

        alertData.value = {
          title: "Success",
          message: "Comment successfully posted!",
        };
        showAlert.value = true;
      } catch (error) {
        console.error(error);
        alertData.value = {
          title: "Error",
          message: error.response?.data?.message || "An unexpected error occurred.",
        };
        showAlert.value = true;
      } finally {
        isSubmitting.value = false;
      }

      router.push("/forum");
    };

    const cancelPost = () => {
      newComment.value.title = "";
      newComment.value.commentText = "";
      router.push("/forum");
    };

    return {
      newComment,
      posts,
      handleSubmit,
      cancelPost,
      isSubmitting,
      showAlert,
      alertData,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <!-- Custom Alert -->
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      @close="showAlert = false"
    />

    <div class="w-full max-w-md mx-auto overflow-hidden bg-white rounded-lg shadow-md dark:bg-gray-800">
      <div class="px-6 py-4">
        <h2 class="mt-3 text-xl font-medium text-center text-gray-600 dark:text-gray-200">
          Maak een nieuwe post
        </h2>

        <form @submit.prevent="handleSubmit" class="mt-4">
          <input
            v-model="newComment.title"
            type="text"
            placeholder="Titel"
            required
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
          />
          <textarea
            v-model="newComment.commentText"
            placeholder="Schrijf je post..."
            required
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
          ></textarea>

          <div class="flex mt-4">
            <button
              type="submit"
              class="w-4/5 px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400"
            >
              Posten
            </button>
            <button
              type="button"
              @click="cancelPost"
              class="w-1/5 px-4 py-2 bg-red-700 text-white rounded-lg hover:bg-red-900"
            >
              Cancel
            </button>
          </div>
        </form>

        <div class="posts mt-6">
          <div v-if="posts.length === 0" class="text-center text-gray-500 dark:text-gray-400">
          </div>
          <div v-else>
            <div
              v-for="(post, index) in posts"
              :key="index"
              class="post mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600"
            >
              <h4 class="text-lg font-medium text-gray-600 dark:text-gray-200">{{ post.title }}</h4>
              <p class="mt-2 text-gray-600 dark:text-gray-200">{{ post.commentText }}</p>
              <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">
                Geplaatst op: {{ post.date }}
              </small>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>



<style scoped>
.min-h-screen {
  min-height: 100vh;
}

.bg-gray-100 {
  background-color: #111827;
}
</style>
