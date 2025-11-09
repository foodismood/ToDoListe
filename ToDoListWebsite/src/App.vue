<script setup>
import TodoList from './components/TodoList.vue'
</script>

<template>
  <div class="page">
    <TodoList />
  </div>
</template>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
  background-color: #000; /* Schwarzer Hintergrund */
}

.page {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;   /* Vertikal mittig */
  align-items: center;       /* Horizontal mittig */
  text-align: center;
}
</style>
