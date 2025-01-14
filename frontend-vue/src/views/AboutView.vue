<template>
  <section class="bg-white dark:bg-gray-900 min-h-screen h-full flex items-center">
    <div class="container px-6 py-10 mx-auto">
      <h1
        class="text-2xl font-semibold text-center text-gray-800 capitalize lg:text-3xl dark:text-white"
      >
        Our <span class="text-blue-500">Executive Team</span>
      </h1>

      <p class="max-w-2xl mx-auto my-6 text-center text-gray-500 dark:text-gray-300">
        We are a dedicated team of professionals committed to innovation, collaboration, and excellence. Together, we work to bring visionary ideas to life, delivering impactful solutions that make a difference.
      </p>

      <div class="grid grid-cols-1 gap-8 mt-8 xl:mt-16 md:grid-cols-2 xl:grid-cols-2">
        <div
          class="px-12 py-8 transition-colors duration-300 transform border cursor-pointer rounded-xl hover:border-transparent group hover:bg-blue-600 dark:border-gray-700 dark:hover:border-transparent"
        >
          <div class="flex flex-col sm:-mx-4 sm:flex-row">
            <img
              class="flex-shrink-0 object-cover w-24 h-24 rounded-full sm:mx-4 ring-4 ring-gray-300"
              src="@/assets/img/Matin.png"
              alt="Matin Kh"
            />
            <div class="mt-4 sm:mx-4 sm:mt-0">
              <h1
                class="text-xl font-semibold text-gray-700 capitalize md:text-2xl dark:text-white group-hover:text-white"
              >
                Matin Kh
              </h1>
              <p class="mt-2 text-gray-500 capitalize dark:text-gray-300 group-hover:text-gray-300">
                Senior Full Stack Developer
              </p>
            </div>
          </div>
          <p class="mt-4 text-gray-500 capitalize dark:text-gray-300 group-hover:text-gray-300">
            Driven by a passion for creating innovative digital solutions, I specialize in crafting scalable, user friendly applications. With a strong foundation in both front end and back end technologies, I thrive on turning complex challenges into seamless user experiences.
          </p>
        </div>

        <div
          class="px-12 py-8 transition-colors duration-300 transform border cursor-pointer rounded-xl hover:border-transparent group hover:bg-blue-600 dark:border-gray-700 dark:hover:border-transparent"
        >
          <div class="flex flex-col sm:-mx-4 sm:flex-row">
            <img
              class="flex-shrink-0 object-cover w-24 h-24 rounded-full sm:mx-4 ring-4 ring-gray-300"
              src="@/assets/img/Tess.png"
              alt="Tess"
            />
            <div class="mt-4 sm:mx-4 sm:mt-0">
              <h1
                class="text-xl font-semibold text-gray-700 capitalize md:text-2xl dark:text-white group-hover:text-white"
              >
                Tess van Uitert
              </h1>
              <p class="mt-2 text-gray-500 capitalize dark:text-gray-300 group-hover:text-gray-300">
                Scrum master
              </p>
            </div>
          </div>
          <p class="mt-4 text-gray-500 capitalize dark:text-gray-300 group-hover:text-gray-300">
            As a dedicated Scrum Master, I facilitate collaboration and ensure agile principles are upheld. My mission is to empower teams to deliver exceptional results by fostering clear communication and continuous improvement.
          </p>
        </div>

        <div
          class="px-12 py-8 transition-colors duration-300 transform border cursor-pointer rounded-xl hover:border-transparent group hover:bg-blue-600 dark:border-gray-700 dark:hover:border-transparent"
        >
          <div class="flex flex-col sm:-mx-4 sm:flex-row">
            <img
              class="flex-shrink-0 object-cover w-24 h-24 rounded-full sm:mx-4 ring-4 ring-gray-300"
            src="@/assets/img/Ibti.png"
              alt="Ibti"
            />
            <div class="mt-4 sm:mx-4 sm:mt-0">
              <h1
                class="text-xl font-semibold text-gray-700 capitalize md:text-2xl dark:text-white group-hover:text-white"
              >
                Ibtisam
              </h1>
              <p class="mt-2 text-gray-500 capitalize dark:text-gray-300 group-hover:text-gray-300">
                Senior developer
              </p>
            </div>
          </div>
          <p class="mt-4 text-gray-500 capitalize dark:text-gray-300 group-hover:text-gray-300">
            With years of experience in software development, I focus on building robust and efficient systems.
            I pride myself on combining technical expertise with a commitment to delivering high quality solutions that meet business needs.
          </p>
        </div>

        <div
          class="px-12 py-8 transition-colors duration-300 transform border cursor-pointer rounded-xl hover:border-transparent group hover:bg-blue-600 dark:border-gray-700 dark:hover:border-transparent"
        >
          <div class="flex flex-col sm:-mx-4 sm:flex-row">
            <img
              class="flex-shrink-0 object-cover w-24 h-24 rounded-full sm:mx-4 ring-4 ring-gray-300"
              src="@/assets/img/David.png"
              alt="David"
            />
            <div class="mt-4 sm:mx-4 sm:mt-0">
              <h1
                class="text-xl font-semibold text-gray-700 capitalize md:text-2xl dark:text-white group-hover:text-white"
              >
                David Bakker
              </h1>
              <p class="mt-2 text-gray-500 capitalize dark:text-gray-300 group-hover:text-gray-300">
                Senior Full Stack Developer
              </p>
            </div>
          </div>
          <p class="mt-4 text-gray-500 capitalize dark:text-gray-300 group-hover:text-gray-300">
            Combining creativity and technical expertise, I develop full stack solutions that drive success. My approach centers on bridging the gap between design and functionality to create impactful digital products.
          </p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup></script>

<style scoped>
html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
}

#app {
  height: 100%;
}
</style>
