export const Log = (...params: any) => {
   if (__DEV__) console.log({ ...params });
};