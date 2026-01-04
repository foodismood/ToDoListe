const baseUrl = import.meta.env.VITE_APP_BACKEND_BASE_URL

function join(path) {
  return `${baseUrl}${path}`
}

async function request(path, options = {}) {
  const res = await fetch(join(path), {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  })

  if (!res.ok) {
    let msg = `${res.status} ${res.statusText}`
    try {
      const body = await res.json()
      if (body?.message) msg = body.message
    } catch {}
    throw new Error(msg)
  }

  if (res.status === 204) return null
  return res.json()
}

export const api = {
  getTodos() {
    return request('/api/todos')
  },
  createTodo(payload) {
    return request('/api/todos', { method: 'POST', body: JSON.stringify(payload) })
  },
  updateTodo(id, payload) {
    return request(`/api/todos/${id}`, { method: 'PUT', body: JSON.stringify(payload) })
  },
  toggleCompleted(id) {
    return request(`/api/todos/${id}/toggle-completed`, { method: 'PATCH' })
  },
  toggleImportant(id) {
    return request(`/api/todos/${id}/toggle-important`, { method: 'PATCH' })
  },
  deleteTodo(id) {
    return request(`/api/todos/${id}`, { method: 'DELETE' })
  }
}
