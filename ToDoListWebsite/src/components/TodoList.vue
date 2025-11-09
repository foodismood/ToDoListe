<template>
  <div class="todo-container">
    <h1>To-Do Liste</h1>
    <h2>Meine To-Dos</h2>

    <ul>
      <TodoItem v-for="(todo, index) in todos" :key="index" :todo="todo" />
    </ul>
  </div>
</template>

<script setup>
import TodoItem from './TodoItem.vue'

const todos = [
  { text: 'Milestone 2', done: false },
  { text: 'Einkaufen', done: false },
  { text: 'Sport machen', done: false }
]
</script>

<style scoped>
.todo-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  min-height: 100vh;
  background-color: #000;
  color: #2f53bb;
  text-align: center;
  padding-top: 120px;

  /* Diese Zeilen hinzufügen oder anpassen */
  width: 500%;           /* nimmt volle Bildschirmbreite */
  max-width: 1000px;      /* aber höchstens 800px breit, damit es nicht zu riesig wird */
  margin:  auto;        /* zentriert den Block horizontal */
}

h1 {
  font-size: 3.5rem; /* großer Titel */
  margin-bottom: 1rem;
}

h2 {
  font-size: 1.5rem;
  color: #aaa;
  margin-bottom: 2rem;
}

ul {
  padding: 0;
}

.todo-item {
  background: #ffffff;
  color: #0b0a0a;
  border-radius: 100px;
  padding: 20px;
  margin-bottom: 20px;
  width: 250px;
  text-align: left;
}
</style>
