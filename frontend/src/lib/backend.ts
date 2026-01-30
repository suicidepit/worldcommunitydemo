const rawBaseUrl = process.env.NEXT_PUBLIC_BACKEND_URL ?? 'http://localhost:8080';

const normalizedBaseUrl = rawBaseUrl.endsWith('/')
  ? rawBaseUrl.slice(0, -1)
  : rawBaseUrl;

export const backendBaseUrl = normalizedBaseUrl;

/**
 * Small helper to build backend URLs consistently.
 */
export const buildBackendUrl = (path: string) => {
  const sanitizedPath = path.startsWith('/') ? path : `/${path}`;
  return `${backendBaseUrl}${sanitizedPath}`;
};
