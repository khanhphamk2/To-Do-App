export const checkNullish = (test) => {
    if (!!test && test !== 'undefined' && test !== 'null') return test;
    else return undefined;
};
