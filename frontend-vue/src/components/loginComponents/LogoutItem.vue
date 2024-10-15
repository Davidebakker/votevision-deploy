<script>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const router = useRouter();

    onMounted(async () => {
      try {
        await router.push({ name: 'login' });
        window.location.reload();

        await axios.post('http://localhost:8080/api/auth/signout', { username: 'your_username' });

        localStorage.removeItem('jwtToken');
        axios.defaults.headers.common['Authorization'] = null;

        alert("You are successfully logged out!")
      } catch (error) {
        alert("An error occurred while logging out");

        console.error("Error during sign-out:", error);
      }
    });
  },
};

</script>
