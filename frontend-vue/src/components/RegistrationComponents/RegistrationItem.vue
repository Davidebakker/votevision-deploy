<script>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const router = useRouter();
    const name = ref('');
    const username = ref('');
    const email = ref('');
    const password = ref('');
    const region = ref('');
    const showModal = ref(false);

    const handleSubmit = async () => {
      showModal.value = true;
    };

    const acceptTermsAndSubmit = async () => {
      const userData = {
        name: name.value,
        username: username.value,
        email: email.value,
        password: password.value,
        region: region.value,
      };

      try {
        const response = await axios.post('http://localhost:8080/api/auth/signup', userData);
        console.log(response.data);

        if (response.data.token) {
          const jwtToken = response.data.token;
          localStorage.setItem('jwtToken', jwtToken);
          axios.defaults.headers.common['Authorization'] = `Bearer ${jwtToken}`;
        }

        localStorage.setItem('userRoles', 'USER_ROLE');
        alert(response.data.message || 'Registratie succesvol');

        router.push({ name: 'home' });
        window.location.reload();
      } catch (error) {
        console.error(error);
        if (error.response?.data?.message) {
          alert(error.response.data.message);
        } else {
          alert('Er is een onverwachte fout opgetreden.');
        }
      }
    };

    const closeModal = () => {
      showModal.value = false;
    };

    return {
      name,
      username,
      email,
      password,
      region,
      handleSubmit,
      showModal,
      closeModal,
      acceptTermsAndSubmit,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-sm mx-auto overflow-hidden bg-white rounded-lg shadow-md">
      <div class="px-6 py-4">
        <h3 class="mt-3 text-xl font-medium text-center text-gray-600">Register</h3>

        <form @submit.prevent="handleSubmit">
          <input
            v-model="name"
            type="text"
            placeholder="Name"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <input
            v-model="username"
            type="text"
            placeholder="Username"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <input
            v-model="email"
            type="email"
            placeholder="Email"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <input
            v-model="password"
            type="password"
            placeholder="Password"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <input
            v-model="region"
            type="text"
            placeholder="Region"
            required
            class="block w-full px-4 py-2 mt-4 border rounded-lg"
          />
          <button
            type="submit"
            class="w-full mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-400"
          >
            Register
          </button>
        </form>
      </div>
    </div>

    <div
      v-if="showModal"
      class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
    >
      <div class="bg-white rounded-lg p-6 w-11/12 max-w-3xl max-h-[90vh] overflow-y-auto relative">
        <h1 class="text-xl font-bold mb-4">Algemene Voorwaarden</h1>
        <h2 class="text-xl font-bold mb-4">Gebruikersgedrag en verantwoordelijkheden</h2>
        <p><strong>Respectvolle communicatie:</strong> Gebruikers moeten respectvol met elkaar omgaan en mogen geen beledigende, discriminerende, gewelddadige of haatzaaiende berichten plaatsen.</p>
        <p><strong>Geen misinformatie:</strong> Gebruikers mogen geen onjuiste of misleidende informatie plaatsen, vooral over verkiezingen of kandidaten.</p>
        <p><strong>Geen spam of promotie:</strong> Het is verboden om ongeautoriseerde advertenties, spam of promotionele content te plaatsen.</p>
        <p><strong>Accountverantwoordelijkheid:</strong> Gebruikers zijn verantwoordelijk voor de activiteiten die vanuit hun account plaatsvinden.</p>
        <br>
        <h2 class="text-xl font-bold mb-4">Moderatie en contentbeheer</h2>
        <p><strong>Recht om content te verwijderen:</strong> Beheerders hebben het recht om ongepaste berichten of accounts te verwijderen zonder voorafgaande kennisgeving.</p>
        <p><strong>Rapporteerfunctie:</strong> Gebruikers kunnen ongepast gedrag of content melden.</p>
        <p><strong>Geen aansprakelijkheid voor discussies:</strong> De website is niet verantwoordelijk voor de meningen en uitspraken van gebruikers.</p>
        <br>
        <h2 class="text-xl font-bold mb-4">Aansprakelijkheid</h2>
        <p><strong>Geen garantie op juistheid:</strong> De website garandeert niet dat alle informatie (bijv. verkiezingsuitslagen) correct en up-to-date is.</p>
        <p><strong>Beperking van aansprakelijkheid:</strong> De website is niet verantwoordelijk voor schade die ontstaat door het gebruik van de site.</p>
        <br>
        <h2 class="text-xl font-bold mb-4">Wijzigingen in de voorwaarden</h2>
        <p> De voorwaarden kunnen worden aangepast en gebruikers worden op de hoogte gesteld van wijzigingen.</p>
        <button
          @click="acceptTermsAndSubmit"
          class="mt-4 px-4 py-2 bg-green-500 text-white rounded-lg">
          Akkoord en Registreren
        </button>
        <button
          @click="closeModal"
          class="mt-4 ml-2 px-4 py-2 bg-red-500 text-white rounded-lg">
          Annuleren
        </button>
      </div>
    </div>
  </div>
</template>


<style scoped>

.fixed {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.bg-black {
  background-color: rgba(0, 0, 0, 0.5);
}

.bg-white {
  background-color: white;
}

.overflow-y-auto {
  overflow-y: auto; /* Maakt verticale scroll mogelijk */
}
</style>


