<script>
import { useRouter } from 'vue-router';
import { ref } from 'vue';

export default {
  setup() {
    const router = useRouter();
    const newPost = ref({
      content: "",
    });

    const submitPost = () => {
      const existingComments = JSON.parse(localStorage.getItem('comments')) || [];
      const newComment = {
        content: newPost.value.content,
        date: new Date().toLocaleString(),
      };
      existingComments.push(newComment);

      localStorage.setItem('comments', JSON.stringify(existingComments));
      router.push('');
    };

    return {
      newPost,
      submitPost,
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
          <button
              type="submit"
              class="flex center w-full px-4 py-2 mt-4 text-sm font-medium tracking-wide text-white capitalize transition-colors duration-300 transform bg-blue-500 rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
          >
            Posten
          </button>
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