/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import { RouteLocationNormalized } from 'vue-router';

export const useTagsViewStore = defineStore('tagsView', () => {
  const visitedViews = ref<RouteLocationNormalized[]>([]);
  const cachedViews = ref<string[]>([]);
  const iframeViews = ref<RouteLocationNormalized[]>([]);

  const getVisitedViews = (): RouteLocationNormalized[] => {
    return visitedViews.value as RouteLocationNormalized[];
  };
  const getIframeViews = (): RouteLocationNormalized[] => {
    return iframeViews.value as RouteLocationNormalized[];
  };
  const getCachedViews = (): string[] => {
    return cachedViews.value;
  };

  const addView = (view: RouteLocationNormalized) => {
    addVisitedView(view);
    addCachedView(view);
  };

  const addIframeView = (view: RouteLocationNormalized): void => {
    if (iframeViews.value.some((v: RouteLocationNormalized) => v.path === view.path)) return;
    iframeViews.value.push(
      Object.assign({}, view, {
        title: view.meta?.title || 'no-name'
      })
    );
  };
  const delIframeView = (view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> => {
    return new Promise((resolve) => {
      iframeViews.value = iframeViews.value.filter((item: RouteLocationNormalized) => item.path !== view.path);
      resolve([...(iframeViews.value as RouteLocationNormalized[])]);
    });
  };
  const addVisitedView = (view: RouteLocationNormalized): void => {
    if (visitedViews.value.some((v: RouteLocationNormalized) => v.path === view.path)) return;
    visitedViews.value.push(
      Object.assign({}, view, {
        title: view.meta?.title || 'no-name'
      })
    );
  };
  const delView = (
    view: RouteLocationNormalized
  ): Promise<{
    visitedViews: RouteLocationNormalized[];
    cachedViews: string[];
  }> => {
    return new Promise((resolve) => {
      delVisitedView(view).then((_) => {});
      if (!isDynamicRoute(view)) {
        delCachedView(view).then((_) => {});
      }
      resolve({
        visitedViews: [...(visitedViews.value as RouteLocationNormalized[])],
        cachedViews: [...cachedViews.value]
      });
    });
  };

  const delVisitedView = (view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> => {
    return new Promise((resolve) => {
      for (const [i, v] of visitedViews.value.entries()) {
        if (v.path === view.path) {
          visitedViews.value.splice(i, 1);
          break;
        }
      }
      resolve([...(visitedViews.value as RouteLocationNormalized[])]);
    });
  };
  const delCachedView = (view?: RouteLocationNormalized): Promise<string[]> => {
    let viewName = '';
    if (view) {
      viewName = view.name as string;
    }
    return new Promise((resolve) => {
      const index = cachedViews.value.indexOf(viewName);
      index > -1 && cachedViews.value.splice(index, 1);
      resolve([...cachedViews.value]);
    });
  };
  const delOthersViews = (
    view: RouteLocationNormalized
  ): Promise<{
    visitedViews: RouteLocationNormalized[];
    cachedViews: string[];
  }> => {
    return new Promise((resolve) => {
      delOthersVisitedViews(view).then((_) => {});
      delOthersCachedViews(view).then((_) => {});
      resolve({
        visitedViews: [...(visitedViews.value as RouteLocationNormalized[])],
        cachedViews: [...cachedViews.value]
      });
    });
  };

  const delOthersVisitedViews = (view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> => {
    return new Promise((resolve) => {
      visitedViews.value = visitedViews.value.filter((v: RouteLocationNormalized) => {
        return v.meta?.affix || v.path === view.path;
      });
      resolve([...(visitedViews.value as RouteLocationNormalized[])]);
    });
  };
  const delOthersCachedViews = (view: RouteLocationNormalized): Promise<string[]> => {
    const viewName = view.name as string;
    return new Promise((resolve) => {
      const index = cachedViews.value.indexOf(viewName);
      if (index > -1) {
        cachedViews.value = cachedViews.value.slice(index, index + 1);
      } else {
        cachedViews.value = [];
      }
      resolve([...cachedViews.value]);
    });
  };

  const delAllViews = (): Promise<{ visitedViews: RouteLocationNormalized[]; cachedViews: string[] }> => {
    return new Promise((resolve) => {
      delAllVisitedViews().then((_) => {});
      delAllCachedViews().then((_) => {});
      resolve({
        visitedViews: [...(visitedViews.value as RouteLocationNormalized[])],
        cachedViews: [...cachedViews.value]
      });
    });
  };
  const delAllVisitedViews = (): Promise<RouteLocationNormalized[]> => {
    return new Promise((resolve) => {
      visitedViews.value = visitedViews.value.filter((tag: RouteLocationNormalized) => tag.meta?.affix);
      resolve([...(visitedViews.value as RouteLocationNormalized[])]);
    });
  };

  const delAllCachedViews = (): Promise<string[]> => {
    return new Promise((resolve) => {
      cachedViews.value = [];
      resolve([...cachedViews.value]);
    });
  };

  const updateVisitedView = (view: RouteLocationNormalized): void => {
    for (let v of visitedViews.value) {
      if (v.path === view.path) {
        v = Object.assign(v, view);
        break;
      }
    }
  };
  const delRightTags = (view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> => {
    return new Promise((resolve) => {
      const index = visitedViews.value.findIndex((v: RouteLocationNormalized) => v.path === view.path);
      if (index === -1) {
        return;
      }
      visitedViews.value = visitedViews.value.filter((item: RouteLocationNormalized, idx: number) => {
        if (idx <= index || (item.meta && item.meta.affix)) {
          return true;
        }
        const i = cachedViews.value.indexOf(item.name as string);
        if (i > -1) {
          cachedViews.value.splice(i, 1);
        }
        return false;
      });
      resolve([...(visitedViews.value as RouteLocationNormalized[])]);
    });
  };
  const delLeftTags = (view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> => {
    return new Promise((resolve) => {
      const index = visitedViews.value.findIndex((v: RouteLocationNormalized) => v.path === view.path);
      if (index === -1) {
        return;
      }
      visitedViews.value = visitedViews.value.filter((item: RouteLocationNormalized, idx: number) => {
        if (idx >= index || (item.meta && item.meta.affix)) {
          return true;
        }
        const i = cachedViews.value.indexOf(item.name as string);
        if (i > -1) {
          cachedViews.value.splice(i, 1);
        }
        return false;
      });
      resolve([...(visitedViews.value as RouteLocationNormalized[])]);
    });
  };

  const addCachedView = (view: RouteLocationNormalized): void => {
    const viewName = view.name as string;
    if (!viewName) return;
    if (cachedViews.value.includes(viewName)) return;
    if (!view.meta?.noCache) {
      cachedViews.value.push(viewName);
    }
  };

  const isDynamicRoute = (view: RouteLocationNormalized): boolean => {
    // 检查匹配的路由记录中是否有动态段
    return view.matched.some((m) => m.path.includes(':'));
  };

  return {
    visitedViews,
    cachedViews,
    iframeViews,

    getVisitedViews,
    getIframeViews,
    getCachedViews,

    addVisitedView,
    addCachedView,
    delVisitedView,
    delCachedView,
    updateVisitedView,
    addView,
    delView,
    delAllViews,
    delAllVisitedViews,
    delAllCachedViews,
    delOthersViews,
    delRightTags,
    delLeftTags,
    addIframeView,
    delIframeView
  };
});
export default useTagsViewStore;
