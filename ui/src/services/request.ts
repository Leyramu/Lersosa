import axios, { AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import { HTTP_STATUS, STORAGE_KEYS, APP_CONFIG } from '@/constants';

// 请求拦截器配置
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API || '/api',
  timeout: APP_CONFIG.REQUEST_TIMEOUT,
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
  },
});

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: unknown) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (res: AxiosResponse) => {
    // 二进制数据直接返回
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
      return res.data;
    }

    if (!res.data || typeof res.data !== 'object' || !('code' in res.data)) {
      return Promise.resolve(res.data);
    }

    const code = Number(res.data.code ?? HTTP_STATUS.SUCCESS);
    const msg = res.data.msg || res.data.message || '系统未知错误';

    if (code === HTTP_STATUS.UNAUTHORIZED) {
      console.error('登录状态已过期，请重新登录');
      localStorage.removeItem(STORAGE_KEYS.TOKEN);
      window.location.href = '/login';
      return Promise.reject(new Error('无效的会话，或者会话已过期，请重新登录'));
    } else if (code !== HTTP_STATUS.SUCCESS) {
      console.error(msg);
      return Promise.reject(new Error(msg));
    } else {
      return Promise.resolve(res.data);
    }
  },
  (error: unknown) => {
    let errorMsg = '未知错误';
    if (error instanceof Error) {
      errorMsg = error.message;
    }
    
    if (errorMsg === 'Network Error') {
      errorMsg = '后端接口连接异常';
    } else if (errorMsg.includes('timeout')) {
      errorMsg = '系统接口请求超时';
    } else if (errorMsg.includes('Request failed with status code')) {
      errorMsg = `系统接口${errorMsg.slice(-3)}异常`;
    }
    console.error(errorMsg);
    return Promise.reject(new Error(errorMsg));
  }
);

export default service;
