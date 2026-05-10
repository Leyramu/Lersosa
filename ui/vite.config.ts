import {defineConfig, loadEnv} from 'vite';
import createPlugins from './vite/plugins';
import fs from 'node:fs';
import https from 'node:https';
import path from 'path';

const defaultConfig = {
  key: 'certs/lersosa-vite-server-key.pem',
  cert: 'certs/lersosa-vite-server-cert.pem',
  ca: 'certs/lersosa-ca-cert.pem',
  clientKey: 'certs/lersosa-vite-client-key.pem',
  clientCert: 'certs/lersosa-vite-client-cert.pem',
  url: 'https://localhost:8004',
} as const;

function isTrue(value?: string, defaultValue = false): boolean {
  if (value == null || value === '') {
    return defaultValue;
  }

  return ['true', '1', 'yes', 'on'].includes(value.toLowerCase());
}

function resolveFilePath(filePath: string): string {
  return path.isAbsolute(filePath) ? filePath : path.resolve(__dirname, filePath);
}

function readOptionalFile(filePath?: string): Buffer | undefined {
  if (!filePath) {
    return undefined;
  }

  const absolutePath = resolveFilePath(filePath);
  if (!fs.existsSync(absolutePath)) {
    return undefined;
  }

  return fs.readFileSync(absolutePath);
}

function createHttpsOptions(env: Record<string, string>) {
  const httpsEnabled = isTrue(env.VITE_APP_HTTPS, false);
  if (!httpsEnabled) {
    return undefined;
  }

  const keyPath = env.VITE_APP_HTTPS_KEY || defaultConfig.key;
  const certPath = env.VITE_APP_HTTPS_CERT || defaultConfig.cert;
  const caPath = env.VITE_APP_HTTPS_CA || defaultConfig.ca;

  const key = readOptionalFile(keyPath);
  const cert = readOptionalFile(certPath);
  const ca = readOptionalFile(caPath);

  if (!key || !cert) {
    throw new Error(
      `HTTPS 已启用，但未找到证书文件。请检查 VITE_APP_HTTPS_KEY / VITE_APP_HTTPS_CERT，当前 key=${resolveFilePath(keyPath)}, cert=${resolveFilePath(certPath)}`
    );
  }

  return {
    key,
    cert,
    ...(ca ? { ca } : {}),
    minVersion: 'TLSv1.2' as const,
  };
}

function createProxyAgent(env: Record<string, string>, target: string) {
  if (!/^https:\/\//i.test(target)) {
    return undefined;
  }

  const secure = isTrue(env.VITE_APP_PROXY_SECURE, true);
  const caPath = env.VITE_APP_PROXY_CA || env.VITE_APP_HTTPS_CA || defaultConfig.ca;
  const clientKeyPath = env.VITE_APP_PROXY_CLIENT_KEY || defaultConfig.clientKey;
  const clientCertPath = env.VITE_APP_PROXY_CLIENT_CERT || defaultConfig.clientCert;
  const ca = readOptionalFile(caPath);
  const key = readOptionalFile(clientKeyPath);
  const cert = readOptionalFile(clientCertPath);
  const passphrase = env.VITE_APP_PROXY_CLIENT_PASSPHRASE;

  if ((key && !cert) || (!key && cert)) {
    throw new Error(
      `HTTPS 代理的客户端证书配置不完整。请同时提供 VITE_APP_PROXY_CLIENT_KEY 与 VITE_APP_PROXY_CLIENT_CERT，当前 key=${resolveFilePath(clientKeyPath)}, cert=${resolveFilePath(clientCertPath)}`
    );
  }

  return new https.Agent({
    ...(ca ? { ca } : {}),
    ...(key ? { key } : {}),
    ...(cert ? { cert } : {}),
    ...(passphrase ? { passphrase } : {}),
    rejectUnauthorized: secure,
  });
}

// https://vite.dev/config/
export default defineConfig(({ mode, command }) => {
  // 加载环境变量
  const env = loadEnv(mode, process.cwd());
  const isBuild = command === 'build';
  const apiTarget = env.VITE_APP_API_URL || defaultConfig.url;
  const httpsOptions = createHttpsOptions(env);
  const proxySecure = isTrue(env.VITE_APP_PROXY_SECURE, true);
  const proxyAgent = createProxyAgent(env, apiTarget);

  return {
    // 基础路径（可通过环境变量配置）
    base: env.VITE_APP_BASE_PATH || '/',

    // 路径别名配置
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json'],
    },

    // 插件配置
    plugins: createPlugins(env, isBuild),

    // 开发服务器配置
    server: {
      host: '0.0.0.0',
      port: Number(env.VITE_APP_PORT) || 3000,
      https: httpsOptions,
      open: true, // 自动打开浏览器
      proxy: {
        '/api': {
          target: apiTarget,
          changeOrigin: isTrue(env.VITE_APP_PROXY_CHANGE_ORIGIN, true),
          ws: true, // 支持 WebSocket
          secure: proxySecure,
          ...(proxyAgent ? { agent: proxyAgent } : {}),
          rewrite: (path) => path.replace(/^\/api/, ''),
        },
      },
    },

    preview: {
      host: '0.0.0.0',
      port: Number(env.VITE_APP_PORT) || 3000,
      https: httpsOptions,
    },

    // CSS 配置
    css: {
      preprocessorOptions: {
        scss: {
          silenceDeprecations: ['legacy-js-api'],
        },
      },
    },

    // 构建优化配置
    build: {
      outDir: 'dist',
      sourcemap: isBuild ? false : 'inline',
      rollupOptions: {
        output: {
          // 分包策略优化
          manualChunks(id) {
            if (id.includes('node_modules')) {
              // React 相关库打包到 vendor
              if (id.includes('react') || id.includes('react-dom')) {
                return 'vendor';
              }
              // React Router 打包到 router
              if (id.includes('react-router')) {
                return 'router';
              }
              // Redux 相关打包到 redux
              if (id.includes('@reduxjs') || id.includes('react-redux')) {
                return 'redux';
              }
              // Radix UI 组件打包到 ui
              if (id.includes('@radix-ui')) {
                return 'ui';
              }
            }
          },
        },
      },
      chunkSizeWarningLimit: 1000, // chunk 大小警告限制（KB）
    },

    // 依赖预编译优化
    optimizeDeps: {
      include: [
        'react',
        'react-dom',
        'react-router-dom',
        '@reduxjs/toolkit',
        'react-redux',
        'axios',
        'lucide-react',
      ],
    },
  };
});
