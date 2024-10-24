import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import TimePicker from "../../controls/picker/TimePicker";
import { MdDeleteForever, MdOutlineUpdate } from "react-icons/md";
import DatePicker from "../../controls/picker/DatePicker";
import { parseDate } from "../../../utils/formatDate";

export default function TaskEdit({
  id,
  title,
  description,
  time,
  date,
  onSave,
  onCancel,
  deleteTask,
  addMode,
}) {
  const [editTitle, setEditTitle] = useState(title);
  const [editDescription, setEditDescription] = useState(description);
  const [editTime, setEditTime] = useState(time);
  const [editDate, setEditDate] = useState(date);

  const handleSave = () => {
    onSave({
      title: editTitle,
      description: editDescription,
      time: editTime,
      date: editDate,
    });
  };

  const handleRemove = (id) => {
    deleteTask(id);
  };

  useEffect(() => {
    console.log("time: ", editTime);
  }, [editTime]);

  return (
    <div className="bg-[#FFFFFF] p-4 rounded-xl shadow-lg">
      <div className="mb-3 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <span className="font-bold text-lg">Edit task</span>
        </div>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth={1.5}
          stroke="currentColor"
          className="size-4 cursor-pointer"
          onClick={() => onCancel(id)}
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M6 18 18 6M6 6l12 12"
          />
        </svg>
      </div>
      <input
        type="text"
        value={editTitle}
        onChange={(e) => setEditTitle(e.target.value)}
        placeholder="Edit title"
        className="w-full mb-2 p-2 border rounded-lg"
      />
      <textarea
        value={editDescription}
        onChange={(e) => setEditDescription(e.target.value)}
        placeholder="Edit description"
        className="w-full mb-2 p-2 border rounded-lg"
      />
      <div className="flex gap-3">
        <DatePicker setValue={setEditDate} value={parseDate(editDate)} />
        <TimePicker setValue={setEditTime} value={editTime} />
      </div>
      <div className="flex justify-end gap-2 mt-4">
        {!addMode && (
          <button
            onClick={() => deleteTask(id)}
            className="bg-red-500 hover:bg-red-700 text-white px-2 py-2 rounded-xl inline-flex items-center"
          >
            <MdDeleteForever className="size-6 mr-1" />
            Remove
          </button>
        )}
        <button
          onClick={handleSave}
          className="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded-xl flex items-center"
        >
          <MdOutlineUpdate className="size-6 mr-1" />
          {addMode ? "Confirm" : "Update"}
        </button>
        <div></div>
      </div>
    </div>
  );
}

TaskEdit.propTypes = {
  id: PropTypes.number.isRequired,
  title: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  time: PropTypes.string.isRequired,
  date: PropTypes.string.isRequired,
  onSave: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  deleteTask: PropTypes.func.isRequired,
  addMode: PropTypes.bool.isRequired,
};
