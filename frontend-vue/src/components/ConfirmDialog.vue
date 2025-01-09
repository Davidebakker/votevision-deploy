<template>
  <div
    class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50"
    @click.self="cancel"
  >
    <div class="bg-gray-800 text-white rounded-lg shadow-lg w-11/12 max-w-lg p-6">
      <h3 class="text-xl font-semibold mb-4">{{ title }}</h3>
      <p class="mb-6">{{ message }}</p>
      <div class="flex justify-end space-x-4">
        <button
          class="bg-gray-500 hover:bg-gray-600 text-white py-2 px-4 rounded"
          @click="cancel"
        >
          Cancel
        </button>
        <button
          class="bg-red-500 hover:bg-red-600 text-white py-2 px-4 rounded"
          @click="confirm"
        >
          Confirm
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ConfirmDialog",
  props: {
    title: {
      type: String,
      required: true,
    },
    message: {
      type: String,
      required: true,
    },
  },
  emits: ["confirm", "cancel"],
  methods: {
    confirm() {
      this.$emit("confirm");
    },
    cancel() {
      this.$emit("cancel");
    },
  },
};
</script>

<style scoped>
/* Add subtle animations for better user experience */
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
