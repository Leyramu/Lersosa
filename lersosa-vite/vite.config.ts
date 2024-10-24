import {defineConfig, loadEnv, UserConfigExport} from 'vite';
import path from 'path';
import createVitePlugins from './vite/plugins';

export default defineConfig(({mode, command}: { mode: string; command: string }): UserConfigExport => {
    const env = loadEnv(mode, process.cwd());
    const {VITE_APP_ENV} = env;
    return {
        base: VITE_APP_ENV === 'production' ? '/' : '/',
        plugins: createVitePlugins(env, command === 'build'),
        resolve: {
            alias: {
                '~': path.resolve(__dirname, './'),
                '@': path.resolve(__dirname, './src')
            },
            extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
        },
        server: {
            port: 80,
            host: true,
            open: true,
            proxy: {
                '/dev-api': {
                    target: 'http://localhost:8080',
                    changeOrigin: true,
                    rewrite: (p: string) => p.replace(/^\/dev-api/, '')
                }
            }
        },
        css: {
            postcss: {
                plugins: [
                    {
                        postcssPlugin: 'internal:charset-removal',
                        AtRule: {
                            charset: (atRule: any) => {
                                if (atRule.name === 'charset') {
                                    atRule.remove();
                                }
                            }
                        }
                    }
                ]
            }
        }
    };
});
