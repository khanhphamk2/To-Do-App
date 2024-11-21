import React from "react";
import PropTypes from "prop-types";
import formatTime from "../../../utils/formatTime";

export default function TimePicker({ value, setValue }) {
  return (
    <div className="relative w-fit">
      <input
        type="time"
        id="time"
        className="bg-gray-50 border leading-none border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
        value={value}
        onChange={(e) => setValue(formatTime(e.target.value))}
        required
      />
    </div>
  );
}

TimePicker.propTypes = {
  value: PropTypes.string.isRequired,
  setValue: PropTypes.func.isRequired,
};
