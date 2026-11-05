import type { AuthMode } from '../types/api';

export function resolveConfiguredAuthMode(): AuthMode {
  const rawMode =
    (import.meta.env.VITE_AUTH_MODE as string | undefined) ||
    (import.meta.env.VITE_DEFAULT_AUTH_MODE as string | undefined);
  const mode = rawMode?.trim().toUpperCase();
  return mode === 'ZALO_AUTH' ? 'ZALO_AUTH' : 'DEMO_MODE';
}

export async function acquireZaloAccessToken(): Promise<string | null> {
  if (resolveConfiguredAuthMode() === 'DEMO_MODE') {
    return null;
  }
  try {
    const zmpSdk = await import('zmp-sdk');
    const token = await zmpSdk.getAccessToken({});
    return typeof token === 'string' && token.length > 0 ? token : null;
  } catch {
    return null;
  }
}

