import type { PluginOption } from 'vite';
import react from '@vitejs/plugin-react';

import createAutoImport from './auto-import';
import createCompression from './compression';
import createSvgComponent from './svg-component';

/**
 * Vite 插件统一配置
 * @param viteEnv - 环境变量
 * @param isBuild - 是否为构建模式
 * @returns Vite 插件数组
 */
export default (viteEnv: Record<string, string>, isBuild = false) => {
  const vitePlugins: PluginOption[] = [];
  
  // React 官方插件
  vitePlugins.push(react());
  
  // 自动导入插件
  vitePlugins.push(createAutoImport());
  
  // SVG 组件化插件
  vitePlugins.push(createSvgComponent());
  
  // 构建时启用压缩插件
  if (isBuild) {
    vitePlugins.push(...createCompression(viteEnv));
  }
  
  return vitePlugins;
};
