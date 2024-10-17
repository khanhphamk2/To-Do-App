export const getLabelFromValue = (value, options = {}) => {
  const option = Object.values(options).find(
    (option) => option.value === value
  );
  return option ? option.label : '';
};
