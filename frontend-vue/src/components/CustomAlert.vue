<template>
  <div
    class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50"
  >
    <div
      class="p-4 max-w-md w-full rounded-lg shadow-lg"
      :class="{
        'bg-green-500 text-white': type === 'success',
        'bg-red-500 text-white': type === 'error',
      }"
    >
      <h3 class="text-xl font-semibold">{{ title }}</h3>
      <p class="mt-2">{{ message }}</p>
      <button
        class="mt-4 py-2 px-4 bg-white text-black rounded-lg"
        @click="$emit('close')"
      >
        Close
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "CustomAlert",
  props: {
    title: {
      type: String,
      required: true,
    },
    message: {
      type: String,
      required: true,
    },
    type: {
      type: String,
      required: true,
    },
  },
};
</script>

<style scoped>
/* Optional styling for animations */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

div {
  animation: fadeIn 0.3s ease-out;
}
</style>
