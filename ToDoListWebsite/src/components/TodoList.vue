<template>
  <div class="todo-wrapper">
    <h1 class="list-title">Mein Tag</h1>

    <!-- Aufgaben-Liste -->
    <div class="task-list">

      <!-- Offene Aufgaben -->
      <ul>
        <TodoItem
          v-for="(todo, idx) in todos.filter(t => !t.done)"
          :key="'o-' + idx"
          :todo="todo"
          @toggle="toggleTodo"
        />
      </ul>

      <!-- Erledigt -->
      <h2 class="done-title" v-if="todos.some(t => t.done)">Erledigt</h2>

      <ul class="done-list">
        <TodoItem
          v-for="(todo, idx) in todos.filter(t => t.done)"
          :key="'d-' + idx"
          :todo="todo"
          @toggle="toggleTodo"
        />
      </ul>

    </div>

    <!-- Eingabefeld -->
    <div class="add-task">
      <input
        v-model="newTask"
        placeholder="Neue Aufgabe eingeben…"
        @keyup.enter="addTodo"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import TodoItem from './TodoItem.vue'

const todos = ref([
  { text: 'Milestone 2', done: false, important: false },
  { text: 'Einkaufen', done: false, important: false },
  { text: 'Sport machen', done: false, important: false }
])


const newTask = ref('')

function addTodo() {
  if (!newTask.value.trim()) return
  todos.value.push({ text: newTask.value.trim(), done: false })
  newTask.value = ''
}

function toggleTodo(todo) {
  todo.done = !todo.done
}
function toggleImportant(todo) {
  todo.important = !todo.important;
}

</script>

<style scoped>
.todo-wrapper {
  width: 700px;
  margin: 0 auto;
}

.list-title {
  font-size: 2.3rem;
  margin-bottom: 30px;
}

.task-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.task-list ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.done-title {
  margin-top: 40px;
  font-size: 1.4rem;
  color: #888;
}

.done-list .todo-item {
  opacity: 0.6;
}

.add-task input {
  width: 100%;
  padding: 16px;
  border-radius: 10px;
  border: 1px solid #ccc;
  margin-top: 30px;
  font-size: 1rem;
}
</style>
