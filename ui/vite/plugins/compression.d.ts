import type { PluginOption } from 'vite';
/**
 * 构建压缩插件配置
 * 功能：
 * - 支持 gzip 压缩
 * - 支持 brotli 压缩（更小的体积）
 * - 通过环境变量控制是否启用
 */
declare const _default: (env: Record<string, string>) => PluginOption[];
export default _default;
