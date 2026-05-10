import autoImport from 'unplugin-auto-import/vite';

/**
 * 自动导入插件配置
 * 功能：
 * - 自动导入 React Hooks (useState, useEffect, useCallback 等)
 * - 自动导入 React Router Hooks (useNavigate, useParams 等)
 * - 自动导入 Redux Hooks (useDispatch, useSelector)
 * - 自动生成类型声明文件
 */
export default () => {
  return autoImport({
    // 自动导入的库和模块
    imports: [
      'react',
      'react-router-dom',
      {
        // Redux 相关
        'react-redux': ['useDispatch', 'useSelector'],
        '@reduxjs/toolkit': ['createSlice', 'createAsyncThunk', 'createSelector'],
      },
    ],
    
    // 生成 ESLint 全局变量配置
    eslintrc: {
      enabled: true,
      filepath: './.eslintrc-auto-import.json',
      globalsPropValue: true,
    },
    
    // 解析器配置（可选，用于自动导入 UI 库）
    resolvers: [
      // 如果后续使用其他 UI 库可在此添加
    ],
    
    // 是否在 JSX/TSX 模板中自动导入
    dts: './src/types/auto-imports.d.ts',
  });
};
