<script>
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import axios from 'axios';


export default {
  setup() {
    const newComment = ref({
      commentText: "",
    });
    const router = useRouter();


    const submitComment = async () => {
      try {
        const response = await axios.post(`http://localhost:8080/api/chat/topic/1/comment/post`, newComment.value);
        console.log(response.data);

        router.push('/forum');
      } catch (error) {
        console.error('Error posting comment:', error);
      }
    };

    const cancelPost = () => {
      router.push('/forum');
    };

    return {
      newComment,
      submitComment,
      cancelPost,
    };
  },
};
</script>


<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-md mx-auto overflow-hidden bg-white rounded-lg shadow-md dark:bg-gray-800">
      <div class="px-6 py-4">
        <h2 class="mt-3 text-xl font-medium text-center text-gray-600 dark:text-gray-200">
          Maak een nieuwe post
        </h2>

        <form @submit.prevent="submitPost" class="mt-4">
          <textarea
              v-model="newPost.content"
              placeholder="Schrijf je post..."
              class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              required
          ></textarea>
          <div class="flex mt-4">
            <button
                type="submit"
                class="w-4/5 px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50 mr-2"
            >
              Posten
            </button>

            <button
                type="button"
                @click="cancelPost"
                class="w-1/4 px-4 py-2bg-red-500 text-white font-medium rounded-lg hover:bg-red-600 focus:outline-none focus:ring focus:ring-red-300 focus:ring-opacity-50 mr-2"
            >
              Annuleren
            </button>
          </div>
        </form>
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