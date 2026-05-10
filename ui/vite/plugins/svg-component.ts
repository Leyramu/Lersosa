import svgr from '@svgr/rollup';

/**
 * SVG 组件化插件
 * 功能：
 * - 将 SVG 文件转换为 React 组件
 * - 支持 import Icon from './icon.svg'
 * - 支持 <Icon /> 方式直接使用
 */
export default () => {
  return svgr({
    // SVGR 配置
    svgProps: {
      role: 'img',
    },
    prettier: false,
    typescript: true,
  });
};
