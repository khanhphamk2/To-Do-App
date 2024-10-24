import React from "react";
import PropTypes from "prop-types";
import Datepicker from "react-tailwindcss-datepicker";
import formatDate from "../../../utils/formatDate";

DatePicker.propTypes = {
  value: PropTypes.object.isRequired,
  setValue: PropTypes.func.isRequired,
};

export default function DatePicker({ value, setValue }) {
  const handleChange = (newValue) => {
    const newDate = newValue?.startDate ? new Date(newValue.startDate) : null;
    if (newDate) {
      setValue(formatDate(newDate));
    }
  };

  return (
    <Datepicker
      useRange={false}
      asSingle={true}
      containerClassName="relative"
      onChange={handleChange}
      value={value}
      classNames="bg-white"
    />
  );
}
