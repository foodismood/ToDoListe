<script setup>
import { computed, ref } from 'vue'

const props = defineProps({
  todo: { type: Object, required: true }
})
const emit = defineEmits(['toggle-completed', 'toggle-important', 'remove', 'update'])

const editing = ref(false)
const editTitle = ref(props.todo.title)
const editNotes = ref(props.todo.notes || '')
const editDue = ref(props.todo.dueDate || '')
const editCategory = ref(props.todo.category || '')

function formatDue(iso) {
  if (!iso) return ''
  const d = new Date(`${iso}T00:00:00`)
  return d.toLocaleDateString('de-DE', { day: '2-digit', month: 'short' })
}

const dueLabel = computed(() => formatDue(props.todo.dueDate))

function startEdit() {
  editing.value = true
  editTitle.value = props.todo.title
  editNotes.value = props.todo.notes || ''
  editDue.value = props.todo.dueDate || ''
  editCategory.value = props.todo.category || ''
}

function cancelEdit() {
  editing.value = false
}

function saveEdit() {
  if (!editTitle.value.trim()) return
  emit('update', {
    title: editTitle.value.trim(),
    notes: editNotes.value.trim() || null,
    dueDate: editDue.value || null,
    category: editCategory.value.trim() || null,
    important: props.todo.important,
    completed: props.todo.completed
  })
  editing.value = false
}
</script>

<template>
  <li class="row" :class="{ done: todo.completed }">
    <input
      class="cb"
      type="checkbox"
      :checked="todo.completed"
      @change="emit('toggle-completed', todo.id)"
      aria-label="Erledigt"
    />

    <div class="main" v-if="!editing">
      <div class="topline">
        <button class="title" @click="startEdit" :title="todo.notes || 'Bearbeiten'">
          {{ todo.title }}
        </button>

        <div class="meta">
          <span class="badge" v-if="todo.category">🏷 {{ todo.category }}</span>
          <span class="badge" v-if="todo.dueDate">📅 {{ dueLabel }}</span>
        </div>
      </div>

      <div class="notes" v-if="todo.notes">{{ todo.notes }}</div>
    </div>

    <div class="edit" v-else>
      <input v-model="editTitle" class="inp" placeholder="Titel" />
      <input v-model="editCategory" class="inp" placeholder="Kategorie (z.B. Uni)" />
      <input v-model="editDue" class="inp" type="date" />
      <textarea v-model="editNotes" class="ta" placeholder="Notizen (optional)" rows="2" />
      <div class="edit-actions">
        <button class="btn" @click="cancelEdit">Abbrechen</button>
        <button class="btn primary" @click="saveEdit">Speichern</button>
      </div>
    </div>

    <div class="actions" v-if="!editing">
      <button
        class="icon"
        :class="{ active: todo.important }"
        @click="emit('toggle-important', todo.id)"
        :aria-label="todo.important ? 'Nicht mehr wichtig' : 'Wichtig markieren'"
        :title="todo.important ? 'Wichtig' : 'Nicht wichtig'"
      >
        {{ todo.important ? '★' : '☆' }}
      </button>
      <button class="icon danger" @click="emit('remove', todo.id)" aria-label="Löschen" title="Löschen">🗑</button>
    </div>
  </li>
</template>

<style scoped>
.row {
  display: grid;
  grid-template-columns: 24px 1fr auto;
  gap: 12px;
  padding: 12px;
  border: 1px solid var(--border);
  background: #fff;
  border-radius: 14px;
}

.cb {
  margin-top: 4px;
}

.main {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.topline {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 10px;
}

.title {
  border: none;
  background: transparent;
  padding: 0;
  text-align: left;
  font-size: 14px;
  font-weight: 700;
  color: var(--text);
  cursor: pointer;
}

.title:hover {
  text-decoration: underline;
}

.notes {
  font-size: 13px;
  color: var(--muted);
  white-space: pre-wrap;
}

.meta {
  display: inline-flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.actions {
  display: inline-flex;
  gap: 8px;
}

.icon {
  width: 36px;
  height: 36px;
  border-radius: 12px;
  border: 1px solid var(--border);
  background: #fff;
  cursor: pointer;
  font-size: 16px;
}

.icon:hover {
  border-color: rgba(16, 24, 40, 0.18);
}

.icon.active {
  background: var(--accent-soft);
  border-color: rgba(37, 99, 235, 0.22);
  color: var(--accent);
}

.icon.danger:hover {
  border-color: rgba(239, 68, 68, 0.35);
}

.done .title {
  text-decoration: line-through;
  color: rgba(31, 35, 40, 0.55);
}

.edit {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.inp, .ta {
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 10px 12px;
  font-size: 14px;
}

.edit-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style>
