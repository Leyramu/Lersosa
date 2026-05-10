import type { PluginOption } from 'vite';
/**
 * Vite 插件统一配置
 * @param viteEnv - 环境变量
 * @param isBuild - 是否为构建模式
 * @returns Vite 插件数组
 */
declare const _default: (viteEnv: Record<string, string>, isBuild?: boolean) => PluginOption[];
export default _default;
