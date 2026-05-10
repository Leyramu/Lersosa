/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_APP_BASE_API: string;
  readonly VITE_APP_BASE_PATH?: string;
  readonly VITE_APP_PORT?: string;
  readonly VITE_APP_API_URL?: string;
  readonly VITE_APP_HTTPS?: string;
  readonly VITE_APP_HTTPS_KEY?: string;
  readonly VITE_APP_HTTPS_CERT?: string;
  readonly VITE_APP_HTTPS_CA?: string;
  readonly VITE_APP_PROXY_SECURE?: string;
  readonly VITE_APP_PROXY_CHANGE_ORIGIN?: string;
  readonly VITE_APP_PROXY_CA?: string;
  readonly VITE_APP_PROXY_CLIENT_KEY?: string;
  readonly VITE_APP_PROXY_CLIENT_CERT?: string;
  readonly VITE_APP_PROXY_CLIENT_PASSPHRASE?: string;
  readonly VITE_BUILD_COMPRESS?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
