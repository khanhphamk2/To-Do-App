import React, { useState } from "react";
import Datepicker from "react-tailwindcss-datepicker";

export default function DatePicker() {
  const [value, setValue] = useState(new Date());
  return (
    <Datepicker
      useRange={false}
      asSingle={true}
      containerClassName="relative"
      toggleClassName="absolute bg-blue-300 rounded-r-lg text-white right-0 h-full px-3 text-gray-400 focus:outline-none disabled:opacity-40 disabled:cursor-not-allowed"
      value={value}
      onChange={(newValue) => setValue(newValue)}
    />
  );
}
