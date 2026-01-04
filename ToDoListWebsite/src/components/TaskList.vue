<script setup>
import { computed, ref, watch } from 'vue'
import TaskRow from './TaskRow.vue'

const props = defineProps({
  todos: { type: Array, required: true },
  activeView: { type: String, required: true },
  selectedDateIso: { type: String, required: true },
  loading: { type: Boolean, required: true }
})

const emit = defineEmits(['add', 'toggle-completed', 'toggle-important', 'remove', 'update'])

const title = ref('')
const notes = ref('')
const dueDate = ref('')
const category = ref('')
const important = ref(false)

watch(
  () => props.activeView,
  (v) => {
    // For "Mein Tag" pre-fill dueDate with selected date so the task shows up immediately
    if (v === 'myday') dueDate.value = props.selectedDateIso
  },
  { immediate: true }
)

const categories = computed(() => {
  const set = new Set(props.todos.map(t => t.category).filter(Boolean))
  return ['Arbeit', 'Privat', 'Uni', ...Array.from(set)]
})

const openTodos = computed(() => props.todos.filter(t => !t.completed))
const doneTodos = computed(() => props.todos.filter(t => t.completed))

function resetForm() {
  title.value = ''
  notes.value = ''
  category.value = ''
  important.value = false
  // keep due date as-is for My Day
  if (props.activeView !== 'myday') dueDate.value = ''
}

async function submit() {
  const t = title.value.trim()
  if (!t) return
  await emit('add', {
    title: t,
    notes: notes.value?.trim() || null,
    dueDate: dueDate.value || null,
    category: category.value || null,
    important: important.value
  })
  resetForm()
}

function fmtDate(iso) {
  if (!iso) return ''
  const d = new Date(iso)
  return d.toLocaleDateString('de-DE', { day: '2-digit', month: '2-digit', year: 'numeric' })
}
</script>

<template>
  <section class="composer">
    <div class="row">
      <input
        v-model="title"
        class="input title"
        placeholder="Neue Aufgabe hinzufügen…"
        @keyup.enter="submit"
      />
      <button class="btn primary" @click="submit" :disabled="loading || !title.trim()">Hinzufügen</button>
    </div>

    <div class="row details">
      <input v-model="dueDate" class="input" type="date" />

      <select v-model="category" class="input">
        <option value="">Kategorie…</option>
        <option v-for="c in categories" :key="c" :value="c">{{ c }}</option>
      </select>

      <label class="chip" :class="{ on: important }">
        <input type="checkbox" v-model="important" />
        <span>Wichtig</span>
      </label>

      <input
        v-model="notes"
        class="input notes"
        placeholder="Notizen (optional)"
        @keyup.enter="submit"
      />
    </div>
  </section>

  <section class="list">
    <div class="muted" v-if="loading">Lade…</div>
    <div class="empty" v-else-if="props.todos.length === 0">
      <div class="empty-title">Noch keine Aufgaben</div>
      <div class="muted">Füge oben eine neue Aufgabe hinzu.</div>
    </div>

    <div v-else>
      <div class="group">
        <div class="group-title">Offen <span class="count">{{ openTodos.length }}</span></div>
        <TaskRow
          v-for="t in openTodos"
          :key="t.id"
          :todo="t"
          :fmt-date="fmtDate"
          @toggle-completed="emit('toggle-completed', t.id)"
          @toggle-important="emit('toggle-important', t.id)"
          @remove="emit('remove', t.id)"
          @update="payload => emit('update', t.id, payload)"
        />
      </div>

      <div class="group" v-if="doneTodos.length">
        <div class="group-title">Erledigt <span class="count">{{ doneTodos.length }}</span></div>
        <TaskRow
          v-for="t in doneTodos"
          :key="t.id"
          :todo="t"
          :fmt-date="fmtDate"
          done
          @toggle-completed="emit('toggle-completed', t.id)"
          @toggle-important="emit('toggle-important', t.id)"
          @remove="emit('remove', t.id)"
          @update="payload => emit('update', t.id, payload)"
        />
      </div>
    </div>
  </section>
</template>

<style scoped>
.composer {
  margin-top: 16px;
  padding: 14px;
  border: 1px solid var(--border);
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.6);
}

.row {
  display: flex;
  gap: 10px;
  align-items: center;
}

.details {
  margin-top: 10px;
  flex-wrap: wrap;
}

.input {
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 10px 12px;
  background: #fff;
  outline: none;
}

.input:focus {
  border-color: rgba(37, 99, 235, 0.35);
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.10);
}

.title {
  flex: 1;
  font-size: 14px;
}

.notes {
  flex: 1;
  min-width: 200px;
}

.chip {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 10px;
  border-radius: 999px;
  border: 1px solid var(--border);
  cursor: pointer;
  user-select: none;
}

.chip input {
  width: 16px;
  height: 16px;
}

.chip.on {
  border-color: rgba(37, 99, 235, 0.35);
  background: var(--accent-soft);
}

.list {
  margin-top: 18px;
}

.group {
  margin-top: 14px;
}

.group-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 800;
  margin-bottom: 10px;
}

.count {
  font-size: 12px;
  color: var(--muted);
  border: 1px solid var(--border);
  padding: 2px 8px;
  border-radius: 999px;
}

.empty {
  margin-top: 30px;
  border: 1px dashed var(--border);
  border-radius: 14px;
  padding: 18px;
}

.empty-title {
  font-weight: 800;
  margin-bottom: 6px;
}

@media (max-width: 720px) {
  .row {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
