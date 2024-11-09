import useUserStore from '@/store/modules/user';

/**
 * 字符权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export const checkPermi = (value: any): any => {
  if (value && value instanceof Array && value.length > 0) {
    const permissions = useUserStore().permissions;
    const permissionDatas = value;
    const all_permission = '*:*:*';

    return permissions.some((permission) => {
      return all_permission === permission || permissionDatas.includes(permission);
    });
  } else {
    console.error(`need roles! Like checkPermi="['system:user:add','system:user:edit']"`);
    return false;
  }
};

/**
 * 角色权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export const checkRole = (value: any): boolean => {
  if (value && value instanceof Array && value.length > 0) {
    const roles = useUserStore().roles;
    const permissionRoles = value;
    const super_admin = 'admin';

    return roles.some((role) => {
      return super_admin === role || permissionRoles.includes(role);
    });
  } else {
    console.error(`need roles! Like checkRole="['admin','editor']"`);
    return false;
  }
};
