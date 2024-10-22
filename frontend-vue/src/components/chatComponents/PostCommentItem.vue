<script>

import { ref } from 'vue'
import axios from 'axios'
import { useRoute } from 'vue-router'
import { jwtDecode } from 'jwt-decode';

export default {
  setup() {
    const route = useRoute();
    const onderwerpNummer = route.params.onderwerpNummer;
    const text = ref('');
    const jwtToken = localStorage.getItem('jwtToken');

    const token = localStorage.getItem('token');
    if (token) {
      const decoded = jwtDecode(token);
      const userId = decoded.userId;
      console.log("User ID:", userId);
    }

    const handleSubmit = async () => {
      const commentData = {
        commentText: text.value,
      };

      try {
        const response = await axios.post(`http://localhost:8080/api/chat/topic/${onderwerpNummer}/comment/post`, commentData, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
          }
        });
        console.log(response.data);

        alert(response.data.message || "Data submitted successfully");

      } catch (error) {
        console.error(error);
        if (error.response && error.response.data && error.response.data.message) {
          alert(error.response.data.message); // Display the specific error message
        } else {
          alert("An unexpected error occurred.");
        }
      }
    };

    return {
      text,
      handleSubmit,
    };
  },
};

</script>

<template>
  <div>
    <form @submit.prevent="handleSubmit">
      <label for="comment">Add a Comment:</label>
      <textarea id="comment" v-model="text" placeholder="Enter your comment here"></textarea>
      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<style scoped>

</style>