export interface Problem {
  title?: string;
  detail?: string;
  status?: number;
}

export class ApiError extends Error {
  constructor(message: string, readonly status: number) {
    super(message);
    this.name = 'ApiError';
  }
}

const apiBase = import.meta.env.VITE_API_BASE_URL ?? '';

async function csrfToken(): Promise<string> {
  const response = await fetch(`${apiBase}/bff/csrf`, { credentials: 'include' });
  if (!response.ok) throw new ApiError('Could not establish a secure session.', response.status);
  const payload = (await response.json()) as { token: string };
  return payload.token;
}

export async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const method = (init.method ?? 'GET').toUpperCase();
  const headers = new Headers(init.headers);
  if (init.body && !headers.has('Content-Type')) headers.set('Content-Type', 'application/json');

  if (!['GET', 'HEAD', 'OPTIONS'].includes(method)) {
    headers.set('X-XSRF-TOKEN', await csrfToken());
  }

  const response = await fetch(`${apiBase}${path}`, {
    ...init,
    method,
    headers,
    credentials: 'include',
  });

  if (!response.ok) {
    const contentType = response.headers.get('content-type') ?? '';
    const problem = contentType.includes('application/problem+json')
      ? ((await response.json().catch(() => null)) as Problem | null)
      : null;
    throw new ApiError(problem?.detail || problem?.title || 'The request could not be completed.', response.status);
  }

  if (response.status === 204) return undefined as T;
  return (await response.json()) as T;
}