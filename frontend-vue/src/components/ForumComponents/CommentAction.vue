<script>
export default {
  props: {
    upvotesCount: {
      type: Number,
      required: true,
    },
    commentId: {
      type: Number,
      required: true,
    },
    replyId: {
      type: Number,
      required: false, // Alleen nodig voor replies
    },
  },
  data() {
    return {
      upvotes: this.upvotesCount,
      reported: false,
    };
  },
  methods: {
    handleUpvote() {
      // Simuleer een API-call
      this.upvotes += 1;
      console.log(`Upvoted comment/reply with ID: ${this.commentId || this.replyId}`);
    },
    handleReport() {
      if (this.reported) {
        alert("You already reported this comment/reply.");
        return;
      }
      this.reported = true;
      console.log(`Reported comment/reply with ID: ${this.commentId || this.replyId}`);
      alert("This comment/reply has been reported.");
    },
  },
};
</script>

<template>
  <div class="flex items-center space-x-4">
    <button @click="handleUpvote" class="flex items-center space-x-1 text-green-600 hover:underline">
      <span>▲</span>
      <span>{{ upvotes }}</span>
    </button>
    <button @click="handleReport" class="text-red-600 hover:underline">
      Report
    </button>
  </div>
</template>



<style scoped>
button {
  transition: color 0.2s ease;
}

button:hover {
  color: #555;
}
</style>
