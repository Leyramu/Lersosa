import type { PluginOption } from 'vite';

/**
 * 自动导入插件配置
 * 功能：
 * - 自动导入 React Hooks (useState, useEffect, useCallback 等)
 * - 自动导入 React Router Hooks (useNavigate, useParams 等)
 * - 自动导入 Redux Hooks (useDispatch, useSelector)
 * - 自动生成类型声明文件
 */
declare const _default: () => PluginOption | PluginOption[];
export default _default;
