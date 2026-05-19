const API_BASE_URL =
  import.meta.env.VITE_CBAM_API_BASE ?? "http://localhost:8080/api/cbam";

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers ?? {})
    },
    ...options
  });

  const data = await response.json().catch(() => null);

  if (!response.ok) {
    const error = new Error(data?.message ?? "Backend request failed.");
    error.payload = data;
    throw error;
  }

  return data;
}

export function getDemoData() {
  return request("/demo-data", { method: "GET" });
}

export function postCalculation(endpoint, payload) {
  return request(endpoint, {
    method: "POST",
    body: JSON.stringify(payload)
  });
}
