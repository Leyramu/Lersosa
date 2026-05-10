import type { PluginOption } from 'vite';
import compression from 'vite-plugin-compression';

/**
 * 构建压缩插件配置
 * 功能：
 * - 支持 gzip 压缩
 * - 支持 brotli 压缩（更小的体积）
 * - 通过环境变量控制是否启用
 */
export default (env: Record<string, string>) => {
  const { VITE_BUILD_COMPRESS } = env;
  const plugins: PluginOption[] = [];

  if (VITE_BUILD_COMPRESS) {
    const compressList = VITE_BUILD_COMPRESS.split(',');
    
    // Gzip 压缩
    if (compressList.includes('gzip')) {
      plugins.push(
        compression({
          ext: '.gz',
          deleteOriginFile: false, // 保留原始文件
          threshold: 10240, // 只压缩大于 10KB 的文件
        })
      );
    }
    
    // Brotli 压缩（比 gzip 更小，但兼容性稍差）
    if (compressList.includes('brotli')) {
      plugins.push(
        compression({
          ext: '.br',
          algorithm: 'brotliCompress',
          deleteOriginFile: false,
          threshold: 10240,
        })
      );
    }
  }
  
  return plugins;
};
