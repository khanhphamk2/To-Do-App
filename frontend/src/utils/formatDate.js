
const formatDate = (date) => {
    if (date instanceof Date && !isNaN(date)) {
        return date.toISOString().split("T")[0];
    }
    return null;
};

export function parseDate(dateString) {
    const date = new Date(dateString);

    if (isNaN(date.getTime())) {
        throw new Error("Invalid date string");
    }

    return {
        startDate: date,
        endDate: date,
    };
}

export default formatDate;