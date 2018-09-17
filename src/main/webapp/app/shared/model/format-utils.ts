export const ngbToDate = (date?: any): Date => {
    if (!date) {
        return null;
    }
    if (date instanceof Date) {
        return date;
    }
    return new Date(date.year, date.month - 1, date.day);
};

export const dateToNgb = (date?: any): any => {
    if (!date) {
        return null;
    }
    if (!(date instanceof Date)) {
        return date;
    }
   return  {
        year: date.getFullYear(),
        month: date.getMonth() + 1,
        day: date.getDate()
    };
};
